package me.nickbrown.sparkjavastarter.http;

import com.typesafe.config.Config;
import me.nickbrown.sparkjavastarter.auth.GitHubOAuthConfigFactory;
import me.nickbrown.sparkjavastarter.utils.ViewModelUtil;
import org.pac4j.sparkjava.ApplicationLogoutRoute;
import org.pac4j.sparkjava.CallbackRoute;
import org.pac4j.sparkjava.SecurityFilter;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.before;


/**
 * Created by ncbrown on 10/27/16.
 */
public class AuthController extends Controller {
    
    private org.pac4j.core.config.Config authConfig;
    private final SecurityFilter authFilter;
    
    public AuthController(Config config) {
        super(config);
        this.authConfig = new GitHubOAuthConfigFactory(
            this.config.getString("app.github_client_id"),
            this.config.getString("app.github_secret"),
            this.config.getString("app.github_callback_url"),
            this,
            this.config.getString("app.github_scopes")
        ).build();
        this.authFilter = new SecurityFilter(authConfig, "GithubClient", "", "");
    }
    
    @Override
    public void publishRoutes () {
        before("/login", authFilter);
        get("/login", (req, res) -> {
            res.redirect("/");
            return null;
        });
        get("/logout", new ApplicationLogoutRoute(authConfig, "/"));
    
        before("/logged_in", authFilter);
        get("/logged_in", AuthController::testLogin, this);
    
        final CallbackRoute callback = new CallbackRoute(authConfig);
        get("/auth/callback", callback);
        post("/auth/callback", callback);
    }
    
    public String dostuff(String bar) {
        return "dynamix " + bar;
    }
    
    private static ModelAndView testLogin(Request request, Response response) {
        HashMap<String, Object> model = ViewModelUtil.generate(request, response);
        return new ModelAndView(model, "login");
    }
}
