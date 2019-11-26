package efrei.m1.aiws.dao;

import efrei.m1.aiws.model.Book;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@NoArgsConstructor @AllArgsConstructor
public class BookDAOImpl implements DAO<Book>
{
	///region Class Properties
	///region Database columns names
	static final String DB_COL_ID_BOOK_ID = "ID_BOOK";
	static final String DB_COL_USER_ID = "ID_USERS";
	static final String DB_COL_AUTHOR = "AUTHOR";
	static final String DB_COL_TITLE = "TITLE";
	static final String DB_COL_TYPE = "TYPE";
	static final String DB_COL_DESCRIPTION = "DESCRIPTION";
	static final String DB_COL_RELEASEDATE = "RELEASEDATE";
	static final String DB_COL_EDITOR = "EDITOR";
	static final String DB_COL_AGELIMIT = "AGELIMIT";
	static final int DB_COL_BOOK_RATING = 0;
	///endregion

	///region SQL Queries
	private static final String SQL_SELECT_ALL = "SELECT * FROM BOOKS";
	private static final String SQL_SELECT_BY_ID_BOOK = "SELECT * FROM BOOKS WHERE ID_BOOK = ?";
	private static final String SQL_SELECT_BY_ID_USER = "SELECT * FROM BOOKS WHERE ID_USERS = ?";
	private static final String SQL_SELECT_BY_AUTHOR = "SELECT * FROM BOOKS WHERE AUTHOR = ?";
	private static final String SQL_SELECT_BY_TITLE = "SELECT * FROM BOOKS WHERE TITLE = ?";
	private static final String SQL_SELECT_BY_TYPE = "SELECT * FROM BOOKS WHERE TYPE = ?";
	private static final String SQL_SELECT_BY_RELEASEDATE = "SELECT * FROM BOOKS WHERE RELEASEDATE = ?";
	private static final String SQL_SELECT_BY_EDITOR = "SELECT * FROM BOOKS WHERE EDITOR = ?";
	private static final String SQL_SELECT_BY_AGELIMIT = "SELECT * FROM BOOKS WHERE AGELIMIT = ?";
	private static final String SQL_SELECT_BY_RATING = "SELECT * FROM BOOKS WHERE RATING = ?";
	private static final String SQL_INSERT_BOOK = "INSERT INTO BOOKS(ID_BOOK,ID_USERS,AUTHOR,TITLE,TYPE,DESCRIPTION,RELEASEDATE,EDITOR,AGELIMIT,RATING) VALUES (?,?,?,?,?,?,?,?,?,?)";
	private static final String SQL_UPDATE_BOOK = "UPDATE BOOKS SET ID_BOOK = ?,ID_USERS = ?,AUTHOR = ?,TITLE = ?,TYPE = ?,DESCRIPTION = ?,RELEASEDATE = ?,EDITOR = ?,AGELIMIT = ?RATING = ?";
	private static final String SQL_DELETE_BOOK = "DELETE FROM BOOKS WHERE ID_BOOK = ?";
	///endregion

	private static final Logger logger = Logger.getLogger(BookDAOImpl.class.getName());
	///endregion

	private DAOFactory daoFactory;

	@Override
	public void create(@NonNull Book book)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet autoGeneratedKeys = null;

		try {
			final String bookID = book.getBookId();
			final String userID = book.getUserId();
			final String author = book.getAuthor();
			final String title = book.getTitle();
			final String type = book.getType();
			final String description = book.getDescription();
			final String releaseDate = book.getReleaseDate();
			final String editor = book.getEditor();
			final int rating = book.getRating();

			preparedStatement = DAOUtils.initPreparedStatement(connection, SQL_INSERT_BOOK,true,book);
			int state = preparedStatement.executeUpdate();

			if(state == 0) {
				throw new DAOException("error in the creation of the book");
			}

			autoGeneratedKeys = preparedStatement.getGeneratedKeys();
			if(autoGeneratedKeys.next()) {
				book.setBookId(autoGeneratedKeys.getObject(1).toString());
			} else {
				logger.log(Level.INFO,"error in the creation of the book : unable to generated proper ID");
			}
		} catch (SQLException e) {
			logger.log(Level.INFO, "error in the creation of the video game", e);
		} finally {
			 DAOUtils.silentClose(autoGeneratedKeys, preparedStatement, connection);
		}
	}

	@Override
	public void update(@NonNull Book book) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		if(book.getBookId() == null) {
			throw new DAOException("impossible to find the book to update");
		}try {
			connection = this.daoFactory.getConnection();
			preparedStatement = DAOUtils.initPreparedStatement(connection, SQL_UPDATE_BOOK, true, book.getBookId(), book.getUserId(),book.getAuthor(),book.getTitle(), book.getType(),book.getDescription(), book.getReleaseDate(), book.getEditor(), book.getAgeLimit());
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			DAOUtils.silentClose(preparedStatement, connection);
		}
	}

	@Override
	public void delete(@NonNull Book book)
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		if(book.getBookId() == null) {
			throw new DAOException("impossible to find the book to delete");
		} try {
			connection = this.daoFactory.getConnection();
			preparedStatement = DAOUtils.initPreparedStatement(connection,SQL_DELETE_BOOK,false,book.getBookId());

			int state = preparedStatement.executeUpdate();

			if(state == 0){
				throw new DAOException("impossible to delete the book");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
				DAOUtils.silentClose(preparedStatement, connection);
		}
	}

	@Override
	public Book findBy(String db) {
		return null;
	}
}
