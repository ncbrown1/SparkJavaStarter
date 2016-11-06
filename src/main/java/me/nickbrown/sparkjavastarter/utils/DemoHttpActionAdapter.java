package me.nickbrown.sparkjavastarter.utils;

import org.pac4j.core.context.HttpConstants;
import org.pac4j.sparkjava.DefaultHttpActionAdapter;
import org.pac4j.sparkjava.SparkWebContext;
import spark.ModelAndView;
import spark.TemplateEngine;

import java.util.HashMap;

import static spark.Spark.halt;


/**
 * Created by ncbrown on 10/27/16.
 */
public class DemoHttpActionAdapter extends DefaultHttpActionAdapter {
    
    private final TemplateEngine templateEngine;
    
    
    public DemoHttpActionAdapter (final TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
    
    @Override
    public Object adapt(int code, SparkWebContext context) {
        switch (code) {
            case HttpConstants.UNAUTHORIZED:
                halt(401, templateEngine.render(
                    new ModelAndView(new HashMap<>(), "error401.mustache")
                ));
            case HttpConstants.FORBIDDEN:
                halt(403, templateEngine.render(
                    new ModelAndView(new HashMap<>(), "error403.mustache")
                ));
            default:
                return super.adapt(code, context);
        }
    }
}