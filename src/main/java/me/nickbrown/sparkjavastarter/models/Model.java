package me.nickbrown.sparkjavastarter.models;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;


/**
 * Created by ncbrown on 11/8/16.
 */
public abstract class Model<T extends Model, ID> {
    
    protected transient final Logger logger = LoggerFactory.getLogger(getClass());
    
    public void setupModel(ConnectionSource source) throws SQLException {
        Dao<T, ID> dao = null;
        try {
            dao = DaoManager.createDao(source, (Class<T>) getClass());
            TableUtils.createTable(dao);
        } catch (SQLException e) {
            if (dao == null) {
                logger.error("Could not create DAO for model", e);
                throw new SQLException("Could not connect to database");
            } // otherwise, it was an error creating the table, which probably already exists
            else {
                logger.error("Could not create table for model", e);
            }
        }
        setDao(dao);
    }
    
    public abstract void setDao (Dao<T, ID> dao);
}
