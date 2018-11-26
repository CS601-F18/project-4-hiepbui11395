package cs601.project4;

import cs601.project4.servlet.EventServlet;
import cs601.project4.servlet.event.CreateServlet;
import cs601.project4.utils.Config;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

public class EventServiceApplication {
    public static void main(String[] args) throws Exception{

        Server server = new Server(Integer.parseInt(Config.getInstance().getProperty("eventPort")));

        ServletContextHandler handler =
                new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        handler.setContextPath("/");
        server.setHandler(handler);

        ServletHolder servletHolder = handler.addServlet(ServletContainer.class, "/*");
        servletHolder.setInitOrder(1);
        servletHolder.setInitParameter(ServerProperties.PROVIDER_CLASSNAMES,
                EventServlet.class.getCanonicalName());


        try {
            server.start();
            server.join();
        }finally {

            server.destroy();
        }
    }
}
