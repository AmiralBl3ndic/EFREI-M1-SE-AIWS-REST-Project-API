package efrei.m1.aiws.dao;
import lombok.NonNull;

import java.awt.print.Book;

public class BookDAOImpl implements DAO<Book>
{
	static final String DB_COL_ID_BOOK_ID = "ID_BOOK";
	static final String DB_COL_USER_ID = "ID_USERS";
	static final String DB_COL_AUTHOR = "AUTHOR";
	static final String DB_COL_TITLE = "TITLE";
	static final String DB_COL_TYPE = "TYPE";
	static final String DB_COL_DESCRIPTION = "DESCRIPTION";
	static final String DB_COL_RELEASEDATE = "RELEASEDATE";
	static final String DB_COL_EDITOR = "EDITOR";
	static final String DB_COL_AGELIMIT = "";
	static final String DB_COL_BOOK_RATING = "";

	@Override
	public void create(@NonNull Book obj) {

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
