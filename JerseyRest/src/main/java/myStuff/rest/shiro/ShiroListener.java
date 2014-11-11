package myStuff.rest.shiro;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.shiro.web.env.EnvironmentLoaderListener;

@WebListener
public class ShiroListener extends EnvironmentLoaderListener implements
		ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().setInitParameter(ENVIRONMENT_CLASS_PARAM,
				ShiroEnvironment.class.getName());
		super.contextInitialized(sce);
	}

}