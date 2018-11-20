package cs601.project4.servlet.user;

import cs601.project4.entity.User;
import cs601.project4.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = { "/register" })
public class RegisterServlet extends HttpServlet {
    private UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("/user/register.jsp");
        rd.forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String phoneNumber = req.getParameter("phoneNumber");
        Map<String, String> messages = new HashMap<String, String>();
        //Check if username/email exist
        User user = userService.findUserByUsername(username);
        if(user!=null){
            messages.put("username", "Username already taken!");
        }
        user = userService.findUserByEmail(email);
        if(user!=null){
            messages.put("email", "Email already used!");
        }
        if(messages.isEmpty()){
            user = new User(username, password, email, phoneNumber);
            userService.addUser(user);
            resp.sendRedirect(req.getContextPath() + "/login");
        } else{
            req.setAttribute("messages", messages);
            RequestDispatcher rd = req.getRequestDispatcher("/user/register.jsp");
            rd.forward(req,resp);
        }
    }
}
