package cs601.project4.repository;

import cs601.project4.entity.User;
import cs601.project4.jdbc.ConnectionUtil;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    private static UserRepository instance;

    private UserRepository() {
    }

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    private final String SQL_INSERT = "insert into `user` (`username`)" +
            "values (?)";

    /**
     * Find user by user id
     * @param id
     * @return User entity or null
     */
    public User findById(long id) {
        BasicDataSource dataSource = ConnectionUtil.getMyConnection();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select * from `user` where `id` = ?")) {
            statement.setLong(1, id);
            return getUserResultSet(statement);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Find user by username
     * @param username
     * @return User entity or null
     */
    public User findByUsername(String username) {
        BasicDataSource dataSource = ConnectionUtil.getMyConnection();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select * from `user` where `username` = ?")) {
            statement.setString(1, username);
            return getUserResultSet(statement);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Add User into db
     * @param entity
     * @return UserId or null
     */
    public Long create(User entity) {
        BasicDataSource dataSource = ConnectionUtil.getMyConnection();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getUsername());

            int affectedRow = statement.executeUpdate();
            if (affectedRow == 0) {
                return null;
            }
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse result set into User
     * @param statement
     * @return
     * @throws SQLException
     */
    private User getUserResultSet(PreparedStatement statement) throws SQLException {
        try (ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                User user = new User(rs);
                return user;
            } else {
                return null;
            }
        }
    }
}
