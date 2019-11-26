package efrei.m1.aiws.dao;
import lombok.NonNull;

import java.awt.print.Book;
import java.util.logging.Logger;

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
	///endregion

	private static final Logger logger = Logger.getLogger(BookDAOImpl.class.getName());
	///endregion

	private DAOFactory daoFactory;

	@Override
	public void create(@NonNull Book obj)
	{

	}

	@Override
	public void update(@NonNull Book obj) {

	}

	@Override
	public void delete(@NonNull Book obj) {

	}

	@Override
	public Book findBy(String db) {
		return null;
	}
}
