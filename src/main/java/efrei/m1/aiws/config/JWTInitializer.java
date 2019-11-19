package efrei.m1.aiws.config;

import static efrei.m1.aiws.utils.Constants.*;

import com.auth0.jwt.algorithms.Algorithm;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class JWTInitializer implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();

		context.setAttribute(CDI_JWT_ISSUER, context.getInitParameter(INIT_PARAM_JWT_ISSUER));

		Algorithm algorithm = Algorithm.HMAC256(context.getInitParameter(INIT_PARAM_JWT_SECRET));
		context.setAttribute(CDI_JWT_ALGORITHM, algorithm);
	}
}
