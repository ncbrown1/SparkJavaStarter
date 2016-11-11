package me.nickbrown.sparkjavastarter.http;

import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.typesafe.config.Config;
import me.nickbrown.sparkjavastarter.utils.HandlebarsTemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by ncbrown on 10/27/16.
 */
public abstract class Controller extends HandlebarsTemplateEngine {
    protected transient final Logger logger = LoggerFactory.getLogger(getClass());
    
    protected Config config;
    
    public Controller(Config config) {
        this.config = config;
        handlebars.registerHelpers(this);
        handlebars.with(new ClassPathTemplateLoader("/templates", ".hbs"));
    }
    
    public abstract void publishRoutes();
}
