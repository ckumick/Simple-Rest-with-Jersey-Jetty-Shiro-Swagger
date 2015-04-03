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
package myStuff.rest.app;

import java.io.File;
import java.nio.file.Paths;
import java.util.EnumSet;

import javax.servlet.DispatcherType;

import myStuff.rest.shiro.MyFilter;
import myStuff.rest.shiro.ShiroListener;
import myStuff.rest.swagger.ApiOriginFilter;
import myStuff.rest.swagger.SwaggerInitializer;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.message.DeflateEncoder;
import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.EncodingFilter;
import org.glassfish.jersey.servlet.ServletContainer;

import com.wordnik.swagger.jersey.config.JerseyJaxrsConfig;

public class MyApplication {
    public static void main(String[] args) {
        Server server = new Server();

        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecureScheme("https");
        http_config.setSecurePort(8443);
        http_config.setOutputBufferSize(32768);

        ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(http_config));
        http.setPort(8080);
        http.setIdleTimeout(30000);

        File keystoreFile = new File(System.getProperty("user.home") + File.separator + "keystore");
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(keystoreFile.getAbsolutePath());
        sslContextFactory.setKeyStorePassword("OBF:1xfd1zt11uha1ugg1zsp1xfp");
        sslContextFactory.setKeyManagerPassword("OBF:1xfd1zt11uha1ugg1zsp1xfp");

        HttpConfiguration https_config = new HttpConfiguration(http_config);
        https_config.addCustomizer(new SecureRequestCustomizer());

        ServerConnector https = new ServerConnector(server, new SslConnectionFactory(
                sslContextFactory, HttpVersion.HTTP_1_1.asString()), new HttpConnectionFactory(
                https_config));
        https.setPort(8443);
        https.setIdleTimeout(500000);

        server.setConnectors(new Connector[] { https, http });

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addEventListener(new ShiroListener());
        context.addEventListener(new SwaggerInitializer());

        EnumSet<DispatcherType> types = EnumSet.allOf(DispatcherType.class);
        context.addFilter(ShiroFilter.class, "/*", types);
        context.addFilter(ApiOriginFilter.class, "/*", EnumSet.allOf(DispatcherType.class));

        ResourceConfig rc = new ResourceConfig();
        rc.packages("myStuff.rest.resources;com.wordnik.swagger.jersey.listing")
                .register(
                        new MyBinder(new File(System.getProperty("user.home") + File.separator
                                + "JerseyServer")))
                .register(MultiPartFeature.class)
                .register(JacksonFeature.class)
                .register(SseFeature.class)
                .register(EncodingFilter.class)
                .register(GZipEncoder.class)
                .register(DeflateEncoder.class);

        ServletHolder h = new ServletHolder(new ServletContainer(rc));
        h.setInitParameter(ServerProperties.PROVIDER_PACKAGES,
                "myStuff.rest.resources;com.wordnik.swagger.jersey.listing");
        h.setInitOrder(1);
        context.addServlet(h, "/*");

        // static files handler
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("./src/main/webapp/");
        ContextHandler resourceContext = new ContextHandler("/api-docs-ui");
        resourceContext.setHandler(resourceHandler);

        HandlerCollection handlerList = new HandlerCollection();
        handlerList.setHandlers(new Handler[] { resourceContext, context });
        server.setHandler(handlerList);

        server.setDumpAfterStart(true);
        try {
            server.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}