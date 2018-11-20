package cs601.project4.servlet.event;

import cs601.project4.entity.Event;
import cs601.project4.entity.User;
import cs601.project4.service.EventService;
import cs601.project4.utils.Config;
import cs601.project4.utils.Utils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/events/create")
public class CreateEventServlet extends HttpServlet {
    private EventService eventService = EventService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("/event/create.jsp");
        rd.forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String location = req.getParameter("location");
        String dateStr = req.getParameter("date");
        Map<String, String> messages = new HashMap<String, String>();
        LocalDate date = null;
        if (name == null || name.isEmpty()) {
            messages.put("name", "Please enter event name!");
        }
        if (description == null || description.isEmpty()) {
            messages.put("description", "Please enter description!");
        }
        if (location == null || location.isEmpty()) {
            messages.put("location", "Please enter location!");
        }
        if (dateStr == null || dateStr.isEmpty()) {
            messages.put("date", "Please enter date!");
        } else{
            try{
                date = LocalDate.parse(req.getParameter("date"),
                        DateTimeFormatter.ofPattern(Config.getInstance().getProperty("dateFormat")));
            } catch (DateTimeParseException e){
                messages.put("date", "Date is incorrect!");
            }
        }
        if(messages.isEmpty()){
            Event event = new Event(name, description, location, date);
            eventService.addEvent(event);
            resp.sendRedirect(req.getContextPath() + "/events");
        } else{
            req.setAttribute("messages", messages);
            RequestDispatcher rd = req.getRequestDispatcher("/user/register.jsp");
            rd.forward(req,resp);
        }
    }
}
