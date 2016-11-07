package me.nickbrown.sparkjavastarter;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import me.nickbrown.sparkjavastarter.utils.CLIParser;


/**
 * Created by ncbrown on 10/25/16.
 */
public class RunApplication {
    
    public static String help_message() {
        String msg = "USAGE: java -jar app.jar [OPTIONS] [COMMAND]\n\n";
        
        msg += "Options:\n";
        msg += "\t-h, --help\t\t\tshow this help message and exit\n";
        
        msg += "\n";
        
        msg += "Commands:\n";
        msg += "\tserve, s\t\t\tserve the application (default command)\n";
        msg += "\tshow-routes, sr\t\tlist all application routes\n";
        msg += "\tinit-db, idb\t\tinitialize the database and create all required tables\n";
        
        return msg;
    }

    public static void main(String[] args) throws Exception {
        String env = System.getenv("ENV");
        env = (env == null ? "development" : env);

        Config config = ConfigFactory.load();
        // select desired set of configs based on current environment
        config = config.getConfig(env).withFallback(config);
    
        Application app = new Application(config);
    
        CLIParser params = new CLIParser(args);
        boolean help = params.switchPresent("--help") || params.switchPresent("-h");
        // allows override of environment/config port when testing
        Long port = params.switchLongValue("--port", null);
        port = port == null ? params.switchLongValue("-p", null) : port;
        
        String[] targets = params.targets();
        
        if (help) {
            System.out.println(help_message());
            return;
        }
        
        if (targets.length >= 1) {
            switch (targets[0]) {
                case "show-routes": case "sr":
                    System.out.println("Unsupported for now");
                    return;
                case "init-db": case "idb":
                    app.initialize_database();
                    return;
                case "serve": case "s":
                    app.initialize(port);
                    break;
                default:
                    System.out.println(help_message());
            }
        } else {
            app.initialize(null);
        }
    }

}
