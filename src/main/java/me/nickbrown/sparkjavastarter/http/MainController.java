package me.nickbrown.sparkjavastarter.http;

import com.typesafe.config.Config;
import spark.ModelAndView;
import spark.Spark;
import spark.TemplateViewRoute;

import java.util.HashMap;


/**
 * Created by ncbrown on 11/8/16.
 */
public class MainController extends Controller {
    
    public MainController (Config config) {
        super(config);
    }
    
    @Override
    public void publishRoutes () {
        Spark.get("/", index(), this);
    }
    
    public TemplateViewRoute index() {
        HashMap<String, String> model = new HashMap<>();
        model.put("name", "foobar");
        return (req, res) -> new ModelAndView(model, "index");
    }
}
