package me.nickbrown.sparkjavastarter.http;

import com.typesafe.config.Config;
import spark.*;

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
        Spark.get("/", MainController::index, this);
    }
    
    public static ModelAndView index(Request request, Response response) {
        HashMap<String, String> model = new HashMap<>();
        model.put("name", "foobar");
        return new ModelAndView(model, "index");
    }
}
