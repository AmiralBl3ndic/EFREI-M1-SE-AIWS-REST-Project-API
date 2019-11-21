package efrei.m1.aiws.dao;

/**
 * Wrapper {@link Exception} for all DAO configuration related exceptions
 */
public class DAOConfigurationException extends DAOException {
	public DAOConfigurationException(String message) {
		super(message);
	}

	public DAOConfigurationException(Throwable cause) {
		super(cause);
	}

	public DAOConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}
}