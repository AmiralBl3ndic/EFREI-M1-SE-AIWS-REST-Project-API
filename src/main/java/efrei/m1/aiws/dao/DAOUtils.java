package efrei.m1.aiws.dao;

import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NegativeOrZero;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@NoArgsConstructor
public class DAOUtils {


    /**
     *
     * @param connection
     * @param sql
     * @param returnGeneratedKeys
     * @param object
     * @return
     * @throws SQLException
     */
    public static PreparedStatement initPreparedStatement(@NonNull Connection connection, @NonNull final String sql, final boolean returnGeneratedKeys, Object... object) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql,returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);

        for(int i=0; i<object.length; i++){
            preparedStatement.setObject(i+1, object[i]);
        }
        return preparedStatement;
    }
}
