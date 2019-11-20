package efrei.m1.aiws.service;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWT;

import com.auth0.jwt.exceptions.JWTCreationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JWTService {
	@Setter
	private static Algorithm jwtAlgorithm;

	@Setter
	private static String jwtIssuer;

	@Setter
	private static JWTVerifier jwtVerifier;

	///region JWT Claims
	private static final String JWT_CLAIM_EMAIL = "email";
	///endregion

	/**
	 * Create a valid {@link JWT} token issued by the application
	 * @param email Email of the user to use
	 * @return {@link JWT} token with the email in the payload, empty string in case of an error
	 */
	public static String createToken(String email) {
		try {
			return JWT.create()
				.withIssuer(jwtIssuer)
				.withClaim(JWT_CLAIM_EMAIL, email)
				.sign(jwtAlgorithm);
		} catch (JWTCreationException e) {
			return "";
		}
	}
}
