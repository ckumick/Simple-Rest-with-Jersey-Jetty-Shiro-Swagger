package myStuff.rest.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.http.HTTPException;

import org.eclipse.jetty.http.HttpStatus;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class HelloWolrdCommand extends HystrixCommand<String> {
    
    private Client client;

    public HelloWolrdCommand(Client client) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup2"));
        this.client = client;
    }

    @Override
    protected String run() throws Exception {
        WebTarget webTarget = client.target("http://127.0.0.1:8080/");
        WebTarget helloworldWebTarget = webTarget.path("hello");
        Invocation.Builder invocationBuilder = helloworldWebTarget
                .request(MediaType.TEXT_PLAIN_TYPE);
        invocationBuilder.header("some-header", "true");
        Response response = invocationBuilder.get();
        if( response.getStatus() != HttpStatus.OK_200) {
            throw new HTTPException(response.getStatus());
        }
        return response.readEntity(String.class);
    }

}
