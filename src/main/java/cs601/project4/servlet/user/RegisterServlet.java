package cs601.project4.servlet.user;

import cs601.project4.entity.User;
import cs601.project4.repository.UserRepository;
import cs601.project4.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = { "/register" })
public class RegisterServlet extends HttpServlet {
    private UserRepository userRepository;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("/user/register.html");
        rd.forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService userService = new UserService();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String phoneNumber = req.getParameter("phoneNumber");
        User user = new User(username, password, email, phoneNumber);
        System.out.print(user.getUsername());
        userService.addUser(user);
        RequestDispatcher rd = req.getRequestDispatcher("/user/login.html");
        rd.forward(req,resp);
    }
}
