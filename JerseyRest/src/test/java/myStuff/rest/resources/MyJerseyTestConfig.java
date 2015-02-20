package myStuff.rest.resources;

import java.io.File;

import javax.ws.rs.core.Application;

import myStuff.rest.app.MyBinder;
import myStuff.rest.app.MyObjectMapper;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.ClassRule;
import org.junit.rules.TemporaryFolder;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

public class MyJerseyTestConfig extends JerseyTest {

	@ClassRule
	public static TemporaryFolder testFolder = new TemporaryFolder();
	public File serverStorageFolder;

	@Override
	protected Application configure() {
		serverStorageFolder = new File(testFolder.getRoot().getAbsolutePath());
		
		ResourceConfig rc = new ResourceConfig(AttributeResource.class);
		rc.packages("myStuff.rest.resources", 
				"myStuff.rest.data", 
				"com.fasterxml.jackson.jaxrs.json",
				"org.glassfish.jersey.examples.multipart")
				.register(new MyBinder(serverStorageFolder)).register(JacksonFeature.class).register(JacksonJaxbJsonProvider.class)
				.register(MultiPartFeature.class)
				.register(MyObjectMapper.class);
		return rc;
	}
	
    @Override
    protected void configureClient(ClientConfig config) {
        config.register(MultiPartFeature.class);
    }

	
}
