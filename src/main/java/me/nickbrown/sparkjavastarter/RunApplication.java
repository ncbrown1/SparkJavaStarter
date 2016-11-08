package me.nickbrown.sparkjavastarter;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


/**
 * Created by ncbrown on 10/25/16.
 */
public class RunApplication {
    
    public static void main(String[] args) throws Exception {
        String env = System.getenv("ENV");
        env = (env == null ? "development" : env);

        Config config = ConfigFactory.load();
        // select desired set of configs based on current environment
        config = config.getConfig(env).withFallback(config);
    
        Application app = new Application(config);
        app.initialize();
    }

}
