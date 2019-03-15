package com.api.rest.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.api.rest.arq.dao.BaseDAO;
import com.api.rest.arq.exception.ConnectionException;
import com.api.rest.bean.UserBean;
import org.apache.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAO extends BaseDAO {
	private static Logger logger = Logger.getLogger(UserDAO.class);

	public UserDAO() throws ConnectionException {
		super();
	}

	public boolean validateUser(String username, String password) throws ConnectionException {
		boolean isValid = false;
		try {
			PreparedStatement stmt = createQuery("SELECT password FROM \"user\" WHERE username=?");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String storedPass = rs.getString(1);
				isValid = BCrypt.checkpw(password, storedPass);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			logger.error("Database error ", e);
			throw new ConnectionException(e.getMessage());
		}
		return isValid;
	}

	public int getUserId(String username) throws ConnectionException {
		int userId = 0;
		try {
			PreparedStatement stmt = createQuery("SELECT id FROM \"user\" WHERE username=?");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				userId = rs.getInt(1);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			logger.error("Database error ", e);
			throw new ConnectionException(e.getMessage());
		}
		return userId;
	}

	public int userExists(String username) throws ConnectionException {
		int returnedUserId = 0;
		try {
			PreparedStatement stmt = createQuery("SELECT id FROM \"user\" WHERE username=?");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				returnedUserId = rs.getInt(1);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			logger.error("Database error ", e);
			throw new ConnectionException(e.getMessage());
		}
		return returnedUserId;
	}

	public boolean registeredEmail(String email) throws ConnectionException {
		try {
			boolean exists = false;
			PreparedStatement stmt = createQuery("SELECT id FROM \"user\" WHERE email=?");
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				exists = !exists;
				break;
			}
			rs.close();
			stmt.close();
			return exists;
		} catch (SQLException e) {
			logger.error("Database error ", e);
			throw new ConnectionException(e.getMessage());
		}
	}

	public boolean createUser(UserBean user) throws ConnectionException {
		boolean res = false;
		try {
			String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(15));
			PreparedStatement stmt = createQuery("INSERT INTO \"user\" (username,password,email) VALUES(?,?,?,?)");
			stmt.setString(1, user.getUsername());
			stmt.setString(2, hashed);
			stmt.setString(3, user.getEmail());
			res = stmt.executeUpdate() > 0;
			stmt.close();
		} catch (SQLException e) {
			logger.error("Database error ", e);
			throw new ConnectionException(e.getMessage());
		}
		return res;
	}

	public void updateLastActivityDate(int userId) throws ConnectionException {
		try {
			PreparedStatement stmt = createQuery("UPDATE \"user\" SET last_activity=NOW() WHERE id=?");
			stmt.setInt(1, userId);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			logger.error("Database error ", e);
			throw new ConnectionException(e.getMessage());
		}
	}

}
