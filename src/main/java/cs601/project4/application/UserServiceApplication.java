package cs601.project4.application;

import cs601.project4.servlet.UserServlet;
import cs601.project4.utils.Config;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

public class UserServiceApplication {
    public static void main(String[] args) throws Exception {

        Server server = new Server(Integer.parseInt(Config.getInstance().getProperty("userPort")));

        ServletContextHandler handler =
                new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        handler.setContextPath("/");
        server.setHandler(handler);

        ServletHolder servletHolder = handler.addServlet(ServletContainer.class, "/*");
        servletHolder.setInitOrder(1);
        servletHolder.setInitParameter(ServerProperties.PROVIDER_CLASSNAMES,
                UserServlet.class.getCanonicalName());


        try {
            server.start();
            server.join();
        } catch (Exception e){
            e.printStackTrace();
            server.stop();
            server.destroy();
        }

    }
}
