package efrei.m1.aiws.config;

import efrei.m1.aiws.rest.DVDsResource;
import efrei.m1.aiws.dao.*;
import efrei.m1.aiws.rest.BooksResource;
import efrei.m1.aiws.rest.UsersResource;
import efrei.m1.aiws.rest.VideoGamesResource;
import efrei.m1.aiws.service.AuthenticationService;
import efrei.m1.aiws.service.JWTService;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static efrei.m1.aiws.utils.Constants.INIT_PARAM_DB_PROPERTIES;

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
			InputStream dbPropsStream = context.getResourceAsStream(context.getInitParameter(INIT_PARAM_DB_PROPERTIES));
			dbProperties.load(dbPropsStream);
		} catch (IOException e) {
			throw new DAOConfigurationException("Unable to load WEB-INF/db.properties file as a Properties object", e);
		}

		final DAOFactory daoFactory = DAOFactory.getInstance(dbProperties);

		// Context Dependency Injection for AuthenticationService
		AuthenticationService.setUserDAO((UserDAOImpl) daoFactory.getUserDao());

		// Context Dependency Injection for JWTService
		JWTService.setUserDAO(daoFactory.getUserDao());

		// Context Dependency Injection for UsersResource
		UsersResource.setUserDAO((UserDAOImpl) daoFactory.getUserDao());

		// Context Dependency Injection for VideoGamesResource
		VideoGamesResource.setVideoGameDAO((VideoGameDAOImpl) daoFactory.getVideoGameDao());
		VideoGamesResource.setUserDAO((UserDAOImpl) daoFactory.getUserDao());

		// Context Dependency Injection for DVDsResource
		DVDsResource.setDvdDAO((DVDDAOImpl) daoFactory.getDVDDao());
		DVDsResource.setUserDAO((UserDAOImpl) daoFactory.getUserDao());


		BooksResource.setUserDAO((UserDAOImpl) daoFactory.getUserDao());
		BooksResource.setBookDAO((BookDAOImpl) daoFactory.getBookDao());
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		// There is no need for resource freeing, hence this method is empty
	}
}
