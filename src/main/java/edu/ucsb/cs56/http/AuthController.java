package edu.ucsb.cs56.http;

import com.typesafe.config.Config;
import edu.ucsb.cs56.auth.GitHubOAuthConfigFactory;
import org.pac4j.sparkjava.ApplicationLogoutRoute;
import org.pac4j.sparkjava.CallbackRoute;
import org.pac4j.sparkjava.SecurityFilter;
import spark.ModelAndView;
import spark.TemplateEngine;
import spark.TemplateViewRoute;

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
    
    public AuthController(Config config, TemplateEngine engine) {
        super(config, engine);
        this.authConfig = new GitHubOAuthConfigFactory(
            this.config.getString("app.github_client_id"),
            this.config.getString("app.github_secret"),
            this.config.getString("app.github_callback_url"),
            this.engine,
            this.config.getString("app.github_scopes")
        ).build();
        this.authFilter = new SecurityFilter(authConfig, "GithubClient", "", "");
    }
    
    @Override
    public void publishRoutes () {
        before("/login", authFilter);
        get("/login", loginRoute(), this.engine);
        get("/logout", new ApplicationLogoutRoute(authConfig, "/"));
    
        final CallbackRoute callback = new CallbackRoute(authConfig);
        get("/auth/callback", callback);
        post("/auth/callback", callback);
    }
    
    public TemplateViewRoute loginRoute() {
        HashMap<String, String> context = new HashMap<>();
        context.put("name", "logged in user");
        return (req, res) -> new ModelAndView(context, "home.hbs");
    }
}
