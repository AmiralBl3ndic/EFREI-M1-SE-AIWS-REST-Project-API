package efrei.m1.aiws.dao;

/**
 * Wrapper {@link Exception} for all DAO related exceptions
 */
public class DAOException extends RuntimeException {
	public DAOException(String message) {
		super(message);
	}

	public DAOException(Throwable cause) {
		super(cause);
	}

	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}
}