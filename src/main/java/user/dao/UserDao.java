package user.dao;

import core.jdbc.ConnectionManager;
import user.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {

    public void insert(User user) throws SQLException {
        JdbcTemplate template = new JdbcTemplate();

        template.sendQuery("INSERT INTO USERS VALUES(?,?,?,?)", (pstmt) -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        });
    }


    public void update(User user) throws SQLException {
        JdbcTemplate template = new JdbcTemplate();
//        template.sendQuery("UPDATE USERS SET password=?, name=?, email=? WHERE userId=?", new PreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement pstmt) throws SQLException {
//                pstmt.setString(1, user.getPassword());
//                pstmt.setString(2, user.getName());
//                pstmt.setString(3, user.getEmail());
//                pstmt.setString(4, user.getUserId());
//            }
//        });
        template.sendQueryWithMultiArray("UPDATE USERS SET password=?, name=?, email=? WHERE userId=?",
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getUserId());
    }

    public void update2(User user) throws SQLException {
        // TODO 구현 필요함.
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = "UPDATE users SET password = ?, name = ?, email = ? WHERE userId = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());

            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }

            if (con != null) {
                con.close();;
            }
        }
    }

    public void delete(User user) throws SQLException {
        JdbcTemplate template = new JdbcTemplate();
        template.sendQuery("DELETE FROM USERS WHERE userId=?",
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement pstmt) throws SQLException {
                        pstmt.setString(1, user.getUserId());
                    }
                });
    }


    public List<User> findAll() throws SQLException {
        JdbcTemplate template = new JdbcTemplate();
        return template.findAll("SELECT * FROM USERS",
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement pstmt) throws SQLException {

                    }
                },
                new CreateRowMapper() {
                    @Override
                    public Object rowMapper(ResultSet rs) throws SQLException {
                        return new User(rs.getString("userId"),
                                rs.getString("password"),
                                rs.getString("name"),
                                rs.getString("email")
                        );
                    }
                }
        );
    }

    public User findByUserId(String userId) throws SQLException {
        JdbcTemplate template = new JdbcTemplate();
        return template.findOne("SELECT * FROM USERS WHERE userId =?"
                , new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement pstmt) throws SQLException {
                        pstmt.setString(1, userId);
                    }
                }
                , new CreateRowMapper() {
                    @Override
                    public Object rowMapper(ResultSet rs) throws SQLException {
                        return new User(
                                rs.getString("userId"),
                                rs.getString("password"),
                                rs.getString("name"),
                                rs.getString("email"));
                    }
                });
    }
}


