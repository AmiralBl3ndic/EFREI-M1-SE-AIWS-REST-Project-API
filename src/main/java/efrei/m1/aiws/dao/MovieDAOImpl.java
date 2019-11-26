package efrei.m1.aiws.dao;

import efrei.m1.aiws.model.DVD;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.SQLException;
import java.util.logging.Logger;

@NoArgsConstructor @AllArgsConstructor
public class MovieDAOImpl implements DAO<DVD> {

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
	static final String DB_COL_DVD_RATING = "RATIONG";
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
	private static final String SQL_SELECT_RATING = "SELECT * FROM DVDS WHERE RATING = ?";
	///endregion

	private static final Logger logger = Logger.getLogger(VideoGameDAOImpl.class.getName());
	///endregion
	private DAOFactory daofactory;

	@Override
	public void create(@NonNull DVD obj) {

	}

	@Override
	public void update(@NonNull DVD obj) {

	}

	@Override
	public void delete(@NonNull DVD obj) {

	}

	@Override
	public DVD findBy(String db) throws SQLException {
		return null;
	}
}
