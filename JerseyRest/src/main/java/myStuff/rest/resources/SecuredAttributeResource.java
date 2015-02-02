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
	public String getValueAsText(@Context SecurityContext sc, @PathParam("id") String id) {
		verifySecurity(sc);
		String value = provider.getValue(id);
		return value;
	}

	@POST
	@Consumes("text/plain")
	public void setValueAsText(@Context SecurityContext sc, @PathParam("id") String id, String value) {
		verifySecurity(sc);
		provider.setValue(id, value);
	}
	
	private void verifySecurity(SecurityContext sc) {
		if( !sc.isSecure() ) {
			throw new WebApplicationException(Status.FORBIDDEN);
		}
		
		Subject subject = SecurityUtils.getSubject();
		if( !subject.isAuthenticated() ) {
			throw new WebApplicationException(Status.UNAUTHORIZED);
		} else if( subject.getPrincipal() == null ) {
			throw new WebApplicationException(Status.UNAUTHORIZED);
		} else if (!subject.getPrincipal().equals("root") ) {
			throw new WebApplicationException(Status.FORBIDDEN);
		}
	}
	
}
