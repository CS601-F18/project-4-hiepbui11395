package cs601.project4.service;

import cs601.project4.entity.User;
import cs601.project4.repository.UserRepository;

public class UserService {
    private UserRepository userRepository = UserRepository.getInstance();
    private static UserService instance;

    private UserService() {
    }

    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public Long create(User user) {
        Long result = userRepository.create(user);
        return result;
    }

    public User findUserById(long id) {
        User user = userRepository.findById(id);
        return user;
    }

    public User findUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user;
    }
}
