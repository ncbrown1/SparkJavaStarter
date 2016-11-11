package me.nickbrown.sparkjavastarter.models;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.pac4j.oauth.profile.github.GitHubProfile;


/**
 * Created by ncbrown on 10/25/16.
 */
@DatabaseTable(tableName = "users")
public class User extends Model<User, String> {
    public static Dao<User, String> dao = null;
    
    @DatabaseField(id = true)
    private String id;
    @DatabaseField(unique = true)
    private String username;
    @DatabaseField
    private String name;
    @DatabaseField
    private String email;
    @DatabaseField
    private String github_token;
    
    private transient GitHubProfile profile;

    public User() {}
    public User(String id, String username, String name, String email, String github_token) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.github_token = github_token;
    }
    public User(GitHubProfile profile) {
        this.id = profile.getId();
        this.username = profile.getUsername();
        this.name = profile.getDisplayName();
        this.email = profile.getEmail();
        this.github_token = profile.getAccessToken();
        this.profile = profile;
    }
    
    public String getId () { return id; }
    public void setId (String id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail () { return email; }
    public void setEmail (String email) { this.email = email; }

    public String getGithub_token() { return github_token; }
    public void setGithub_token(String github_token) { this.github_token = github_token; }
    
    public GitHubProfile getProfile () { return profile; }
    public void setProfile (GitHubProfile profile) { this.profile = profile; }
    
    @Override
    public void setDao (Dao<User, String> dao) {
        User.dao = dao;
    }
    
    @Override
    public String toString () {
        return "(" + this.id + ") " + this.username + " - " + this.name;
    }
}
