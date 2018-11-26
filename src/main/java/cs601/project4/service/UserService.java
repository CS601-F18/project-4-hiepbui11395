package cs601.project4.service;

import cs601.project4.entity.User;
import cs601.project4.jdbc.ConnectionUtil;
import cs601.project4.repository.UserRepository;

import java.sql.Connection;
import java.sql.SQLException;

public class UserService {
    private UserRepository userRepository = UserRepository.getInstance();
    private static UserService instance;
    private UserService(){}

    public static synchronized UserService getInstance() {
        if(instance == null){
            instance = new UserService();
        }
        return instance;
    }

    public Long create(User user){
        try(Connection connection = ConnectionUtil.getMyConnection()) {
            Long result = userRepository.createUser(user, connection);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User findUserById(long id){
        try(Connection connection = ConnectionUtil.getMyConnection()) {
            User user = userRepository.findUserById(id, connection);
            return  user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User findUserByUsername(String username){
        try(Connection connection = ConnectionUtil.getMyConnection()) {
            User user = userRepository.findUserByUsername(username, connection);
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
