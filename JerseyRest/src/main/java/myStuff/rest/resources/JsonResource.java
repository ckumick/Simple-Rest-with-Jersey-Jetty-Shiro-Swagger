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

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import myStuff.rest.data.JsonData;

@Path("json")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "json", description = "Return a json value")
public class JsonResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "a json value", notes = "More notes about this method", response=myStuff.rest.data.JsonData.class)
	public JsonData getData() {
		List<String> alist = Arrays.asList(new String[] { "one", "two", "three" });
		
		return new JsonData(5, "hello", alist);
	}
}
