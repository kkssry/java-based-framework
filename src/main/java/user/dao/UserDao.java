package user.dao;

import user.domain.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {

    public void insert(User user) throws SQLException {
        JdbcTemplate template = new JdbcTemplate();

        template.update("INSERT INTO USERS VALUES(?,?,?,?)", (pstmt) -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        });


    }

    public void update(User user) throws SQLException {
        JdbcTemplate template = new JdbcTemplate();
        template.testUpdate("UPDATE USERS SET password=?, name=?, email=? WHERE userId=?",
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getUserId());
    }

    public void delete(User user) throws SQLException {
        JdbcTemplate template = new JdbcTemplate();
        template.update("DELETE FROM USERS WHERE userId=?",
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
                rs -> new User(rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                )
        );
    }

    public User findByUserId(String userId) throws SQLException {
        JdbcTemplate template = new JdbcTemplate();
        return template.findOne("SELECT * FROM USERS WHERE userId =?"
                , pstmt -> pstmt.setString(1, userId)
                ,rs -> new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")));
    }
}


