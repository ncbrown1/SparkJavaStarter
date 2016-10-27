package edu.ucsb.cs56;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import com.typesafe.config.Config;

import edu.ucsb.cs56.models.user.UserDao;
import org.skife.jdbi.v2.DBI;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;
import spark.route.RouteOverview;

import javax.sql.DataSource;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.staticFiles;

/**
 * Created by ncbrown on 10/24/16.
 */
public class Application {

    private Config config;
    private DBI database;

    public Application(Config config) {
        this.config = config;
    }
    
    private DataSource getDBConnString() {
        DataSource source;
        switch (config.getString("db.driver")) {
            case "mysql":
                source = new MysqlConnectionPoolDataSource();
                ((MysqlConnectionPoolDataSource)source).setServerName(config.getString("db.url"));
                ((MysqlConnectionPoolDataSource)source).setPort(config.getInt("db.port"));
                ((MysqlConnectionPoolDataSource)source).setDatabaseName(config.getString("db.database"));
                ((MysqlConnectionPoolDataSource)source).setUser(config.getString("db.username"));
                ((MysqlConnectionPoolDataSource)source).setPassword(config.getString("db.password"));
                break;
            case "sqlite":default:
                source = new SQLiteConnectionPoolDataSource();
                ((SQLiteConnectionPoolDataSource)source).setUrl("jdbc:sqlite:" + config.getString("db.url"));
                break;
        }
        return source;
    }

    public void initialize() {

        // configure server settings

        port(config.getInt("port"));
        if (config.getBoolean("localhost")) {
            String projectDir = System.getProperty("user.dir");
            String staticDir = "/src/main/resources/public";
            staticFiles.externalLocation(projectDir + staticDir);
        } else {
            staticFiles.location("/public");
        }
        
        // connect to database

        this.database = new DBI(this.getDBConnString());
        UserDao users = database.open(UserDao.class);
        
        try {
            users.createUserTable();
            users.insert("ncbrown", "Nick Brown", "asdf1234");
        } catch (Exception e) {
//            e.printStackTrace();
        }

        System.out.println(users.findUserByUsername("ncbrown"));

        users.close();

        users = database.open(UserDao.class);
        System.out.println(users.findUserByUsername("ncbrown"));

        users.close();

        // set up routes

        get("/", (req, res) -> "Hello World!");
        RouteOverview.enableRouteOverview(); // /debug/routeoverview/
    }
}
