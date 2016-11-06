package me.nickbrown.sparkjavastarter;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;

import me.nickbrown.sparkjavastarter.http.AuthController;
import me.nickbrown.sparkjavastarter.http.Controller;
import me.nickbrown.sparkjavastarter.models.Dao;
import me.nickbrown.sparkjavastarter.models.user.UserDao;
import org.postgresql.ds.PGPoolingDataSource;
import org.skife.jdbi.v2.DBI;
import spark.ModelAndView;
import spark.route.RouteOverview;

import javax.sql.DataSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import spark.Spark;


/**
 * Created by ncbrown on 10/24/16.
 */
public class Application extends Controller {

    private DBI database;
    private static Map<String, Class<? extends Dao>> models =
        new ImmutableMap.Builder<String, Class<? extends Dao>>()
            .put("Users", UserDao.class)
            .build();

    public Application(Config config) {
        super(config);
    }

    @Override
    public void publishRoutes () {
        Arrays.asList(
            new AuthController(config)
        ).forEach(Controller::publishRoutes);
    }
    
    private DataSource getDataSource() {
        PGPoolingDataSource source = new PGPoolingDataSource();
        source.setDataSourceName("Application Database");
        source.setServerName(config.getString("db.url"));
        source.setDatabaseName(config.getString("db.database"));
        source.setUser(config.getString("db.username"));
        source.setPassword(config.getString("db.password"));
        source.setMaxConnections(config.getInt("db.connections"));
        return source;
    }

    public void initialize(Long port) {

        // configure server settings
        if (port == null) {
            Spark.port(config.getInt("port"));
        } else {
            Spark.port(port.intValue());
        }

        if (config.getBoolean("localhost")) {
            String projectDir = System.getProperty("user.dir");
            String staticDir = "/src/main/resources/public";
            Spark.staticFiles.externalLocation(projectDir + staticDir);
        } else {
            Spark.staticFiles.location("/public");
        }
        
        // connect to database

        this.database = new DBI(this.getDataSource());
        this.testAddUser();
        
        this.publishRoutes();
        
        HashMap<String, String> model = new HashMap<>();
        model.put("name", "foobar");
        Spark.get("/", (req, res) -> new ModelAndView(model, "home.hbs"), this);
        RouteOverview.enableRouteOverview(); // /debug/routeoverview/
    }
    
    public void initialize_database() {
        this.database = new DBI(this.getDataSource());
        models.forEach((name, model) -> {
            Dao dao = database.open(model);
            try {
                System.out.print("Creating database table for " + name + "...");
                dao.createTable();
                System.out.println("Created!");
            } catch (Exception e) {
                System.out.println("Error--The table probably already exists.");
//                e.printStackTrace();
            }
            dao.close();
        });
    }
    
    private void testAddUser() {
        UserDao users = database.open(UserDao.class);
    
        try {
            users.insert("ncbrown", "Nick Brown", "asdf1234");
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        System.out.println(users.findUserByUsername("ncbrown"));
    
        users.close();
    
        users = database.open(UserDao.class);
        System.out.println(users.findUserByUsername("ncbrown"));
    
        users.close();
    }
}
