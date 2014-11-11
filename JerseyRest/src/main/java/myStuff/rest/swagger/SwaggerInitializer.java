/*******************************************************************************
 * Copyright (C) 2014 by Craig Kumick
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *******************************************************************************/
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