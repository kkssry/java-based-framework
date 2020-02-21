package user.dao;

import core.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    public void sendQueryWithMultiArray(String sql, Object... values) throws SQLException {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
            pstmt.executeUpdate();
        }

    }

    public void sendQuery(String sql, PreparedStatementSetter pss) throws SQLException {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pss.setValues(pstmt);
            pstmt.executeUpdate();
        }
    }

    public <T> List<T> findAll(String sql, PreparedStatementSetter pss, CreateRowMapper rm) throws SQLException {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pss.setValues(pstmt);
            try (ResultSet rs = pstmt.executeQuery()) {
                return findAllObjects(rs, rm);
            }
        }
    }

    public <T> T findOne(String sql, PreparedStatementSetter pss, CreateRowMapper rm) throws SQLException {
        List<T> originObjects = findAll(sql, pss, rm);
        if (originObjects.isEmpty()) {
            return null;
        }
        return originObjects.get(0);
    }

    public <T> List<T> findAllObjects(ResultSet rs, CreateRowMapper<T> rm) throws SQLException {
        List<T> findAll = new ArrayList<>();
        while (rs.next()) {
            findAll.add(rm.rowMapper(rs));
        }
        return findAll;
    }
}
