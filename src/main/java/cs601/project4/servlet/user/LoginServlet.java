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

@WebServlet(urlPatterns = { "/login" })
public class LoginServlet extends HttpServlet {
    UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("/user/login.jsp");
        rd.forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        Map<String, String> messages = new HashMap<String, String>();
        if (username == null || username.isEmpty()) {
            messages.put("username", "Please enter username");
        }

        if (password == null || password.isEmpty()) {
            messages.put("password", "Please enter password");
        }
        if(messages.isEmpty()){
            User user = userService.login(username,password);
            if(user!=null){
                req.getSession().setAttribute("user", user);
                resp.sendRedirect(req.getContextPath() + "/index");
                System.out.println("Login success");
            } else{
                System.out.println("Login fail");
                messages.put("login", "Wrong username or password! Try again!");
                req.setAttribute("messages", messages);
                RequestDispatcher rd = req.getRequestDispatcher("/user/login.jsp");
                rd.forward(req,resp);
            }
        }
    }
}
