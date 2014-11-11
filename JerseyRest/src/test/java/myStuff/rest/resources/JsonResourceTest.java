package myStuff.rest.resources;

import static org.junit.Assert.*;

import javax.ws.rs.core.Application;

import myStuff.rest.app.MyBinder;
import myStuff.rest.app.MyObjectMapper;
import myStuff.rest.data.JsonData;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

public class JsonResourceTest extends JerseyTest {

	@Override
	protected Application configure() {
		ResourceConfig rc = new ResourceConfig(AttributeResource.class);
		rc.packages("myStuff.rest.resources", "myStuff.rest.data", "com.fasterxml.jackson.jaxrs.json").register(new MyBinder())
				.register(JacksonFeature.class).register(JacksonJaxbJsonProvider.class).register(MyObjectMapper.class);
		return rc;
	}

	@Test
	public void test() {
		final Object data = target("json").request("application/json").get(
				String.class);
		assertNotNull(data);
	}

}
