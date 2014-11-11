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
