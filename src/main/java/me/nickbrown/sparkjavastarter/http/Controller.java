package me.nickbrown.sparkjavastarter.http;

import com.typesafe.config.Config;
import me.nickbrown.sparkjavastarter.utils.HandlebarsTemplateEngine;


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
