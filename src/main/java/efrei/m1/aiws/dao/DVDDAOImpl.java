package efrei.m1.aiws.dao;

import efrei.m1.aiws.model.DVD;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.List;
import java.util.logging.Level;

@NoArgsConstructor @AllArgsConstructor
public class DVDDAOImpl implements DAO<DVD> {

	///region Class Properties
	///region Database columns names
	static final String DB_COL_ID_DVD = "ID_DVD";
	static final String DB_COL_DVD_ID_USER = "ID_USER";
	static final String DB_COL_DVD_TITLE = "TITLE";
	static final String DB_COL_DVD_TYPE = "TYPE";
	static final String DB_COL_DVD_DESCRIPTION = "DESCRIPTION";
	static final String DB_COL_DVD_EDITOR = "EDITOR";
	static final String DB_COL_DVD_AUDIO = "AUDIO";
	static final String DB_COL_DVD_RELEASEDATE = "RELEASEDATE";
	static final String DB_COL_DVD_AGELIMIT = "AGELIMIT";
	static final String DB_COL_DVD_DURATION = "DURATION";
	///endregion

	///region SQL QUERIES
	private static final String SQL_SELECT_ALL = "SELECT * FROM DVDS";
	private static final String SQL_SELECT_BY_ID_DVD = "SELECT * FROM DVDS WHERE ID_DVD = ?";
	private static final String SQL_SELECT_BY_ID_USER = "SELECT * FROM DVDS WHERE ID_USER = ?";
	private static final String SQL_SELECT_BY_NAME = "SELECT * FROM DVDS WHERE NAME = ?";
	private static final String SQL_SELECT_BY_TYPE = "SELECT * FROM DVDS WHERE TYPE = ?";
	private static final String SQL_SELECT_BY_EDITOR = "SELECT * FROM DVDS WHERE EDITOR = ?";
	private static final String SQL_SELECT_BY_AUDIO = "SELECT * FROM DVDS WHERE AUDIO = ?";
	private static final String SQL_SELECT_BY_RELEASEDATE = "SELECT * FROM DVDS WHERE RELEASEDATE = ?";
	private static final String SQL_SELECT_AGELIMIT = "SELECT * FROM DVDS WHERE AGELIMIT = ?";
	private static final String SQL_SELECT_DURATION = "SELECT * FROM DVDS WHERE DURATION = ?";
	private static final String SQL_INSERT_DVD = "INSERT INTO DVDS(ID_DVD,ID_USER,TITLE,TYPE,DESCRIPTION,EDITOR,AUDIO,RELEASEDATE,AGELIMIT,DURATION) VALUES (?,?,?,?,?,?,?,?,?,?)";
	private static final String SQL_UPDATE_DVD = "UPDATE DVDS SET ID_DVD = ?, ID_USER = ?,TITLE = ?,TYPE = ?,DESCRIPTION = ?,EDITOR = ?,RELEASEDATE = ?,AGELIMIT = ?, DURATION = ? WHERE ID_DVD = ? ";
	private static final String SQL_DELETE_USER = "DELETE FROM DVDS WHERE ID_DVD = ?";
	///endregion

	private static final Logger logger = Logger.getLogger(DVDDAOImpl.class.getName());
	///endregion
	private DAOFactory daofactory;

	@Override
	public void create(@NonNull DVD dvd) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet autoGeneratedKeys = null;

		try {
			connection = this.daofactory.getConnection();

			final String dvdId = dvd.getDvdId();
			final String userId = dvd.getUserId();
			final String ageLimit = dvd.getAgeLimit();
			final String duration = dvd.getDuration();
			final String title = dvd.getTitle();
			final String type = dvd.getType();
			final String description = dvd.getDescription();
			final String editor = dvd.getEditor();
			final String audio = dvd.getAudio();
			final String releaseDate = dvd.getReleaseDate();


			preparedStatement = DAOUtils.initPreparedStatement(connection, SQL_INSERT_DVD, true, dvdId, userId,ageLimit, duration, title, type, description, editor, audio, releaseDate);
			int state = preparedStatement.executeUpdate();

			if (state == 0) {
				throw new DAOException("error in the creation of the dvd ");
			}

			autoGeneratedKeys = preparedStatement.getGeneratedKeys();
			if (autoGeneratedKeys.next()) {
				dvd.setDvdId(autoGeneratedKeys.getObject(1).toString());
			} else {
				logger.log(Level.INFO, "error in the creation of the dvd : unable to generate proper Id");
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "error in the creation of the dvd", e);
		} finally {
			DAOUtils.silentClose(autoGeneratedKeys, preparedStatement, connection);
		}
	}

	@Override
	public void update(@NonNull DVD dvd) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		if (dvd.getDvdId() == null) {
			throw new DAOException("impossible to find the dvd to update");
		}

		try {
			connection = this.daofactory.getConnection();
			preparedStatement = DAOUtils.initPreparedStatement(connection, SQL_UPDATE_DVD, false, dvd.getDvdId(), dvd.getUserId(), dvd.getAgeLimit(), dvd.getDuration(), dvd.getTitle(), dvd.getType(), dvd.getDescription(), dvd.getEditor(), dvd.getAudio(), dvd.getReleaseDate());

			int state = preparedStatement.executeUpdate();

			if (state == 0) {
				throw new DAOException("error in the update of the dvd");
			}
		} catch(SQLException e) {
			throw new DAOException(e);
		} finally {
			DAOUtils.silentClose(preparedStatement,connection);
		}
	}

	@Override
	public void delete(@NonNull DVD dvd) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		if (dvd.getDvdId() == null) {
			throw new DAOException("impossible to find the dvd to delete :( ");
		}
		try{
			connection = this.daofactory.getConnection();
			preparedStatement = DAOUtils.initPreparedStatement(connection, SQL_DELETE_USER, false, dvd.getDvdId());

			int state = preparedStatement.executeUpdate();

			if (state == 0) {
				throw new DAOException("impossible to delete the dvd");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			DAOUtils.silentClose(preparedStatement, connection);
		}
	}

	@Override
	public DVD findBy(String id) {
		List<DVD> candidates;
		candidates = this.selectBy(SQL_SELECT_BY_ID_DVD, id);

		if (!candidates.isEmpty()) {
			return candidates.get(0);
		}

		return null;
	}

	private List<DVD> selectBy(String sqlQuerySelector, String value) {
		List<DVD> dvd = new ArrayList<>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = this.daofactory.getConnection();
			preparedStatement = DAOUtils.initPreparedStatement(connection, sqlQuerySelector, false, value);
			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {
				dvd.add(DAOUtils.mappingDVD(resultSet));
			}
		} catch(Exception e) {
			throw new DAOException(e);
		} finally {
			DAOUtils.silentClose(resultSet, preparedStatement, connection);
		}

		return dvd;
	}

	public DVD findAll() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		DVD dvd = null;

		try {
			connection = this.daofactory.getConnection();
			preparedStatement = DAOUtils.initPreparedStatement(connection, SQL_SELECT_ALL, false);
			resultSet = preparedStatement.executeQuery();

			if(resultSet.next()) {
				dvd = DAOUtils.mappingDVD(resultSet);
			}

		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			DAOUtils.silentClose(resultSet, preparedStatement, connection);
		}

		return dvd;
	}

	public DVD findByUserID(String id) {
		List<DVD> candidates;
		candidates = this.selectBy(SQL_SELECT_BY_ID_USER, id);

		if (!candidates.isEmpty()) {
			return candidates.get(0);
		}

		return null;
	}

	public DVD findByName(String id) {
		List<DVD> candidates;
		candidates = this.selectBy(SQL_SELECT_BY_NAME, id);

		if (!candidates.isEmpty()) {
			return candidates.get(0);
		}

		return null;
	}

	public DVD findByType(String id) {
		List<DVD> candidates;
		candidates = this.selectBy(SQL_SELECT_BY_TYPE, id);

		if (!candidates.isEmpty()) {
			return candidates.get(0);
		}

		return null;
	}

	public DVD findByEditor(String id) {
		List<DVD> candidates;
		candidates = this.selectBy(SQL_SELECT_BY_EDITOR, id);

		if (!candidates.isEmpty()) {
			return candidates.get(0);
		}

		return null;
	}

	public DVD findByAudio(String id) {
		List<DVD> candidates;
		candidates = this.selectBy(SQL_SELECT_BY_AUDIO, id);

		if (!candidates.isEmpty()) {
			return candidates.get(0);
		}

		return null;
	}

	public DVD findByReleaseDate(String id) {
		List<DVD> candidates;
		candidates = this.selectBy(SQL_SELECT_BY_RELEASEDATE, id);

		if (!candidates.isEmpty()) {
			return candidates.get(0);
		}

		return null;
	}

	public DVD findByAgeLimit(String id) {
		List<DVD> candidates;
		candidates = this.selectBy(SQL_SELECT_AGELIMIT, id);

		if (!candidates.isEmpty()) {
			return candidates.get(0);
		}

		return null;
	}

	public DVD findByDuration(String id) {
		List<DVD> candidates;
		candidates = this.selectBy(SQL_SELECT_DURATION, id);

		if (!candidates.isEmpty()) {
			return candidates.get(0);
		}

		return null;
	}
}
