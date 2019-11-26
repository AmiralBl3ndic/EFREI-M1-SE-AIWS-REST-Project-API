package efrei.m1.aiws.dao;

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
	static final String DB_COL_VG_RATING = "VG_RATING";
	///endregion

	///region SQL Queries
	private static final String SQL_SELECT_ALL = "SELECT * FROM VIDEOGAMES";
	private static final String SQL_SELECT_BY_ID_VG = "SELECT * FROM VIDEOGAMES WHERE ID_VIDEO_GAME = ?";
	private static final String SQL_SELECT_BY_ID_USER = "SELECT * FROM VIDEOGAMES WHERE ID_USERS = ?";
	private static final String SQL_SELECT_BY_NAME = "SELECT * FROM VIDEOGAMES WHERE NAME = ?";
	private static final String SQL_SELECT_BY_TYPE = "SELECT * FROM VIDEOGAMES WHERE TYPE = ?";
	private static final String SQL_SELECT_BY_EDITOR = "SELECT * FROM VIDEOGAMES WHERE VIDEO_GAME_EDITOR = ?";
	private static final String SQL_SELECT_BY_RELEASEDATE = "SELECT * FROM VIDEOGAMES WHERE RELEASEDATE = ?";
	private static final String SQL_INSERT_VIDEOGAME = "INSERT INTO VIDEOGAMES(ID_VIDEO_GAME, ID_USERS, NAME, TYPE, RESUME, VIDEO_GAME_EDITOR, RELEASEDATE, VG_RATING) VALUES (?,?,?,?,?,?,?,?)";
	private static final String SQL_UPDATE_VIDEOGAME = "UPDATE VIDEOGAMES SET ID_VIDEO_GAME = ?, ID_USERS = ?, NAME = ?, TYPE = ?, RESUME = ?, VIDEO_GAME_EDITOR = ?, RELEASEDATE = ?, VG_RATING = ? WHERE ID_VIDEO_GAME = ?";
	private static final String SQL_DELETE_USER = "DELETE FROM VIDEOGAMES WHERE ID_VIDEO_GAME = ?";
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
			final int rating = videoGame.getRating();

			preparedStatement = DAOUtils.initPreparedStatement(connection, SQL_INSERT_VIDEOGAME, true, videoGameID, userID, name, type, resume, editor, releaseDate, rating);
			int state = preparedStatement.executeUpdate();

			if (state == 0) {
				throw new DAOException("error in the creation of the video game ");
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
			throw new DAOException("impossible to find the video game to update :( ");
		}
		try {
			connection = this.daofactory.getConnection();
			preparedStatement = DAOUtils.initPreparedStatement(connection, SQL_UPDATE_VIDEOGAME, false, videoGame.getVideoGameId(), videoGame.getUserId(), videoGame.getName(), videoGame.getType(), videoGame.getResume(), videoGame.getEditor(), videoGame.getReleaseDate(), videoGame.getRating());

			int state = preparedStatement.executeUpdate();

			if (state == 0) {
				throw new DAOException("error in the update of the video game");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			DAOUtils.silentClose(preparedStatement, connection);
		}

	}

	@Override
	public void delete(@NonNull VideoGame videoGame) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		if (videoGame.getVideoGameId() == null)
			throw new DAOException("impossible to find the video game to delete :( ");

		try {
			connection = this.daofactory.getConnection();
			preparedStatement = DAOUtils.initPreparedStatement(connection, SQL_DELETE_USER, false, videoGame.getVideoGameId());

			int state = preparedStatement.executeUpdate();

			if (state == 0) {
				throw new DAOException("impossible to delete the video game");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			DAOUtils.silentClose(preparedStatement, connection);
		}
	}

	@Override
	public VideoGame findBy(String id) {
		List<VideoGame> candidates = null;
		try {
			candidates = this.selectBy(SQL_SELECT_BY_ID_VG, id);

			if (candidates.size() >= 1) {
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

			DAOUtils.silentClose(resultSet, preparedStatement, connection);

		} catch(Exception e) {
			throw new DAOException(e);
		}

		return videogames;
	}

	public VideoGame findAll() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		VideoGame videoGame = null;

		try {
			connection = this.daofactory.getConnection();
			preparedStatement = DAOUtils.initPreparedStatement(connection, SQL_SELECT_ALL, false);
			resultSet = preparedStatement.executeQuery();

			if(resultSet.next()) {
				videoGame = DAOUtils.mappingVideoGame(resultSet);
			}

		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			DAOUtils.silentClose(resultSet, preparedStatement, connection);
		}

		return videoGame;
	}

	public VideoGame findByUserID(String id) {
		List<VideoGame> candidates;
		try {
			candidates = this.selectBy(SQL_SELECT_BY_ID_USER, id);

			if (candidates.size() >= 1) {
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

			if (candidates.size() >= 1) {
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

			if (candidates.size() >= 1) {
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

			if (candidates.size() >= 1) {
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

			if (candidates.size() >= 1) {
				return candidates.get(0);
			}
		} catch (SQLException e) {
			logger.log(Level.WARNING,"Unable to get candidates while selecting video-games records by release date", e);
		}

		return null;
	}
}
