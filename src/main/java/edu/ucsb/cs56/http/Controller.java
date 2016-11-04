package edu.ucsb.cs56.http;

import com.typesafe.config.Config;
import edu.ucsb.cs56.utils.HandlebarsTemplateEngine;


/**
 * Created by ncbrown on 10/27/16.
 */
public abstract class Controller extends HandlebarsTemplateEngine {
    
    protected Config config;
    
    public Controller(Config config) {
        this.config = config;
        handlebars.registerHelpers(this);
    }
    
    public abstract void publishRoutes();
}
