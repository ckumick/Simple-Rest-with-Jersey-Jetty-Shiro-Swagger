package myStuff.rest.resources;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import myStuff.rest.app.MyBinder;
import myStuff.rest.app.MyObjectMapper;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

public class XmlFileTransferTest extends JerseyTest {

	@ClassRule
	public static TemporaryFolder testFolder = new TemporaryFolder();

	@Override
	protected Application configure() {
		File folder = null;
		try {
			folder = new File(testFolder.getRoot().getAbsolutePath() + File.separator + "uploads");
			if( !folder.exists() ) {
				testFolder.newFolder("uploads");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ResourceConfig rc = new ResourceConfig(AttributeResource.class);
		rc.packages("myStuff.rest.resources", 
				"myStuff.rest.data", 
				"com.fasterxml.jackson.jaxrs.json",
				"org.glassfish.jersey.examples.multipart")
				.register(new MyBinder(folder)).register(JacksonFeature.class).register(JacksonJaxbJsonProvider.class)
				.register(MultiPartFeature.class)
				.register(MyObjectMapper.class);
		return rc;
	}
	
    @Override
    protected void configureClient(ClientConfig config) {
        config.register(MultiPartFeature.class);
    }


	@Test
	public void testDownload() {
		Response response = target("xml/testFile").request(MediaType.APPLICATION_XML).get();
		assertThat("Status OK", response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
		assertThat("Content-Disposition is needs attachment: filename=xxx",
				response.getHeaderString("Content-Disposition"), equalTo("attachment: filename=testFile.xml"));
		assertThat("Has file data", response.readEntity(String.class), equalTo("<Hello>world</Hello>"));
	}

	@Test
	public void testUpload() throws IOException {
		InputStream uploadFile = getClass().getResourceAsStream("/newFile.xml");
		MultiPart multiPart = new MultiPart();
	    multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
		multiPart.bodyPart( new StreamDataBodyPart("file",
				uploadFile,
				"newFile.xml",
	            MediaType.APPLICATION_XML_TYPE) );
	    Response response = target("xml/newFile").request(MediaType.MULTIPART_FORM_DATA_TYPE)
				.post(Entity.entity(multiPart, multiPart.getMediaType()));
	    
		assertThat("Status OK", response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
		
		File folder = new File(testFolder.getRoot().getAbsoluteFile() + File.separator + "uploads");
		File newFile = new File( folder.getAbsolutePath() + File.separator + "newFile.xml");
		assertThat("New file created in temp folder", newFile.exists(), is(true));
		
		
	}

}
