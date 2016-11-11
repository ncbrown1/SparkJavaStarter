package me.nickbrown.sparkjavastarter.utils;

import me.nickbrown.sparkjavastarter.auth.AuthProvider;
import me.nickbrown.sparkjavastarter.models.User;
import org.pac4j.sparkjava.SparkWebContext;
import spark.Request;
import spark.Response;

import java.util.HashMap;


/**
 * Created by ncbrown on 11/10/16.
 */
public class ViewModelUtil {
    
    public static HashMap<String, Object> generate(Request request, Response response) {
        HashMap<String, Object> model = new HashMap<>();
        SparkWebContext context = new SparkWebContext(request, response);
        
        boolean is_authed = AuthProvider.isAuthenticated(context);
        User u = AuthProvider.getLoggedInUser(context);
        HashMap<String, Object> auth = new HashMap<>();
        
        auth.put("is_authed", is_authed);
        auth.put("user", u);
        if (u != null) {
            auth.put("user_data", u.getProfile().getAttributes());
        } else {
            auth.put("user_data", "None");
        }
        
        model.put("auth", auth);
        
        return model;
    }
}
