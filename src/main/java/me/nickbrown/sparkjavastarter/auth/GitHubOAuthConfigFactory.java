package me.nickbrown.sparkjavastarter.auth;

import me.nickbrown.sparkjavastarter.utils.DemoHttpActionAdapter;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.core.client.Clients;
import org.pac4j.oauth.client.GitHubClient;
import spark.TemplateEngine;


/**
 * Created by ncbrown on 10/27/16.
 */
public class GitHubOAuthConfigFactory implements ConfigFactory {
    
    private String github_client_id;
    private String github_client_secret;
    private String callback_url;
    
    private final TemplateEngine templateEngine;
    private final String scope;
    
    public GitHubOAuthConfigFactory(
        String github_client_id,
        String github_client_secret,
        String callback_url,
        TemplateEngine templateEngine,
        String scope
    ) {
        this.github_client_id = github_client_id;
        this.github_client_secret = github_client_secret;
        this.callback_url = callback_url;
        this.templateEngine = templateEngine;
        this.scope = scope;
    }
    
    public Config build() {
        
        GitHubClient githubClient = new GitHubClient(github_client_id, github_client_secret);
        if (scope != null && !scope.equals("")) {
            githubClient.setScope(scope);
        }
        
        Clients clients = new Clients(callback_url, githubClient);
        Config config = new Config(clients);
        
        config.setHttpActionAdapter(new DemoHttpActionAdapter(templateEngine));
        
        return config;
    }
}