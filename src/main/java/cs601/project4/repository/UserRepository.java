package cs601.project4.repository;

import cs601.project4.entity.User;
import cs601.project4.jdbc.ConnectionUtil;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    private static UserRepository instance;

    private UserRepository(){}

    public static synchronized UserRepository getInstance(){
        if(instance == null){
            instance = new UserRepository();
        }
        return instance;
    }

    private final String SQL_INSERT = "insert into `user` (`name`,`description`,`location`,`date`,`phoneNumber`,`active`)" +
            "values (?,?,?,?,?,?)";

    public long save(User entity) throws SQLException{
        //TODO: Check user id to know it is add or update
        Connection connection = ConnectionUtil.getMyConnection();
        try{
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getPassword());
            statement.setString(3, entity.getSalt());
            statement.setString(4, entity.getEmail());
            statement.setString(5, entity.getPhoneNumber());
            statement.setBoolean(6, true);
            int affectedRow = statement.executeUpdate();
            if(affectedRow == 0){
                throw new SQLException("Creating user failed - no row affected");
            }
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()){
                entity.setId(rs.getLong(1));
            } else{
                throw new SQLException("Creating user failed - no Id obtained");
            }
            return entity.getId();
        } finally {
            connection.close();
        }
    }

    public User findUserByUsername(String username) throws SQLException{
        Connection connection = ConnectionUtil.getMyConnection();
        try{
            PreparedStatement statement = connection.prepareStatement(
                    "select * from `user` where `username` = ?");
            statement.setString(1,username);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                User user = new User(rs);
                return user;
            } else {
                return null;
            }
        } finally {
            connection.close();
        }
    }

    public User findUserByEmail(String email) throws SQLException{
        Connection connection = ConnectionUtil.getMyConnection();
        try{
            PreparedStatement statement = connection.prepareStatement(
                    "select * from `user` where `email` = ?");
            statement.setString(1,email);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                User user = new User(rs);
                return user;
            } else {
                return null;
            }
        } finally {
            connection.close();
        }
    }

    public User findUserByUsernameOrEmail(String username, String email) throws SQLException{
        Connection connection = ConnectionUtil.getMyConnection();
        try{
            PreparedStatement statement = connection.prepareStatement(
                    "select * from `user` where `username` = ? or `email` = ?");
            statement.setString(1,username);
            statement.setString(2,email);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                User user = new User(rs);
                return user;
            } else {
                return null;
            }
        } finally {
            connection.close();
        }
    }
}
