package myStuff.rest.resources;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import myStuff.rest.providers.AttributeValueProvider;

@Path("secure/{id}")
public class SecuredAttributeResource {

	private final AttributeValueProvider provider;
	
	@Inject
	public SecuredAttributeResource(AttributeValueProvider provider) {
		this.provider = provider;
	}

	@GET
	@Produces("text/plain")
	@RolesAllowed("admin")
	public String getValueAsText(@PathParam("id") String id) {
		Subject subject = SecurityUtils.getSubject();
		if( !subject.isAuthenticated() ) {
			throw new WebApplicationException(Status.UNAUTHORIZED);
		} else if( subject.getPrincipal() == null ) {
			throw new WebApplicationException(Status.UNAUTHORIZED);
		} else if (!subject.getPrincipal().equals("root") ) {
			throw new WebApplicationException(Status.FORBIDDEN);
		}
		
		String value = provider.getValue(id);
		return value;
	}
	
	@POST
	@Consumes("text/plain")
	public void setValueAsText(@PathParam("id") String id, String value) {
		Subject subject = SecurityUtils.getSubject();
		if( !subject.isAuthenticated() ) {
			throw new WebApplicationException(Status.UNAUTHORIZED);
		} else if( subject.getPrincipal() == null ) {
			throw new WebApplicationException(Status.UNAUTHORIZED);
		} else if (!subject.getPrincipal().equals("root") ) {
			throw new WebApplicationException(Status.FORBIDDEN);
		}
		provider.setValue(id, value);
	}
}
