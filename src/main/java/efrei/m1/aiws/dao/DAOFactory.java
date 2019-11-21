package efrei.m1.aiws.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import efrei.m1.aiws.model.User;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Wrapper class for DAO generation-related operations
 */
public class DAOFactory {
	///region db.properties keys
	private static final String PROP_JDBC_DRIVER = "jdbcDriver";
	private static final String PROP_JDBC_URL = "jdbcUrl";
	private static final String PROP_DB_USERNAME = "dbUser";
	private static final String PROP_DB_PASSWORD = "dbPassword";
	///endregion

	/**
	 * Connection Pooling data source
	 */
	private HikariDataSource dataSource;

	private DAOFactory(HikariConfig config) {
		this.dataSource = new HikariDataSource(config);
	}

	/**
	 * Safely build and instance of {@link DAOFactory}
	 * @param dbProperties {@link Properties} of the database to connect to
	 * @return Instance of {@link DAOFactory}
	 * @throws DAOConfigurationException In case of any configuration problem (JDBC, Runtime...)
	 */
	public static DAOFactory getInstance(Properties dbProperties) throws DAOConfigurationException {
		// Read database configuration from .properties file
		final String jdbcDriver = dbProperties.getProperty(PROP_JDBC_DRIVER);
		final String jdbcUrl = dbProperties.getProperty(PROP_JDBC_URL);
		final String dbUsername = dbProperties.getProperty(PROP_DB_USERNAME);
		final String dbPassword = dbProperties.getProperty(PROP_DB_PASSWORD);

		try {
			Class.forName(jdbcDriver);
		} catch (ClassNotFoundException e) {
			throw new DAOConfigurationException("Cannot find class " + jdbcDriver, e);
		}

		HikariConfig hkConfig = new HikariConfig();

		// Mandatory configuration
		hkConfig.setJdbcUrl(jdbcUrl);
		hkConfig.setUsername(dbUsername);
		hkConfig.setPassword(dbPassword);

		// Optimization configuration
		hkConfig.addDataSourceProperty("cachePrepStmts", "true");
		hkConfig.addDataSourceProperty("prepStmtCacheSize", "250");
		hkConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

		return new DAOFactory(hkConfig);
	}

	public Connection getConnection() throws SQLException {
		return this.dataSource.getConnection();
	}


	public DAO<User> getUserDao(){
		return new UserDAOImpl(this);
	}

	private static Properties loadDatabaseProperties(@NonNull InputStream propertiesFile) throws DAOConfigurationException {
		Properties dbProperties = new Properties();

		try {
			dbProperties.load(propertiesFile);
		} catch (IOException e) {
			throw new DAOConfigurationException("Unable to load database properties file", e);
		}

		return dbProperties;
	}



}
