package edu.ucsb.cs56;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


/**
 * Created by ncbrown on 10/25/16.
 */
public class RunApplication {

    public static void main(String[] args) {
        String env = System.getenv("ENV");
        env = (env == null ? "development" : env);

        Config config = ConfigFactory.load();
        // select desired set of configs based on current environment
        config = config.getConfig(env).withFallback(config);
        
        Application app = new Application(config);
        app.initialize();
    }

}
