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

	///region VideoGames
	public static final String VIDEOGAMES_ERROR_NOT_FOUND = "Video-game not found";
	public static final String VIDEOGAMES_ERROR_NOT_CREATED = "Unable to create video-game";
	public static final String VIDEOGAMES_ERROR_NO_CLIENT_ACCOUNT = "Unable to find a valid account to create this resource";
	public static final String VIDEOGAMES_ERROR_FORBIDDEN = "You cannot update or delete a record you haven't created";
	public static final String VIDEOGAMES_ILLEGAL_FILTER_VALUE = "Please specify a valid value for URL parameters";
	public static final String VIDEOGAMES_ILLEGAL_FILTER_TYPE_INT = "Please specify a valid integer for URL parameter";
	///endregion
	///endregion
}
