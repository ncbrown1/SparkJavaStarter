package edu.ucsb.cs56;

import com.typesafe.config.Config;

import edu.ucsb.cs56.http.AuthController;
import edu.ucsb.cs56.http.Controller;
import edu.ucsb.cs56.models.user.UserDao;
import edu.ucsb.cs56.utils.HandlebarsTemplateEngine;
import org.postgresql.ds.PGPoolingDataSource;
import org.skife.jdbi.v2.DBI;
import spark.ModelAndView;
import spark.route.RouteOverview;

import javax.sql.DataSource;

import java.util.Arrays;
import java.util.HashMap;

import spark.Spark;


/**
 * Created by ncbrown on 10/24/16.
 */
public class Application {

    private Config config;
    private DBI database;
    private static final HandlebarsTemplateEngine templateEngine = new HandlebarsTemplateEngine();

    public Application(Config config) {
        this.config = config;
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

    public void initialize() {

        // configure server settings

        Spark.port(config.getInt("port"));
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
        
        Arrays.asList(
            new AuthController(config)
        ).forEach(Controller::publishRoutes);
        
        HashMap<String, String> model = new HashMap<>();
        model.put("name", "foobar");
        Spark.get("/", (req, res) -> new ModelAndView(model, "home.hbs"), templateEngine);
        RouteOverview.enableRouteOverview(); // /debug/routeoverview/
    }
    
    private void testAddUser() {
        UserDao users = database.open(UserDao.class);
    
        try {
            users.createUserTable();
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
