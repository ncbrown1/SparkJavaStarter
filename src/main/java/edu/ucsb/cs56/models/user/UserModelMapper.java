package edu.ucsb.cs56.models.user;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UserModelMapper implements ResultSetMapper<UserModel> {

    @Override
    public UserModel map(int i, ResultSet resultSet, StatementContext statementContext)
    throws SQLException {
        return new UserModel(
            resultSet.getInt("id"),
            resultSet.getString("username"),
            resultSet.getString("name"),
            resultSet.getString("github_token")
        );
    }

}
