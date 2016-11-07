package me.nickbrown.sparkjavastarter;

import com.google.common.collect.ImmutableMap;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.db.PostgresDatabaseType;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.typesafe.config.Config;

import me.nickbrown.sparkjavastarter.http.AuthController;
import me.nickbrown.sparkjavastarter.http.Controller;
import me.nickbrown.sparkjavastarter.models.User;
import org.postgresql.ds.PGPoolingDataSource;
import spark.route.RouteOverview;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import spark.Spark;

import static spark.debug.DebugScreen.enableDebugScreen;


/**
 * Created by ncbrown on 10/24/16.
 */
public class Application extends Controller {

    private ConnectionSource connectionSource;
    private static Map<String, Class> models =
        new ImmutableMap.Builder<String, Class>()
            .put("Users", User.class)
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
    
    private ConnectionSource getConnectionSource() throws SQLException {
        if (this.connectionSource != null) return this.connectionSource;
        PGPoolingDataSource source = new PGPoolingDataSource();
        source.setDataSourceName("Application Database");
        source.setServerName(config.getString("db.url"));
        source.setDatabaseName(config.getString("db.database"));
        source.setUser(config.getString("db.username"));
        source.setPassword(config.getString("db.password"));
        source.setMaxConnections(config.getInt("db.connections"));
        this.connectionSource = new DataSourceConnectionSource(source, new PostgresDatabaseType());
        return this.connectionSource;
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

        try {
            this.initialize_daos();
            this.testAddUser();
        } catch (SQLException se) {
            System.err.println("Could not establish a connection with the database.");
            System.exit(1);
        }
        
        this.publishRoutes();
        
        HashMap<String, String> model = new HashMap<>();
        model.put("name", "foobar");
        Spark.get("/", (req, res) -> "hi there"); //new ModelAndView(model, "home.hbs"), this);
        RouteOverview.enableRouteOverview(); // /debug/routeoverview/
        
        String env = System.getenv("ENV");
        env = (env == null ? "development" : env);
        
        if (env.equals("development")) {
            enableDebugScreen();
        }
    }
    
    private void initialize_daos() throws SQLException {
        User.dao = DaoManager.createDao(getConnectionSource(), User.class);
    }
    
    public void initialize_database() throws SQLException {
        models.forEach((name, model)  -> {
            try {
                System.out.print("Creating database table for " + name + "...");
                TableUtils.createTable(getConnectionSource(), model);
                System.out.println("Created!");
            } catch (SQLException e) {
                System.out.println("Error--The table probably already exists.");
                e.printStackTrace();
            }
        });
    }
    
    private void testAddUser() {
        try {
            User u = new User();
            u.setUsername("ncbrown");
            u.setName("Nick Brown");
            u.setGithub_token("asdf1234");
            User.dao.create(u);
            
            System.out.println(User.dao.queryForEq("username", "ncbrown"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
