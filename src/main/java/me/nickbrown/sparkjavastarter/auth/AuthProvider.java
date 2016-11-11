package me.nickbrown.sparkjavastarter.auth;

import me.nickbrown.sparkjavastarter.models.User;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.oauth.profile.github.GitHubProfile;
import org.pac4j.sparkjava.SparkWebContext;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.Optional;


/**
 * Created by ncbrown on 11/10/16.
 */
public class AuthProvider {
    
    public static User getProfile(final Request request, final Response response) {
        final SparkWebContext context = new SparkWebContext(request, response);
        final ProfileManager<GitHubProfile> manager = new ProfileManager<>(context);
        
        Optional<GitHubProfile> ghp = manager.get(true);
        if (!ghp.isPresent()) return null;
        GitHubProfile profile = ghp.get();
        
        User u = new User(profile);
        try {
            User.dao.createOrUpdate(u);
        } catch (SQLException e) {
            System.err.println("Could not create user due to:\n\n");
            e.printStackTrace();
            u = null;
        }
        return u;
    }
    
}
