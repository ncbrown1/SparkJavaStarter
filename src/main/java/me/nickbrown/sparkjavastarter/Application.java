package me.nickbrown.sparkjavastarter;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.typesafe.config.Config;

import me.nickbrown.sparkjavastarter.http.AuthController;
import me.nickbrown.sparkjavastarter.http.Controller;
import me.nickbrown.sparkjavastarter.models.Model;
import me.nickbrown.sparkjavastarter.models.User;
import spark.ModelAndView;
import spark.route.RouteOverview;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
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
            AuthController.class
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
            } catch (Exception e) {}
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
            ue.printStackTrace();
            System.err.println("Malformed database connection string in config. Check your syntax!");
            System.exit(1);
        }
        
        return this.connectionSource;
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

        try {
            this.initialize_daos();
        } catch (SQLException se) {
            System.err.println("Could not establish a connection with the database.");
            System.exit(1);
        }
        
        this.publishRoutes();
        
        HashMap<String, String> model = new HashMap<>();
        model.put("name", "foobar");
        Spark.get("/", (req, res) -> new ModelAndView(model, "home.hbs"), this);
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
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
    
    /*private void testAddUser() {
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
    }*/
}
