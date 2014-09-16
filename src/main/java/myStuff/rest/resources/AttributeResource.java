package myStuff.rest.resources;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

import myStuff.rest.providers.AttributeValueProvider;

@Path("attribute/{id}")
public class AttributeResource {

	private static Map<String, String> values = new HashMap<>();
	private final AttributeValueProvider provider;
	
	@Inject
	public AttributeResource(AttributeValueProvider provider) {
		this.provider = provider;
	}

	@GET
	@Produces("text/plain")
	public String getValueAsText(@PathParam("id") String id) {
		String value = provider.getValue(id);
		if( value.isEmpty() ) {
			throw new WebApplicationException(404);
		}
		return value;
	}
	
	@POST
	@Consumes("text/plain")
	public void setValueAsText(@PathParam("id") String id, String value) {
		provider.setValue(id, value);
	}
}
