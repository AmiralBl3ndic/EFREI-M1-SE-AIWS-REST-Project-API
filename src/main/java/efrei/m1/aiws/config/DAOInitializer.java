package efrei.m1.aiws.config;

import static efrei.m1.aiws.utils.Constants.*;

import efrei.m1.aiws.dao.DAOConfigurationException;
import efrei.m1.aiws.dao.DAOFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class responsible for DAO initialization at server startup
 */
public class DAOInitializer implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();

		Properties dbProperties = new Properties();

		// Attempt to load the db.properties file as a Properties object
		try {
			InputStream dbPropsStream = context.getResourceAsStream(INIT_PARAM_DB_PROPERTIES);
			dbProperties.load(dbPropsStream);
		} catch (IOException e) {
			throw new DAOConfigurationException("Unable to load WEB-INF/db.properties file as a Properties object", e);
		}

		// Instantiate a DAOFactory from the dbProperties
		try {
			DAOFactory daoFactory = DAOFactory.getInstance(dbProperties);
		} catch (DAOConfigurationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {

	}
}
