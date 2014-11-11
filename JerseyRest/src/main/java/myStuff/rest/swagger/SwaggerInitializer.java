package myStuff.rest.swagger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.wordnik.swagger.jaxrs.config.BeanConfig;

@WebListener
public class SwaggerInitializer implements ServletContextListener {

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setVersion("1.0.2");
		beanConfig.setResourcePackage("myStuff.rest.resources");
		beanConfig.setBasePath("http://localhost:8080/api");
		beanConfig.setDescription("My RESTful resources");
		beanConfig.setTitle("My RESTful API");
		beanConfig.setScan(true);
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
	}

}