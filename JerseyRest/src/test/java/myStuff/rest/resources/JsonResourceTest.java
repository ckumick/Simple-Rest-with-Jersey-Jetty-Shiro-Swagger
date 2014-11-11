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
