package edu.ucsb.cs56.http;

import com.typesafe.config.Config;
import spark.TemplateEngine;


/**
 * Created by ncbrown on 10/27/16.
 */
public abstract class Controller {
    
    protected Config config;
    protected TemplateEngine engine;
    
    public Controller(Config config, TemplateEngine engine) {
        this.config = config;
        this.engine = engine;
    }
    
    public abstract void publishRoutes();
}
