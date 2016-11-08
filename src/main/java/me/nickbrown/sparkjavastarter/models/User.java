package me.nickbrown.sparkjavastarter.models;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Created by ncbrown on 10/25/16.
 */
@DatabaseTable(tableName = "users")
public class User extends Model<User, Integer> {
    public static Dao<User, Integer> dao = null;
    
    @DatabaseField(generatedId = true)
    private Integer id;
    @DatabaseField(unique = true)
    private String username;
    @DatabaseField
    private String name;
    @DatabaseField
    private String github_token;

    public User() {}
    public User(Integer id, String username, String name, String github_token) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.github_token = github_token;
    }

    public Integer getId() { return id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGithub_token() { return github_token; }
    public void setGithub_token(String github_token) { this.github_token = github_token; }
    
    @Override
    public void setDao (Dao<User, Integer> dao) {
        User.dao = dao;
    }
    
    @Override
    public String toString () {
        return "(" + this.id + ") " + this.username + " - " + this.name;
    }
}
