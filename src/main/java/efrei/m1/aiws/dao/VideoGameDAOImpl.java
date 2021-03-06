package efrei.m1.aiws.dao;

import efrei.m1.aiws.model.Comment;
import efrei.m1.aiws.model.VideoGame;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@NoArgsConstructor @AllArgsConstructor
public class VideoGameDAOImpl implements DAO<VideoGame> {
	///region Class Properties
	///region Database columns names
	static final String DB_COL_ID_VG = "ID_VIDEO_GAME";
	static final String DB_COL_ID_USER = "ID_USERS";
	static final String DB_COL_NAME = "NAME";
	static final String DB_COL_TYPE = "TYPE";
	static final String DB_COL_RESUME = "RESUME";
	static final String DB_COL_EDITOR = "VIDEO_GAME_EDITOR";
	static final String DB_COL_RELEASEDATE = "RELEASEDATE";
	///endregion

	///region SQL Queries
	private static final String SQL_SELECT_ALL = "SELECT * FROM VIDEOGAMES";
	private static final String SQL_SELECT_BY_ID_VG = "SELECT * FROM VIDEOGAMES WHERE ID_VIDEO_GAME = ?";
	private static final String SQL_SELECT_BY_ID_USER = "SELECT * FROM VIDEOGAMES WHERE ID_USERS = ?";
	private static final String SQL_SELECT_BY_NAME = "SELECT * FROM VIDEOGAMES WHERE NAME = ?";
	private static final String SQL_SELECT_BY_TYPE = "SELECT * FROM VIDEOGAMES WHERE TYPE = ?";
	private static final String SQL_SELECT_BY_EDITOR = "SELECT * FROM VIDEOGAMES WHERE VIDEO_GAME_EDITOR = ?";
	private static final String SQL_SELECT_BY_RELEASEDATE = "SELECT * FROM VIDEOGAMES WHERE RELEASEDATE = ?";
	private static final String SQL_INSERT_VIDEOGAME = "INSERT INTO VIDEOGAMES(ID_VIDEO_GAME, ID_USERS, NAME, TYPE, RESUME, VIDEO_GAME_EDITOR, RELEASEDATE) VALUES (?,?,?,?,?,?,?)";
	private static final String SQL_UPDATE_VIDEOGAME = "UPDATE VIDEOGAMES SET ID_USERS = ?, NAME = ?, TYPE = ?, RESUME = ?, VIDEO_GAME_EDITOR = ?, RELEASEDATE = ? WHERE ID_VIDEO_GAME = ?";
	private static final String SQL_DELETE_USER = "DELETE FROM VIDEOGAMES WHERE ID_VIDEO_GAME = ?";
	private static final String SQL_SELECT_COMMENTS = "SELECT * FROM VIDEOGAMES v INNER JOIN VG_COMMENTS c on v.ID_VIDEO_GAME= c.ID_VG_COMMENTED WHERE ID_VIDEO_GAME=?";
	private static final String SQL_SELECT_COMMENT_BY_ID = "SELECT * FROM vg_comments WHERE ID_VG_COMMENTED = ? AND COMMENT_ID = ?";
	private static final String SQL_INSERT_COMMENT = "INSERT INTO VG_COMMENTS(ID_VG_COMMENTED, ID_COMMENTER_VG, COMMENT_CONTENT) VALUES (?, ?, ?)";
	private static final String SQL_UPDATE_COMMENT = "UPDATE VG_COMMENTS(ID_VG_COMMENTED = ?, ID_COMMENTER_VG = ?, COMMENT_CONTENT = ?";
	private static final String SQL_DELETE_COMMENT = "DELETE FROM vg_comments WHERE COMMENT_ID = ?";
	///endregion

	private static final Logger logger = Logger.getLogger(VideoGameDAOImpl.class.getName());
	///endregion

	private DAOFactory daofactory;

	@Override
	public void create(@NonNull VideoGame videoGame) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet autoGeneratedKeys = null;

		try {
			connection = this.daofactory.getConnection();

			final String videoGameID = videoGame.getVideoGameId();
			final String userID = videoGame.getUserId();
			final String name = videoGame.getName();
			final String type = videoGame.getType();
			final String resume = videoGame.getResume();
			final String editor = videoGame.getEditor();
			final String releaseDate = videoGame.getReleaseDate();

			preparedStatement = DAOUtils.initPreparedStatement(connection, SQL_INSERT_VIDEOGAME, true, videoGameID, userID, name, type, resume, editor, releaseDate);
			int state = preparedStatement.executeUpdate();

			if (state == 0) {
				logger.log(Level.WARNING, "Error: cannot create video-game record");
				return;
			}

			autoGeneratedKeys = preparedStatement.getGeneratedKeys();
			if (autoGeneratedKeys.next()) {
				videoGame.setVideoGameId(autoGeneratedKeys.getObject(1).toString());
			} else {
				logger.log(Level.INFO, "error in the creation of the video game : unable to generate proper Id");
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "error in the creation of the video game", e);
		} finally {
			DAOUtils.silentClose(autoGeneratedKeys, preparedStatement, connection);
		}
	}

	@Override
	public void update(@NonNull VideoGame videoGame) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		if (videoGame.getVideoGameId() == null) {
			logger.log(Level.WARNING, "Error: unable to find video-game to update");
		}

		try {
			connection = this.daofactory.getConnection();
			preparedStatement = DAOUtils.initPreparedStatement(connection, SQL_UPDATE_VIDEOGAME, false, videoGame.getUserId(), videoGame.getName(), videoGame.getType(), videoGame.getResume(), videoGame.getEditor(), videoGame.getReleaseDate(), videoGame.getVideoGameId());

			int state = preparedStatement.executeUpdate();

			if (state == 0) {
				logger.log(Level.WARNING, "Error: unable to update video-game");
			}
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Error while updating video-game record", e);
		} finally {
			DAOUtils.silentClose(preparedStatement, connection);
		}

	}

	@Override
	public void delete(@NonNull VideoGame videoGame) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		if (videoGame.getVideoGameId() == null) {
			logger.log(Level.WARNING, "Unable to find video-game to delete");
			return;
		}

		try {
			connection = this.daofactory.getConnection();
			preparedStatement = DAOUtils.initPreparedStatement(connection, SQL_DELETE_USER, false, videoGame.getVideoGameId());

			int state = preparedStatement.executeUpdate();

			if (state == 0) {
				logger.log(Level.WARNING, "Unable to delete video-game record");
			}
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Error while deleting video-game record", e);
		} finally {
			DAOUtils.silentClose(preparedStatement, connection);
		}
	}

	@Override
	public VideoGame findBy(String id) {
		List<VideoGame> candidates;
		try {
			candidates = this.selectBy(SQL_SELECT_BY_ID_VG, id);

			if (!candidates.isEmpty()) {
				return candidates.get(0);
			}
		} catch (SQLException e) {
			logger.log(Level.WARNING,"Unable to get candidates while selecting video-games records by the video game's id", e);
		}

		return null;
	}

	private List<VideoGame> selectBy(String sqlQuerySelector, String value) throws SQLException {
		List<VideoGame> videogames = new ArrayList<>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = this.daofactory.getConnection();
			preparedStatement = DAOUtils.initPreparedStatement(connection, sqlQuerySelector, false, value);
			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {
				videogames.add(DAOUtils.mappingVideoGame(resultSet));
			}
		} catch(Exception e) {
			throw new SQLException(e);
		} finally {
			DAOUtils.silentClose(resultSet, preparedStatement, connection);
		}

		return videogames;
	}

	public List<VideoGame> findAll() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<VideoGame> videoGames = new ArrayList<>();

		try {
			connection = this.daofactory.getConnection();
			preparedStatement = DAOUtils.initPreparedStatement(connection, SQL_SELECT_ALL, false);
			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {
				videoGames.add(DAOUtils.mappingVideoGame(resultSet));
			}
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Unhandled error", e);
		} finally {
			DAOUtils.silentClose(resultSet, preparedStatement, connection);
		}

		return videoGames;
	}

	public VideoGame findByUserID(String id) {
		List<VideoGame> candidates;
		try {
			candidates = this.selectBy(SQL_SELECT_BY_ID_USER, id);

			if (!candidates.isEmpty()) {
				return candidates.get(0);
			}
		} catch (SQLException e) {
			logger.log(Level.WARNING,"Unable to get candidates while selecting video-games records by the user's id", e);
		}

		return null;
	}

	public VideoGame findByName(String id) {
		List<VideoGame> candidates;
		try {
			candidates = this.selectBy(SQL_SELECT_BY_NAME, id);

			if (!candidates.isEmpty()) {
				return candidates.get(0);
			}
		} catch (SQLException e) {
			logger.log(Level.WARNING,"Unable to get candidates while selecting video-games records by name", e);
		}

		return null;
	}

	public VideoGame findByType(String id) {
		List<VideoGame> candidates;
		try {
			candidates = this.selectBy(SQL_SELECT_BY_TYPE, id);

			if (!candidates.isEmpty()) {
				return candidates.get(0);
			}
		} catch (SQLException e) {
			logger.log(Level.WARNING,"Unable to get candidates while selecting video-games records by type", e);
		}

		return null;
	}

	public VideoGame findByEditor(String id) {
		List<VideoGame> candidates;
		try {
			candidates = this.selectBy(SQL_SELECT_BY_EDITOR, id);

			if (!candidates.isEmpty()) {
				return candidates.get(0);
			}
		} catch (SQLException e) {
			logger.log(Level.WARNING,"Unable to get candidates while selecting video-games records by editor", e);
		}

		return null;
	}

	public VideoGame findByReleaseDate(String id) {
		List<VideoGame> candidates;
		try {
			candidates = this.selectBy(SQL_SELECT_BY_RELEASEDATE, id);

			if (!candidates.isEmpty()) {
				return candidates.get(0);
			}
		} catch (SQLException e) {
			logger.log(Level.WARNING,"Unable to get candidates while selecting video-games records by release date", e);
		}

		return null;
	}

	/**
	 * Function to list all the comments made on a given Video Game
	 * @param idVideoGame ID of the video-game database record to look the comments for
	 * @return List of all {@link Comment}s related to video-game record with id {@code idVideoGame}
	 */
	public List<Comment> selectAllComments(String idVideoGame) {
		List <Comment> comments = new ArrayList<>();
		Connection connection = null;
		PreparedStatement preparedStatement = null ;
		ResultSet resultSet = null;

		try {
			connection = this.daofactory.getConnection();
			preparedStatement = DAOUtils.initPreparedStatement(connection,SQL_SELECT_COMMENTS,false,idVideoGame);
			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {
				comments.add(DAOUtils.mappingCommentVideoGames(resultSet));
			}
		} catch (SQLException e) {
            logger.log(Level.WARNING, "Unable to get comments of video-game", e);
		}
		finally {
			DAOUtils.silentClose(resultSet, preparedStatement, connection);
		}

		return comments;
	}

	/**
	 * Select a single comment of a specified video-game
	 * @param videoGameId ID of the {@link VideoGame} to look comments for
	 * @param commentId Id of the {@link Comment} to look for
	 * @return {@link Comment} database record if the request succeeded, {@code null} in any other case
	 */
	public Comment selectCommentById(String videoGameId, String commentId) {
		Comment comment = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null ;
		ResultSet resultSet = null;

		try {
			connection = this.daofactory.getConnection();
			preparedStatement = DAOUtils.initPreparedStatement(connection, SQL_SELECT_COMMENT_BY_ID,false, videoGameId, commentId);
			resultSet=preparedStatement.executeQuery();

			if (resultSet.next()) {
				comment = DAOUtils.mappingCommentVideoGames(resultSet);
			}
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Unable to get comment #" + commentId + " of video-game #" + videoGameId, e);
		} finally {
			DAOUtils.silentClose(resultSet, preparedStatement, connection);
		}

		return comment;
	}

	/**
	 * Creates a database record of the passed in {@link VideoGame} {@link Comment}
	 * @param comment {@link VideoGame} comment with all its fields set to non-null values
	 */
	public void createComment(Comment comment) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet autoGeneratedKeys = null;

		try {
			connection = this.daofactory.getConnection();

			preparedStatement = DAOUtils.initPreparedStatement(connection, SQL_INSERT_COMMENT, true, comment.getResourceId(), comment.getCreatorId(), comment.getContent());
			int state = preparedStatement.executeUpdate();

			if (state == 0) {
				logger.log(Level.WARNING, "Error: cannot create video-game record");
				return;
			}

			autoGeneratedKeys = preparedStatement.getGeneratedKeys();
			if (autoGeneratedKeys.next()) {
				comment.setDbId(autoGeneratedKeys.getObject(1).toString());
			} else {
				logger.log(Level.INFO, "Error in the creation of the video-game comment : unable to generate proper Id");
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "Error in the creation of the video-game comment", e);
		} finally {
			DAOUtils.silentClose(autoGeneratedKeys, preparedStatement, connection);
		}
	}

	public void updateComment(Comment comment) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		if (comment.getDbId() == null) {
			logger.log(Level.WARNING, "Error: unable to find VG comment to update");
		}

		try {
			connection = this.daofactory.getConnection();
			preparedStatement = DAOUtils.initPreparedStatement(connection, SQL_UPDATE_COMMENT, false, comment.getDbId(), comment.getContent(), comment.getCreatorId(), comment.getResourceId());

			int state = preparedStatement.executeUpdate();

			if(state == 0) {
				logger.log(Level.WARNING, "Error : unable to update VG comment");
			}
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Error while update VG comment record", e);
		} finally {
			DAOUtils.silentClose(preparedStatement, connection);
		}
	}


	/**
	 * Deletes the passed in {@link Comment} record from the database
	 * @param comment Comment to delete
	 * @return Whether the deletion succeeded
	 */
	public boolean deleteComment(Comment comment) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		boolean deletionSucceeded = false;

		if (comment.getDbId() == null) {
			logger.log(Level.WARNING, "Unable to find video-game comment to delete");
			return false;
		}

		try {
			connection = this.daofactory.getConnection();
			preparedStatement = DAOUtils.initPreparedStatement(connection, SQL_DELETE_COMMENT, false, comment.getDbId());

			int state = preparedStatement.executeUpdate();

			if (state == 0) {
				logger.log(Level.WARNING, "Unable to delete video-game comment record");
			} else {
				deletionSucceeded = true;
			}
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Error while deleting video-game comment", e);
		} finally {
			DAOUtils.silentClose(preparedStatement, connection);
		}

		return deletionSucceeded;
	}
}
