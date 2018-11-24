package cs601.project4.service;

import cs601.project4.entity.User;
import cs601.project4.repository.UserRepository;
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
        Long result = null;
        try {
            result = userRepository.save(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public User findUserById(long id){
        User user = null;
        try {
            user = userRepository.findUserById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User findUserByUsername(String username){
        User user = null;
        try {
            user = userRepository.findUserByUsername(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
