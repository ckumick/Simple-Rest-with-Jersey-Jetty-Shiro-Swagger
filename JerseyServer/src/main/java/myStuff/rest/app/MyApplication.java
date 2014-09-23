package myStuff.rest.app;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

public class MyApplication {
	public static void main(String[] args) {
		Server server = new Server(8112);

		ServletContextHandler context = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);

		ResourceConfig rc = new ResourceConfig();
		rc.packages("myStuff.rest.resources").register(new MyBinder());
//				.register(JacksonFeature.class);
		
		ServletHolder h = new ServletHolder(new ServletContainer(rc));
		h.setInitParameter(ServerProperties.PROVIDER_PACKAGES,
				"myStuff.rest.resources");
		h.setInitOrder(1);
		context.addServlet(h, "/*");
		server.setDumpAfterStart(true);
		try {
			server.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}