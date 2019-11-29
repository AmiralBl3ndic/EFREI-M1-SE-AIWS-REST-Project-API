package efrei.m1.aiws.dao;

import lombok.NonNull;

public interface DAO<T> {
	/**
	 * Creates an instance of passed object in the database
	 * @param obj Object to store in the database
	 */
	void create(@NonNull T obj);

	/**
	 * Updates an instance of the passed object in the database
	 * @param obj Object to update in the database
	 */
	void update(@NonNull T obj);

	/**
	 * Deletes an instance of the passed object in the database
	 * @param obj Object to delete from the database
	 */
	void delete(@NonNull T obj);

	/**
	 * Find an instance of the object in the database by its id
	 * @param db Database ID of the object to look for
	 * @return {@code null} if no object found, Instance of object if found
	 */
	T findBy(String db);
}
