package efrei.m1.aiws.config;

import static efrei.m1.aiws.utils.Constants.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import efrei.m1.aiws.service.AuthenticationService;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class JWTInitializer implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();

		///region JWT CDI
		Algorithm algorithm = Algorithm.HMAC256(context.getInitParameter(INIT_PARAM_JWT_SECRET));
		final String issuer = context.getInitParameter(INIT_PARAM_JWT_ISSUER);
		JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();

		AuthenticationService.setJwtAlgorithm(algorithm);
		AuthenticationService.setJwtIssuer(issuer);
		AuthenticationService.setJwtVerifier(verifier);
		///endregion
	}
}
