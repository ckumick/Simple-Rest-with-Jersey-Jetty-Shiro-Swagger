package myStuff.rest.resources;

import static org.junit.Assert.*;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class HelloWorldResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(HelloWorldResource.class);
    }
    
	@Test
	public void test() {
		final String hello = target("hello").request().get(String.class);
		assertEquals("hello world", hello);
	}

}
