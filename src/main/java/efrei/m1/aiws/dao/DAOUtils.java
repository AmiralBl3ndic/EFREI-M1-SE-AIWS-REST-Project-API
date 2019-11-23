package efrei.m1.aiws.dao;

import efrei.m1.aiws.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import static efrei.m1.aiws.dao.UserDAOImpl.*;

/**
 * Static class to handle multiple DAO-related repetitive actions
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DAOUtils {

	private static final Logger logger = Logger.getLogger(DAOUtils.class.getName());

	private static final String ERROR_UNABLE_TO_CLOSE = "Unable to close statement.";

	/**
	 * Initiate a {@link PreparedStatement} with all its parameters already set
	 * @param connection Database connection to use
	 * @param sql SQL query to prepare
	 * @param returnGeneratedKeys Whether to return the generated keys
	 * @param object List of parameters to set for the {@link PreparedStatement}
	 * @return A {@link PreparedStatement} with all its parameters already set
	 * @throws SQLException In case of a SQL-related problem
	 */
	public static PreparedStatement initPreparedStatement(@NonNull Connection connection, @NonNull final String sql, final boolean returnGeneratedKeys, Object... object) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(sql,returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);

		for(int i=0; i<object.length; i++){
			preparedStatement.setObject(i+1, object[i]);
		}
		return preparedStatement;
	}

	/**
	 * Maps a {@link ResultSet} row to a {@link User} object
	 * @param resultSet {@link ResultSet} row to map
	 * @return {@link User} object casted from the {@link ResultSet} row
	 * @throws SQLException In case of a SQL-related problem
	 */
	public static User mappingUser(@NonNull ResultSet resultSet) throws SQLException {
		User user= new User();
		user.setDbId(resultSet.getString(DB_COL_ID));
		user.setEmail(resultSet.getString(DB_COL_EMAIL));
		user.setPassword(resultSet.getString(DB_COL_PASSWORD));
		user.setCity(resultSet.getString(DB_COL_CITY));
		return user;
	}

	///region silentClose

	public static void silentClose(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING, ERROR_UNABLE_TO_CLOSE, e);
			}
		}
	}

	/**
	 * Silently close a {@link ResultSet}
	 * @param rs ResultSet to close
	 */
	public static void silentClose(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING, ERROR_UNABLE_TO_CLOSE, e);
			}
		}
	}

	/**
	 * Silently close a {@link Connection}
	 * @param conn Connection to close
	 */
	public static void silentClose(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING, ERROR_UNABLE_TO_CLOSE, e);
			}
		}
	}

	/**
	 * Silently close a {@link Statement} and a {@link Connection}
	 * @param statement Statement to close
	 * @param conn Connection to close
	 */
	public static void silentClose(Statement statement, Connection conn) {
		silentClose(statement);
		silentClose(conn);
	}

	/**
	 * Silently close a {@link ResultSet}, a {@link Statement} and a {@link Connection}
	 * @param rs ResultSet to close
	 * @param statement Statement to close
	 * @param conn Connection to close
	 */
	public static void silentClose(ResultSet rs, Statement statement, Connection conn) {
		silentClose(rs);
		silentClose(statement, conn);
	}
	///endregion
}
