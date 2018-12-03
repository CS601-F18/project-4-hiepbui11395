package cs601.project4.application;

import cs601.project4.servlet.WebFrontEndServlet;
import cs601.project4.utils.Config;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class WebFrontEndServiceApplication {
    public static void main(String[] args) throws Exception {

        Server server = new Server(Integer.parseInt(Config.getInstance().getProperty("webFrontEndPort")));

        ServletContextHandler handler =
                new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        handler.setContextPath("/");
        server.setHandler(handler);

        ServletHolder servletHolder = handler.addServlet(ServletContainer.class, "/*");
        servletHolder.setInitOrder(1);
        servletHolder.setInitParameter("jersey.config.server.provider.classnames",
                WebFrontEndServlet.class.getCanonicalName());


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
