package efrei.m1.aiws.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import efrei.m1.aiws.dao.UserDAOImpl;
import efrei.m1.aiws.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.SecureRandom;

/**
 * Wrapping class acting as a holder for authentication-related functionalities
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticationService {
	@Setter
	private static UserDAOImpl userDAO;

	private static final int HASH_COST = 12;

	/**
	 * Hash a string using the BCrypt algorithm
	 * @param stringToHash String to hash with the BCrypt algorithm
	 * @return Hashed string with the BCrypt algorithm
	 */
	public static String hashWithBCrypt(final String stringToHash) {
		return BCrypt.with(new SecureRandom()).hashToString(HASH_COST, stringToHash.toCharArray());
	}

	/**
	 * Compare a string against a BCrypt hash to check if they match
	 * @param candidate String to verify against the hash
	 * @param hash BCrypt hash to verify against
	 * @return Whether the passed {@code candidate} matches the BCrypt {@code hash}
	 */
	public static boolean compareToBCryptHash(final String candidate, final String hash) {
		return BCrypt.verifyer().verify(candidate.toCharArray(), hash).verified;
	}

	/**
	 * Authenticate a user from database users
	 * @param email Email of the user to authenticate
	 * @param password Password of the user to authenticates
	 * @return {@link User} record corresponding to authenticated user, {@code null} if no record found or if password do not match
	 */
	public static User authenticateUser(final String email, final String password) {
		User user = userDAO.findByEmail(email);

		if (user != null) {  // Check if a user record with passed in email was found
			// Check if passwords match
			if (AuthenticationService.compareToBCryptHash(password, user.getPassword())) {
				return user;
			}
		}

		// If no user found
		return null;
	}

	/**
	 * Get the user id associated with the passed in Authorization HTTP header (JWT token)
	 * @param authorizationHeader {@code Authorization} HTTP header value
	 * @return User id associated with the passed in Authorization HTTP header (JWT token)
	 */
	public static String getUserIdFromAuthorizationHeader(String authorizationHeader) {
		final String jwtToken = JWTService.extractTokenFromHeader(authorizationHeader);
		User clientUserRecord = JWTService.getUserFromToken(jwtToken);

		// The following check should never be true
		if (clientUserRecord == null || clientUserRecord.getDbId() == null) {
			return "";
		}

		return clientUserRecord.getDbId();
	}
}
