package efrei.m1.aiws.config;

import static efrei.m1.aiws.utils.Constants.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import efrei.m1.aiws.service.JWTService;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Random;

public class JWTInitializer implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();

		// Generate app secret with random length (minimum 20 characters) of alphanumeric characters
		final int secretLength = new Random().nextInt(45) + 20;
		final String alphanumerics = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < secretLength; i++) {
			sb.append(alphanumerics.charAt(new Random().nextInt(alphanumerics.length())));
		}

		///region JWT CDI
		Algorithm algorithm = Algorithm.HMAC256(sb.toString());
		String issuer = context.getInitParameter(INIT_PARAM_JWT_ISSUER);
		JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();

		JWTService.setJwtAlgorithm(algorithm);
		JWTService.setJwtIssuer(issuer);
		JWTService.setJwtVerifier(verifier);
		///endregion
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		// This method is empty because there is no need for the JWTInitializer class to delete any of the resources it manages
	}
}
