package cs601.project4.service;

import cs601.project4.entity.User;
import cs601.project4.repository.UserRepository;
import cs601.project4.utils.Utils;
import org.apache.commons.codec.binary.Hex;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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

    public long addUser(User user){
        Long result = null;
        try {
            String salt = Utils.generateSalt();
            String hashPassword = Utils.hashPassword(user.getPassword(), salt);
            user.setPassword(hashPassword);
            user.setSalt(salt);
            result = userRepository.save(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public User findUserByUsernameOrEmail(String username, String email){
        User user = null;
        try {
            user = userRepository.findUserByUsernameOrEmail(username, email);
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

    public User findUserByEmail(String email){
        User user = null;
        try {
            user = userRepository.findUserByEmail(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User login(String username, String password){
        try {
            User user = userRepository.findUserByUsername(username);
            if(user != null){
                String hashPassword = Utils.hashPassword(password, user.getSalt());
                if(hashPassword.equals(user.getPassword())){
                    return user;
                } else{
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
