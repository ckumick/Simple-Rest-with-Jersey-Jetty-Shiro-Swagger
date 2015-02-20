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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

public class XmlFileTransferTest extends JerseyTest {

	private static final String SERVER_SOURCE_LOCATION = "/xmlFile/serverSide/";
	private static final String CLIENT_SOURCE_LOCATION = "/xmlFile/clientSide/";
	private static final String FILE_TO_UPLOAD = "newFile.xml";
	private static final String FILE_TO_DOWNLOAD = "exisitingFile.xml";
	
	
	@ClassRule
	public static TemporaryFolder testFolder = new TemporaryFolder();
	private File serverStorageFolder;

	@Override
	protected Application configure() {
		serverStorageFolder = new File(testFolder.getRoot().getAbsolutePath());
		
		ResourceConfig rc = new ResourceConfig(AttributeResource.class);
		rc.packages("myStuff.rest.resources", 
				"myStuff.rest.data", 
				"com.fasterxml.jackson.jaxrs.json",
				"org.glassfish.jersey.examples.multipart")
				.register(new MyBinder(serverStorageFolder)).register(JacksonFeature.class).register(JacksonJaxbJsonProvider.class)
				.register(MultiPartFeature.class)
				.register(MyObjectMapper.class);
		return rc;
	}
	
    @Override
    protected void configureClient(ClientConfig config) {
        config.register(MultiPartFeature.class);
    }


	@Test
	public void testDownload() throws IOException {
		File serverFileOnServer = setupServerFile();
		
		Response response = downloadFile();
		
		assertThat("Status OK", response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
		assertThat("Content-Disposition is needs attachment: filename=xxx",
				response.getHeaderString("Content-Disposition"), equalTo("attachment: filename=" + FILE_TO_DOWNLOAD));
		assertThat("Files are the same", response.readEntity(String.class), equalTo(FileUtils.readFileToString(serverFileOnServer)));
	}

	private File setupServerFile() throws IOException {
		File serverFileFromTestResouces = FileUtils.toFile(getClass().getResource(SERVER_SOURCE_LOCATION + FILE_TO_DOWNLOAD));
		File serverFileOnServer = new File( serverStorageFolder.getAbsolutePath() + File.separator + FILE_TO_DOWNLOAD);
		FileUtils.copyFile(serverFileFromTestResouces, serverFileOnServer);
		return serverFileOnServer;
	}

	private Response downloadFile() {
		Response response = target("xml/" + FilenameUtils.removeExtension(FILE_TO_DOWNLOAD) ).request(MediaType.APPLICATION_XML).get();
		return response;
	}

	@Test
	public void testDownloadNotFound() {
		Response response = target("xml/unknownFile").request(MediaType.APPLICATION_XML).get();
		assertThat("Status 404 Not Found", response.getStatus(), equalTo(Response.Status.NOT_FOUND.getStatusCode()));
	}

	@Test
	public void testUpload() throws IOException {
		File uploadFile = FileUtils.toFile(getClass().getResource(CLIENT_SOURCE_LOCATION + FILE_TO_UPLOAD));
		InputStream uploadStream = getClass().getResourceAsStream(CLIENT_SOURCE_LOCATION + FILE_TO_UPLOAD);
		
		File newFileOnServer = new File( serverStorageFolder.getAbsolutePath() + File.separator + FILE_TO_UPLOAD);
		assertThat("File doesn't exist on server yet", newFileOnServer.exists(), is(false));
		
		Response response = uploadFile(uploadFile, uploadStream);
	    
		assertThat("Status OK", response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
		assertThat("File loaded to server", newFileOnServer.exists(), is(true));
		assertThat("Files are the same", FileUtils.readLines(newFileOnServer), equalTo(FileUtils.readLines(uploadFile)));
	}

	private Response uploadFile(File uploadFile, InputStream uploadStream) throws IOException {
		MultiPart multiPart = new MultiPart();
	    multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
		multiPart.bodyPart( new StreamDataBodyPart("file",
				uploadStream,
				FILE_TO_UPLOAD,
	            MediaType.APPLICATION_XML_TYPE) );
	    Response response = target("xml/" + FilenameUtils.removeExtension(uploadFile.getName()) ).request(MediaType.MULTIPART_FORM_DATA_TYPE)
				.post(Entity.entity(multiPart, multiPart.getMediaType()));
	    uploadStream.close();
		return response;
	}

}
