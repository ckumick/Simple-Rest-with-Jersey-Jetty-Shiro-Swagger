package myStuff.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Path("hello")
@Api(value = "/hello", description = "Return hellow world")
public class HelloWorldResource {
	@GET
	@ApiOperation(value = "hello world", notes = "More notes about this method", response = String.class)
	@Produces("text/plain")
	public String getHello() {
		return "hello world";
	}

}
