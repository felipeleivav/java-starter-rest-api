package com.api.rest.arq.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;

import com.api.rest.arq.exception.ConnectionException;
import com.api.rest.utils.Constants;
import com.api.rest.utils.PropLoader;
import org.apache.log4j.Logger;

public class BaseDAO {
	private static Logger logger = Logger.getLogger(BaseDAO.class);
	
	private Connection connection;
	
	public BaseDAO() throws ConnectionException {
		createConnection();
	}
	
    protected void createConnection() throws ConnectionException {
        try {
			Class.forName(PropLoader.get(Constants.JDBC_DRIVER));
			connection = DriverManager.getConnection(PropLoader.get(Constants.JDBC_URL));
		} catch (SQLException e) {
			logger.error("Database error ", e);
			throw new ConnectionException(e.getMessage());
		} catch (ClassNotFoundException e) {
			logger.error("Database error ", e);
			throw new ConnectionException(e.getMessage());
		}
    }
    
    public void close() throws ConnectionException {
    	try {
    		if (connection!=null) {
    			connection.close();
    		}
		} catch (SQLException e) {
			logger.error("Database error ", e);
			throw new ConnectionException(e.getMessage());
		}
    }
    
    protected Connection getConnection() {
    	return this.connection;
    }
    
    protected PreparedStatement createQuery(String sqlQuery) throws ConnectionException {
    	try {
			return this.connection.prepareStatement(sqlQuery);
		} catch (SQLException e) {
			logger.error("Database error ", e);
			throw new ConnectionException(e.getMessage());
		}
    }
    
    protected PreparedStatement createQuery(String sqlQuery, int generatedKeys) throws ConnectionException {
    	try {
			return this.connection.prepareStatement(sqlQuery, generatedKeys);
		} catch (SQLException e) {
			logger.error("Database error ", e);
			throw new ConnectionException(e.getMessage());
		}
    }
    
    protected String preparePlaceHolders(int length) {
	    return String.join(",", Collections.nCopies(length, "?"));
	}
	
}
