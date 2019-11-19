package efrei.m1.aiws.config;

import static efrei.m1.aiws.utils.Constants.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import efrei.m1.aiws.service.JWTService;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class JWTInitializer implements ServletContextListener {

	private Algorithm algorithm;

	private String issuer;

	private JWTVerifier verifier;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();

		///region JWT CDI
		this.algorithm = Algorithm.HMAC256(context.getInitParameter(INIT_PARAM_JWT_SECRET));
		this.issuer = context.getInitParameter(INIT_PARAM_JWT_ISSUER);
		this.verifier = JWT.require(algorithm).withIssuer(issuer).build();

		JWTService.setJwtAlgorithm(this.algorithm);
		JWTService.setJwtIssuer(this.issuer);
		JWTService.setJwtVerifier(this.verifier);
		///endregion
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {

	}
}
