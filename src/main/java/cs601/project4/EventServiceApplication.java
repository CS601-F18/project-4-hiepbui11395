package cs601.project4;

import cs601.project4.servlet.event.CreateServlet;
import cs601.project4.utils.Config;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class EventServiceApplication {
    public static void main(String[] args) throws Exception{

        Server server = new Server(
                Integer.parseInt(Config.getInstance().getProperty("eventPort")));

        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);

        handler.addServletWithMapping(CreateServlet.class, "/create");

        server.start();
        server.join();
    }
}
