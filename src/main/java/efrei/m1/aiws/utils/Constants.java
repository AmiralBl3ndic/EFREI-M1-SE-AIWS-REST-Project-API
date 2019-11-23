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
	///endregion

	///region Users
	public static final String USERS_ERROR_EMPTY_FIELDS = "Unable to perform request: fields 'email', 'password' and 'city' must be set and not empty";
	///endregion
	///endregion
}
