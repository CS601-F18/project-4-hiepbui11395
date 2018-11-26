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

    private final String SQL_INSERT = "insert into `user` (`username`)" +
            "values (?)";
    private final String SQL_UPDATE = "update `user` set `username`=? " +
            "where `id`=?";

    public User findById(long id, Connection connection) throws SQLException{
            PreparedStatement statement = connection.prepareStatement(
                    "select * from `user` where `id` = ?");
            statement.setLong(1,id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                User user = new User(rs);
                return user;
            } else{
                return null;
            }
    }

    public User findByUsername(String username, Connection connection) throws SQLException{
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
    }

    public Long create(User entity, Connection connection) throws SQLException{
        PreparedStatement statement = connection.prepareStatement(SQL_INSERT,
                PreparedStatement.RETURN_GENERATED_KEYS);
        statement.setString(1, entity.getUsername());

        int affectedRow = statement.executeUpdate();
        if(affectedRow == 0){
            return null;
        }
        ResultSet rs = statement.getGeneratedKeys();
        if(rs.next()){
            return rs.getLong(1);
        } else{
            return null;
        }
    }

    public User update(User entity, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);
        statement.setString(1, entity.getUsername());
        statement.setLong(2, entity.getId());

        int affectedRow = statement.executeUpdate();
        if(affectedRow != 0){
            return entity;
        } else{
            return null;
        }
    }
}
