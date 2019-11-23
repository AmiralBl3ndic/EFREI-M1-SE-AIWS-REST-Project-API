package efrei.m1.aiws.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;

/**
 * Wrapping class acting as a holder for authentication-related functionalities
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticationService {
	private static final int HASH_COST = 12;

	/**
	 * Hash a string using the BCrypt algorithm
	 * @param stringToHash String to hash with the BCrypt algorithm
	 * @return Hashed string with the BCrypt algorithm
	 */
	public static String hashWithBCrypt(final String stringToHash) {
		return BCrypt.with(new SecureRandom()).hashToString(HASH_COST, stringToHash.toCharArray());
	}
}
