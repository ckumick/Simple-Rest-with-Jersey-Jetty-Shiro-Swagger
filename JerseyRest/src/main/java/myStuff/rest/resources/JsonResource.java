package myStuff.rest.resources;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import myStuff.rest.data.JsonData;

@Path("json")
@Produces(MediaType.APPLICATION_JSON)
public class JsonResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JsonData getData() {
		List<String> alist = Arrays.asList(new String[] { "one", "two", "three" });
		
		return new JsonData(5, "hello", alist);
	}
}
