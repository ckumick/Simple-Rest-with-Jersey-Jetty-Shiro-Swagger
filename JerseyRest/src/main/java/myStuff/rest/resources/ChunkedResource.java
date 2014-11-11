package myStuff.rest.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import myStuff.rest.providers.AttributeValueListener;
import myStuff.rest.providers.AttributeValueProvider;

import org.glassfish.jersey.server.ChunkedOutput;

@Singleton
@Path("attribute/chunked")
public class ChunkedResource implements AttributeValueListener {

	private AttributeValueProvider provider;
	private List<ChunkedOutput<String>> outputs = new ArrayList<>();

	@Inject
	public ChunkedResource(AttributeValueProvider provider) {
		this.provider = provider;
		provider.addListenner(this);

	}

	@GET
	public synchronized ChunkedOutput<String> getChunkedResponse() {
		ChunkedOutput<String> output = new ChunkedOutput<String>(String.class);
		outputs.add(output);

		return output;
	}

	@Override
	public synchronized void update(String attribute, String value) {

		List<ChunkedOutput<String>> failedOutputs = new ArrayList<>();
		
		for (ChunkedOutput<String> output : outputs) {
			try {
				output.write(attribute + " updated to " + value);
			} catch (IOException e) {
				failedOutputs.add(output);
				try {
					output.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}

		for (ChunkedOutput<String> output : failedOutputs) {
			outputs.remove(output);
		}
	}

}
