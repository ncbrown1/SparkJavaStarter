package edu.ucsb.cs56;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import edu.ucsb.cs56.models.user.UserDao;
import org.skife.jdbi.v2.DBI;


/**
 * Created by ncbrown on 10/25/16.
 */
public class RunApplication {

    public static void main(String[] args) {
        String env = System.getenv("ENV");
        env = (env == null ? "development" : env);

        Config config = ConfigFactory.load();
        // select desired set of configs based on current environment
        config = config.getConfig(env).withFallback(config);

        DBI dbi = new DBI("jdbc:h2:mem:test");
        UserDao users = dbi.open(UserDao.class);
        users.createUserTable();
        
        users.insert("ncbrown", "Nick Brown", "asdf1234");
        
        System.out.println(users.findUserByUsername("ncbrown"));
        
        Application app = new Application(config);
        app.initialize();
    }

}
