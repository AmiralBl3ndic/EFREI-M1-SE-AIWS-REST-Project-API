package efrei.m1.aiws.dao;

import efrei.m1.aiws.model.User;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NegativeOrZero;
import java.sql.*;

import static efrei.m1.aiws.dao.UserDAOImpl.*;

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
    public static User mappingUser(@NonNull ResultSet resultSet) throws SQLException {
        User user= new User();
        user.setDbId(resultSet.getString(DB_COL_ID));
        user.setEmail(resultSet.getString(DB_COL_EMAIL));
        user.setPassword(resultSet.getString(DB_COL_PASSWORD));
        return user;
    }

    //Closing
    public static void silentClose(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException ignore) {}
        }
    }

    /**
     * Silently close a {@link ResultSet}
     * @param rs ResultSet to close
     */
    public static void silentClose(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ignore) {}
        }
    }

    /**
     * Silently close a {@link Connection}
     * @param conn Connection to close
     */
    public static void silentClose(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignore) {}
        }
    }

    /**
     * Silently close a {@link Statement} and a {@link Connection}
     * @param statement Statement to close
     * @param conn Connection to close
     */
    public static void silentClose(Statement statement, Connection conn) {
        silentClose(statement);
        silentClose(conn);
    }

    /**
     * Silently close a {@link ResultSet}, a {@link Statement} and a {@link Connection}
     * @param rs ResultSet to close
     * @param statement Statement to close
     * @param conn Connection to close
     */
    public static void silentClose(ResultSet rs, Statement statement, Connection conn) {
        silentClose(rs);
        silentClose(statement, conn);
    }
    ///endregion

}
