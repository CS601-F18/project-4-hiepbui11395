package cs601.project4;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.*;

import java.net.URI;

public class TicketPurchaseApplication {
    public static void main(String[] args) throws Exception{

        Server server = new Server(8081);

        //Using WebAppContext and web.xml
//        WebAppContext context = new WebAppContext();
//        context.setResourceBase("src/main/webapp");
//        context.setContextPath("/");
//        context.setDescriptor("webapp/WEB-INF/web.xml");
//        context.setParentLoaderPriority(true);
//        server.setHandler(context);
//
//        server.start();
//        server.join();

        //Using WebAppContext and annotation
        WebAppContext context = new WebAppContext();
        context.setResourceBase("src/main/webapp");
        context.setContextPath("/");
        context.setDescriptor("webapp/WEB-INF/web.xml");
        context.setConfigurations(new Configuration[]
                {
                        new AnnotationConfiguration(),
                        new WebInfConfiguration(),
                        new WebXmlConfiguration(),
                        new MetaInfConfiguration(),
                        new FragmentConfiguration(),
                        new EnvConfiguration(),
                        new PlusConfiguration(),
                        new JettyWebXmlConfiguration()
                });
        //Find annotation
        context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",".*/classes/.*");
        context.setParentLoaderPriority(true);

        //Avoid locking file on Windows
        //https://www.eclipse.org/jetty/documentation/9.4.x/troubleshooting-locked-files-on-windows.html
        context.setInitParameter(
                "org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
        server.setHandler(context);

        server.start();
        server.join();
    }
}
