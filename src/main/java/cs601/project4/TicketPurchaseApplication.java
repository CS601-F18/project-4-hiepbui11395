package cs601.project4;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.*;

import java.net.URI;

public class TicketPurchaseApplication {
    public static void main(String[] args) throws Exception{

        Server server = new Server(8080);

        //Using raw servlet
//        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        context.setContextPath("/");
//        server.setHandler(context);
//
//        ServletHolder holderHome = new ServletHolder("default", DefaultServlet.class);
//        holderHome.setInitParameter("resourceBase", "./src/main/webapp/");
//        holderHome.setInitParameter("dirAllowed","true");
//        holderHome.setInitParameter("pathInfoOnly","true");
//        context.addServlet(holderHome, "/*");
//
//
//        context.addServlet(IndexServlet.class, "/index");

        WebAppContext context = new WebAppContext();
        context.setResourceBase("src/main/webapp");
        context.setContextPath("/");
        context.setDescriptor("webapp/WEB-INF/web.xml");
        context.setParentLoaderPriority(true);
        server.setHandler(context);

        server.start();
        server.join();
    }
}
