package user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface CreateRowMapper<T> {
    T rowMapper(ResultSet rs) throws SQLException;
}
