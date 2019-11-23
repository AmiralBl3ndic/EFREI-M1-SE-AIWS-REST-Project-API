package efrei.m1.aiws.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
	///region Init parameters | Names
	public static final String INIT_PARAM_JWT_ISSUER = "jwtIssuer";
	public static final String INIT_PARAM_DB_PROPERTIES = "dbProperties";
	///endregion

	///region Error messages
	///region Authentication
	public static final String AUTH_ERROR_WRONG_CREDENTIALS = "Wrong email/password";
	public static final String AUTH_JWT_TOKEN_NEEDED = "Authentication needed: please provide a valid JWT token";
	///endregion

	///region Users
	public static final String USERS_ERROR_EMPTY_FIELDS = "Unable to perform request: fields 'email', 'password' and 'city' must be set and not empty";
	public static final String USERS_ERROR_CANNOT_CREATE = "Unable to create account with these credentials, they may already be in use";
	public static final String USERS_ERROR_CANNOT_UPDATE = "Cannot update record";
	///endregion
	///endregion
}
