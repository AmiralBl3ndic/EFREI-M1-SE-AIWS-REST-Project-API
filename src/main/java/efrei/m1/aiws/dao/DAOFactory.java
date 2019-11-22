package efrei.m1.aiws.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Wrapper class for DAO generation-related operations
 */
public class DAOFactory {

	private static final Logger logger = Logger.getLogger(DAOFactory.class.getName());

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
		logger.log(Level.INFO, "Datasource created");
	}

	/**
	 * Safely build and instance of {@link DAOFactory}
	 * @param dbProperties {@link Properties} of the database to connect to
	 * @return Instance of {@link DAOFactory}
	 * @throws DAOConfigurationException In case of any configuration problem (JDBC, Runtime...)
	 */
	public static DAOFactory getInstance(Properties dbProperties) {
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

		logger.log(Level.INFO, "Database driver registered");

		HikariConfig hkConfig = new HikariConfig();

		// Mandatory configuration
		hkConfig.setJdbcUrl(jdbcUrl);
		hkConfig.setUsername(dbUsername);
		hkConfig.setPassword(dbPassword);

		// Optimization configuration
		hkConfig.addDataSourceProperty("cachePrepStmts", "true");
		hkConfig.addDataSourceProperty("prepStmtCacheSize", "250");
		hkConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

		logger.log(Level.INFO, "HikariCP configuration done, creating datasource");

		return new DAOFactory(hkConfig);
	}
}
