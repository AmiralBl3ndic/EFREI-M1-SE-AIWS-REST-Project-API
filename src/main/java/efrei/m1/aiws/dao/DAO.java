package efrei.m1.aiws.dao;
import efrei.m1.aiws.model.User;
import lombok.NonNull;

public interface DAO<T> {

public void create(@NonNull T obj) throws DAOException;
public void update(@NonNull T obj) throws DAOException;
public void delete(@NonNull T obj) throws DAOException;
public T findBy(String db) throws DAOException;

}
