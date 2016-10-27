package edu.ucsb.cs56.models.user;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;


@RegisterMapper(UserModelMapper.class)
public interface UserDao
{
    @SqlUpdate("create table users (" +
        "id integer primary key auto_increment, " +
        "username varchar(50) UNIQUE, " +
        "name varchar(200), " +
        "github_token varchar(200)" +
    ")")
    void createUserTable();

    @SqlUpdate("insert into users " +
        "(username, name, github_token) values " +
        "(:username, :name, :github_token)"
    )
    void insert(
        @Bind("username") String username,
        @Bind("name") String name,
        @Bind("github_token") String github_token
    );

    @SqlQuery("select * from users where id = :id")
    UserModel findUserById(@Bind("id") int id);

    @SqlQuery("select * from users where username = :username")
    UserModel findUserByUsername(@Bind("username") String username);

    /**
     * close with no args is used to close the connection
     */
    void close();
}