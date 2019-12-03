package efrei.m1.aiws.dao;

import efrei.m1.aiws.model.Comment;
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
	//private static final String SQL_SELECT_RATING = "SELECT * FROM DVDS WHERE RATING = ?";
	private static final String SQL_INSERT_DVD = "INSERT INTO DVDS(ID_USER,AGELIMIT,DURATION,TITLE,TYPE,DESCRIPTION,EDITOR,AUDIO,RELEASEDATE) VALUES (?,?,?,?,?,?,?,?,?)";
	private static final String SQL_UPDATE_DVD = "UPDATE DVDS SET ID_USER = ?,AGELIMIT = ?, DURATION = ?,TITLE = ?,TYPE = ?,DESCRIPTION = ?,EDITOR = ?,AUDIO = ?, RELEASEDATE = ? WHERE ID_DVD = ?";
	private static final String SQL_DELETE_USER = "DELETE FROM DVDS WHERE ID_DVD = ?";

	private static final String SQL_SELECT_COMMENTS = "SELECT COMMENT_CONTENT FROM DVDS v INNER JOIN DVD_COMMENTS c on v.ID_DVD= c.ID_DVD_COMMENTED WHERE ID_DVD=?";
	private static final String SQL_SELECT_COMMENT_BY_ID = "SELECT * FROM DVD_COMMENTS WHERE ID_DVD_COMMENTED = ? AND COMMENT_ID = ?";
	private static final String SQL_UPDATE_COMMENT = "UPDATE DVD_COMMENTS(ID_DVD_COMMENT = ?, ID_COMMENTER_DVD = ?, COMMENT_COMMENT = ?";
	private static final String SQL_INSERT_COMMENT = "INSERT INTO DVD_COMMENTS(ID_DVD_COMMENTED, ID_COMMENTER_DVD, COMMENT_CONTENT) VALUES (?, ?, ?)";
	private static final String SQL_DELETE_COMMENT = "DELETE FROM DVD_COMMENTS WHERE COMMENT_ID = ?";
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

			final String userId = dvd.getUserId();
			final String ageLimit = dvd.getAgeLimit();
			final String duration = dvd.getDuration();
			final String title = dvd.getTitle();
			final String type = dvd.getType();
			final String description = dvd.getDescription();
			final String editor = dvd.getEditor();
			final String audio = dvd.getAudio();
			final String releaseDate = dvd.getReleaseDate();

			preparedStatement = DAOUtils.initPreparedStatement(connection, SQL_INSERT_DVD, true, userId,ageLimit, duration, title, type, description, editor, audio, releaseDate);
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
			preparedStatement = DAOUtils.initPreparedStatement(connection, SQL_UPDATE_DVD, false, dvd.getUserId(), dvd.getAgeLimit(), dvd.getDuration(), dvd.getTitle(), dvd.getType(), dvd.getDescription(), dvd.getEditor(), dvd.getAudio(), dvd.getReleaseDate(),dvd.getDvdId());

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
		List<DVD> dvds = new ArrayList<>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = this.daofactory.getConnection();
			preparedStatement = DAOUtils.initPreparedStatement(connection, sqlQuerySelector, false, value);
			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {
				dvds.add(DAOUtils.mappingDVD(resultSet));
			}
		} catch(Exception e) {
			throw new DAOException(e);
		} finally {
			DAOUtils.silentClose(resultSet, preparedStatement, connection);
		}

		return dvds;
	}

	public List<DVD> findAll() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<DVD> dvd = new ArrayList<>();

		try {
			connection = this.daofactory.getConnection();
			preparedStatement = DAOUtils.initPreparedStatement(connection, SQL_SELECT_ALL, false);
			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {
				dvd.add(DAOUtils.mappingDVD(resultSet));
			}

		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			DAOUtils.silentClose(resultSet, preparedStatement, connection);
		}

		return dvd;
	}

	public List<DVD> findByUserID(String id) {
		return this.selectBy(SQL_SELECT_BY_ID_USER, id);
	}

	public List<DVD> findByName(String id) {
		return this.selectBy(SQL_SELECT_BY_NAME, id);
	}

	/**
	 * Creates a database record of the passed in {@link DVD} {@link Comment}
	 * @param comment {@link DVD} comment with all its fields set to non-null values
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
				logger.log(Level.WARNING, "Error: cannot create DVD record");
				return;
			}

			autoGeneratedKeys = preparedStatement.getGeneratedKeys();
			if (autoGeneratedKeys.next()) {
				comment.setDbId(autoGeneratedKeys.getObject(1).toString());
			} else {
				logger.log(Level.INFO, "Error in the creation of the DVD comment : unable to generate proper Id");
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "Error in the creation of the DVD comment", e);
		} finally {
			DAOUtils.silentClose(autoGeneratedKeys, preparedStatement, connection);
		}
	}

	public void updateComment(Comment comment) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		if (comment.getDbId() == null) {
			logger.log(Level.WARNING, "Error: unable to find DVD comment to update!");
		}

		try {
			connection = this.daofactory.getConnection();
			preparedStatement = DAOUtils.initPreparedStatement(connection, SQL_UPDATE_COMMENT, false, comment.getDbId(), comment.getContent(), comment.getCreatorId(), comment.getResourceId());

			int state = preparedStatement.executeUpdate();

			if(state == 0) {
				logger.log(Level.WARNING, "Error : unable to update DVD comment!");
			}
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Error while update DVD comment record", e);
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
			logger.log(Level.WARNING, "Unable to find DVD comment to delete");
			return false;
		}

		try {
			connection = this.daofactory.getConnection();
			preparedStatement = DAOUtils.initPreparedStatement(connection, SQL_DELETE_COMMENT, false, comment.getDbId());

			int state = preparedStatement.executeUpdate();

			if (state == 0) {
				logger.log(Level.WARNING, "Unable to delete DVD comment record");
			} else {
				deletionSucceeded = true;
			}
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Error while deleting DVD comment", e);
		} finally {
			DAOUtils.silentClose(preparedStatement, connection);
		}

		return deletionSucceeded;
	}

	/**
	 * Select a single comment of a specified DVD
	 * @param dvdId ID of the {@link DVD} to look comments for
	 * @param commentId Id of the {@link Comment} to look for
	 * @return {@link Comment} database record if the request succeeded, {@code null} in any other case
	 */
	public Comment selectCommentById(String dvdId, String commentId) {
		Comment comment = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null ;
		ResultSet resultSet = null;

		try {
			connection = this.daofactory.getConnection();
			preparedStatement = DAOUtils.initPreparedStatement(connection, SQL_SELECT_COMMENT_BY_ID,false, dvdId, commentId);
			resultSet=preparedStatement.executeQuery();

			if (resultSet.next()) {
				comment = DAOUtils.mappingCommentDVD(resultSet);
			}
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Unable to get comment #" + commentId + " of DVD #" + dvdId, e);
		} finally {
			DAOUtils.silentClose(resultSet, preparedStatement, connection);
		}

		return comment;
	}

	/**
	 * Function to list all the comments made on a given DVD
	 * @param idDVD ID of the DVD database record to look the comments for
	 * @return List of all {@link Comment}s related to DVD record with id {@code idDVD}
	 */
	public List<Comment> selectAllComments(String idDVD) {
		List <Comment> comments = new ArrayList<>();
		Connection connection = null;
		PreparedStatement preparedStatement = null ;
		ResultSet resultSet = null;

		try {
			connection= this.daofactory.getConnection();
			preparedStatement=DAOUtils.initPreparedStatement(connection,SQL_SELECT_COMMENTS,false,idDVD);
			resultSet=preparedStatement.executeQuery();

			while(resultSet.next()) {
				comments.add(DAOUtils.mappingCommentDVD(resultSet));
			}
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Unable to get comments of DVD", e);
		}
		finally {
			DAOUtils.silentClose(resultSet, preparedStatement, connection);
		}

		return comments;
	}


}
