package myStuff.rest.resources;

import static org.junit.Assert.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import myStuff.rest.app.MyBinder;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class AttributeResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        ResourceConfig rc = new ResourceConfig(AttributeResource.class);
        rc.packages("myStuff.rest.resources").register(new MyBinder());
		return rc;
    }
    
	@Test
	public void test() {
		Entity<String> entity = Entity.entity("hello", "text/plain");
		
		Response response = target("attribute/test").request().buildPost(Entity.entity("hello", "text/plain")).invoke();
		final String hello = target("attribute/test").request().get(String.class);
		assertEquals("hello", hello);
	}

}
