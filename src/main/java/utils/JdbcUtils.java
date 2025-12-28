package utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class JdbcUtils {

    private JdbcUtils() {
    }

    public static void setNullableInt(PreparedStatement stmt, int index, Integer value) throws SQLException {
        if (value != null) stmt.setInt(index, value);
        else stmt.setNull(index, java.sql.Types.INTEGER);
    }

    public static void setNullableString(PreparedStatement stmt, int index, String value) throws SQLException {
        if (value != null) stmt.setString(index, value);
        else stmt.setNull(index, java.sql.Types.VARCHAR);
    }

    public static void setNullableTimestamp(PreparedStatement stmt, int index, LocalDateTime value) throws SQLException {
        if (value != null) stmt.setTimestamp(index, Timestamp.valueOf(value));
        else stmt.setNull(index, java.sql.Types.TIMESTAMP);
    }
}
