package cs601.project4.service;

import cs601.project4.entity.User;
import cs601.project4.repository.UserRepository;

public class UserService {
    private UserRepository userRepository = UserRepository.getInstance();
    private static UserService instance;

    private UserService() { }

    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    /**
     * Add user into db
     * @param user
     * @return UserId or null
     */
    public Long create(User user) {
        Long result = userRepository.create(user);
        return result;
    }

    /**
     * Get user by user id
     * @param id
     * @return User entity or null
     */
    public User findUserById(long id) {
        User user = userRepository.findById(id);
        return user;
    }

    /**
     * Get user by username
     * @param username
     * @return User entity or null
     */
    public User findUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user;
    }
}
