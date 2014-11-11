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
//	                .configure(SerializationFeature.WRAP_ROOT_VALUE, true)
//	                .configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true)
//	                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
	        		.configure(SerializationFeature.INDENT_OUTPUT, true)
	                .setAnnotationIntrospector(createJaxbJacksonAnnotationIntrospector());
	    }

	    private static ObjectMapper createDefaultMapper() {
	        final ObjectMapper result = new ObjectMapper();
	        result.enable(SerializationFeature.INDENT_OUTPUT);
	        return result;
	    }

	    private static AnnotationIntrospector createJaxbJacksonAnnotationIntrospector() {
	        final AnnotationIntrospector jaxbIntrospector = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
	        final AnnotationIntrospector jacksonIntrospector = new JacksonAnnotationIntrospector();
	        //return AnnotationIntrospector.pair(jacksonIntrospector, jaxbIntrospector);
	        return jacksonIntrospector;
	    }
	}
