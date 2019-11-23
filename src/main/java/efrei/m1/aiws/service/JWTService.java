package efrei.m1.aiws.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.SecureRandom;

/**
 * Service handling all operations related to JSON Web Tokens (creation, validation, decoding, ...)s
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JWTService {
	///region JWT Context Dependency Injection
	@Setter
	private static Algorithm jwtAlgorithm;

	@Setter
	private static String jwtIssuer;

	@Setter
	private static JWTVerifier jwtVerifier;
	///endregion

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


	/**
	 * Verifies whether a {@link JWT} token is legit (hashed with same secret as app)
	 * @param token String to be considered as a {@link JWT} token
	 * @return Whether the passed string is a legit token
	 */
	public static boolean isTokenLegit(String token) {
		try {
			JWTService.jwtVerifier.verify(token);
			return true;
		} catch (Exception e) {  // If any exception occurs, the token is considered as not legit
			return false;
		}
	}

	/**
	 * Securely generates a random alphanumerics string of random length to serve as a secret
	 * @return String of random alphanumeric characters of length between 20 and 64 (both inclusive)
	 */
	public static String generateRandomSecret() {
		final int secretLength = new SecureRandom().nextInt(45) + 20;
		final String alphanumerics = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < secretLength; i++) {
			sb.append(alphanumerics.charAt(new SecureRandom().nextInt(alphanumerics.length())));
		}

		return sb.toString();
	}
}
