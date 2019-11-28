package efrei.m1.aiws.dao;

import efrei.m1.aiws.model.*;
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
import static efrei.m1.aiws.dao.BookDAOImpl.*;
import static efrei.m1.aiws.dao.VideoGameDAOImpl.*;
import static efrei.m1.aiws.dao.DVDDAOImpl.*;

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

	public static VideoGame mappingVideoGame(@NonNull ResultSet resultSet) throws  SQLException{
		VideoGame videoGame = new VideoGame();
		videoGame.setVideoGameId(resultSet.getString(DB_COL_ID_VG));
		videoGame.setVideoGameId(resultSet.getString(DB_COL_ID_USER));
		videoGame.setVideoGameId(resultSet.getString(DB_COL_NAME));
		videoGame.setVideoGameId(resultSet.getString(DB_COL_TYPE));
		videoGame.setVideoGameId(resultSet.getString(DB_COL_RESUME));
		videoGame.setVideoGameId(resultSet.getString(DB_COL_EDITOR));
		videoGame.setVideoGameId(resultSet.getString(DB_COL_RELEASEDATE));
		return videoGame;
	}

	public static Book mappingBook(@NonNull ResultSet resultSet) throws  SQLException {
		Book book = new Book();
		book.setBookId(resultSet.getString(DB_COL_BOOK_ID));
		book.setUserId(resultSet.getString(DB_COL_BOOK_USER_ID));
		book.setAuthor(resultSet.getString(DB_COL_BOOK_AUTHOR));
		book.setTitle(resultSet.getString(DB_COL_BOOK_TITLE));
		book.setType(resultSet.getString(DB_COL_BOOK_TYPE));
		book.setDescription(resultSet.getString(DB_COL_BOOK_DESCRIPTION));
		book.setReleaseDate(resultSet.getString(DB_COL_BOOK_RELEASEDATE));
		book.setEditor(resultSet.getString(DB_COL_BOOK_EDITOR));
		book.setAgeLimit(Integer.parseInt(resultSet.getString(DB_COL_BOOK_AGELIMIT)));
		return book;
	}

	public static DVD mappingDVD(@NonNull ResultSet resultSet) throws SQLException {
		DVD dvd = new DVD();
		dvd.setDvdId(resultSet.getString(DB_COL_ID_DVD));
		dvd.setUserId(resultSet.getString(DB_COL_DVD_ID_USER));
		dvd.setTitle(resultSet.getString(DB_COL_DVD_TITLE));
		dvd.setType(resultSet.getString(DB_COL_DVD_TYPE));
		dvd.setDescription(resultSet.getString(DB_COL_DVD_DESCRIPTION));
		dvd.setEditor(resultSet.getString(DB_COL_DVD_EDITOR));
		dvd.setAudio(resultSet.getString(DB_COL_DVD_AUDIO));
		dvd.setReleaseDate(resultSet.getString(DB_COL_DVD_RELEASEDATE));
		dvd.setAgeLimit(resultSet.getString(DB_COL_DVD_AGELIMIT));
		dvd.setDuration(resultSet.getString(DB_COL_DVD_DURATION));
		return dvd;
	}

	public static Comment mappingCommentVideoGames(@NonNull ResultSet resultSet) throws SQLException {
		Comment comment= new Comment();
		comment.setCreatorId(resultSet.getString("ID_COMMENTER_VG"));
		comment.setResourceId(resultSet.getString("ID_VG_COMMENTED"));
		comment.setContent(resultSet.getString("COMMENT_CONTENT"));
		//@TODO
		// Mapp au bon type d'objet.
		return comment;
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
