package edu.ucsb.cs56.models.user;


/**
 * Created by ncbrown on 10/25/16.
 */
public class UserModel {
    private int id;
    private String username;
    private String name;
    private String github_token;

    public UserModel() {}
    public UserModel(int id, String username, String name, String github_token) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.github_token = github_token;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGithub_token() { return github_token; }
    public void setGithub_token(String github_token) { this.github_token = github_token; }
    
    @Override
    public String toString () {
        return this.username + " - " + this.name;
    }
}
