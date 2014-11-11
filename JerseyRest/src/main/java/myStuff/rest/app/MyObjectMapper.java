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
package myStuff.rest.app;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import myStuff.rest.data.JsonData;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

@Provider
public class MyObjectMapper implements ContextResolver<ObjectMapper> {

	    final ObjectMapper defaultObjectMapper;
	    final ObjectMapper combinedObjectMapper;

	    public MyObjectMapper() {
	        defaultObjectMapper = createDefaultMapper();
	        combinedObjectMapper = createCombinedObjectMapper();
	    }

	    @Override
	    public ObjectMapper getContext(final Class<?> type) {

	        if (type == JsonData.class) {
	            return combinedObjectMapper;
	        } else {
	            return defaultObjectMapper;
	        }
	    }

	    private static ObjectMapper createCombinedObjectMapper() {
	        return new ObjectMapper()
	        		.configure(SerializationFeature.INDENT_OUTPUT, true)
	                .setAnnotationIntrospector(createJaxbJacksonAnnotationIntrospector());
	    }

	    private static ObjectMapper createDefaultMapper() {
	        final ObjectMapper result = new ObjectMapper();
	        result.enable(SerializationFeature.INDENT_OUTPUT);
	        return result;
	    }

	    private static AnnotationIntrospector createJaxbJacksonAnnotationIntrospector() {
	        final AnnotationIntrospector jacksonIntrospector = new JacksonAnnotationIntrospector();
	        return jacksonIntrospector;
	    }
	}
