package com.api.rest.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.api.rest.utils.Constants;
import com.api.rest.utils.PropLoader;
import org.apache.log4j.Logger;

import com.api.rest.arq.dao.BaseDAO;
import com.api.rest.arq.exception.ConnectionException;

public class InitDAO extends BaseDAO {
	private static Logger logger = Logger.getLogger(InitDAO.class);
	
	public InitDAO () throws ConnectionException {
		super();
	}
	
	public boolean isDbInstalled() throws ConnectionException {
		int requiredTables = 0;
		int tableCounter = 0;
		try {
			PreparedStatement stmt;
			if (PropLoader.get(Constants.JDBC_DRIVER).toUpperCase().contains("POSTGRE")) {
				stmt = createQuery("SELECT tablename FROM pg_catalog.pg_tables");
			} else {
				stmt = createQuery("SHOW TABLES");
			}
			ResultSet rs = stmt.executeQuery();
			/*
				SCHEMA VALIDATION
				==================
				Here you can put your tables separated w/ and OR operator
				in order to validate all of them exists in the remote database schema
			 */
			requiredTables = 1;
			while (rs.next()) {
				String table = rs.getString(1);
				if (table.equalsIgnoreCase("user")) {
					tableCounter++;
				}
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			logger.error("Database error ", e);
			throw new ConnectionException(e.getMessage());
		}
		return requiredTables==tableCounter;
	}
	
	public boolean testConnection() throws ConnectionException {
		boolean ok = false;
		try {
			PreparedStatement stmt = createQuery("SELECT 1");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				rs.getInt(1);
			}
			rs.close();
			stmt.close();
			ok = true;
		} catch (SQLException e) {
			logger.error("Database error ", e);
			throw new ConnectionException(e.getMessage());
		}
		return ok;
	}
	
}
