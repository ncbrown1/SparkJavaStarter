package edu.ucsb.cs56;

import com.typesafe.config.Config;

import spark.route.RouteOverview;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.staticFiles;

/**
 * Created by ncbrown on 10/24/16.
 */
public class Application {

    private Config config;

    public Application(Config config) {
        this.config = config;
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

        // set up routes

        get("/", (req, res) -> "Hello World!");
        RouteOverview.enableRouteOverview(); // /debug/routeoverview/
    }
}
