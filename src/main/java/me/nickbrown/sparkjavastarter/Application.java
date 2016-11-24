package me.nickbrown.sparkjavastarter;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.typesafe.config.Config;

import me.nickbrown.sparkjavastarter.http.AuthController;
import me.nickbrown.sparkjavastarter.http.Controller;
import me.nickbrown.sparkjavastarter.http.MainController;
import me.nickbrown.sparkjavastarter.http.middleware.LoggingMiddleware;
import me.nickbrown.sparkjavastarter.models.Model;
import me.nickbrown.sparkjavastarter.models.User;
import org.eclipse.jetty.util.resource.Resource;
import spark.route.RouteOverview;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import spark.Spark;

import static spark.debug.DebugScreen.enableDebugScreen;


/**
 * Created by ncbrown on 10/24/16.
 */
public class Application extends Controller {

    private ConnectionSource connectionSource;
    private List<Class<? extends Model>> models =
        Arrays.asList(
            User.class
            // insert your other models here
        );
    private List<Class<? extends Controller>> controllers =
        Arrays.asList(
            AuthController.class,
            MainController.class
            // insert your other controllers here
        );

    public Application(Config config) {
        super(config);
    }

    @Override
    public void publishRoutes () {
        controllers.forEach((controller) -> {
            try {
                controller
                    .getConstructor(Config.class)
                    .newInstance(config)
                    .publishRoutes();
            } catch (Exception e) {
                logger.error("Problem publishing routes for controller.", e);
            }
        });
    }
    
    private ConnectionSource getConnectionSource() throws SQLException {
        if (this.connectionSource != null) return this.connectionSource;
        
        try {
            URI uri = new URI(config.getString("db.url"));
            String username = uri.getUserInfo().split(":")[0];
            String password = uri.getUserInfo().split(":")[1];
            int port = uri.getPort();
    
            String dbUrl = "jdbc:postgresql://" + uri.getHost() + ":" + port + uri.getPath();
    
            this.connectionSource = new JdbcConnectionSource(dbUrl, username, password);
        } catch (URISyntaxException ue) {
            logger.error("Malformed database connection string in config. Check your syntax!", ue);
            System.exit(1);
        }
        
        return this.connectionSource;
    }

    public void initialize() {
        // configure server settings
        Spark.port(config.getInt("port"));
        Spark.staticFileLocation("/META-INF/resources");
        
        // connect to database

        try {
            this.initialize_daos();
        } catch (SQLException se) {
            logger.error("Could not establish a connection with the database.", se);
            System.exit(1);
        }
        
        Spark.before("*", LoggingMiddleware::log);
        this.publishRoutes();
        RouteOverview.enableRouteOverview(); // /debug/routeoverview/
        
        String env = System.getenv("ENV");
        if (env == null || env.equals("development")) {
            enableDebugScreen();
        }
    }
    
    private void initialize_daos() throws SQLException {
        models.forEach((model_class) -> {
            try {
                model_class.getMethod("setupModel", ConnectionSource.class)
                    .invoke(model_class.newInstance(), getConnectionSource());
            } catch (Exception e) {
                logger.error("Problem setting up model connection and dao.", e);
                System.exit(1);
            }
        });
    }
    
}
