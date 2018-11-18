package cs601.project4.service;

import cs601.project4.entity.User;
import cs601.project4.repository.UserRepository;

import java.sql.SQLException;

public class UserService {
    public long addUser(User user){
        UserRepository userRepository = new UserRepository();
        Long result = null;
        try {
            result = userRepository.save(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
