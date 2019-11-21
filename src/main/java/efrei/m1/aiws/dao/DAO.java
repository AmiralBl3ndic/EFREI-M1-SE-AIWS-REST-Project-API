package efrei.m1.aiws.dao;
import efrei.m1.aiws.model.User;
import lombok.NonNull;

public interface DAO<T> {

public void create(@NonNull T obj) throws DAOException;
public void update(@NonNull T obj, String dbId) throws DAOException;
public void delete(@NonNull T obj) throws DAOException;
public T findById(String dbId) throws DAOException;

}
