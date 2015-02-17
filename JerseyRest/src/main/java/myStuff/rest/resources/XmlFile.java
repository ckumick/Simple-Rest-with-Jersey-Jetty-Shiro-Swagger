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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import myStuff.rest.providers.XmlFileProvider;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Path("xml/{name}")
@Api(value = "xml", description = "Return a xml file")
public class XmlFile {

	private final XmlFileProvider provider;

	@Inject
	public XmlFile(XmlFileProvider provider) {
		this.provider = provider;
	}

	@GET
	@Produces(MediaType.APPLICATION_XML)
	@ApiOperation(value = "Download an xml file", notes = "More notes about this method")
	public Response download(@PathParam("name") String name) {
		try {
			File file = new File(getClass().getClassLoader().getResource("defaultXmlFile.xml").toURI());
			return Response.ok(file).header("Content-Disposition", "attachment: filename=" + name + ".xml").build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@ApiOperation(value = "Upload an xml file", notes = "More notes about this method")
	public Response upload(@PathParam("name") String name, @FormDataParam("file") InputStream uploadStream,
			@FormDataParam("file") FormDataContentDisposition uploadDetail) {

		try {
			OutputStream outpuStream = new FileOutputStream(new File(provider.getFolder().getAbsolutePath()
					+ File.separator + uploadDetail.getFileName()));
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = uploadStream.read(bytes)) != -1) {
				outpuStream.write(bytes, 0, read);
			}
			outpuStream.flush();
			outpuStream.close();
			uploadStream.close();
		} catch (IOException e) {
			Response.serverError();
		}
		return Response.ok().build();
	}
}
