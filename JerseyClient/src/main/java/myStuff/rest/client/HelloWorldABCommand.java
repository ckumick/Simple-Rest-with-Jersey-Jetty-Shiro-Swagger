package myStuff.rest.client;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.http.HTTPException;

import org.eclipse.jetty.http.HttpStatus;

import rx.functions.Action1;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class HelloWorldABCommand extends HystrixCommand<String>
{

    private static class HelloWorldACommand extends HystrixCommand<String> 
    {

        private Client clientA;

        public HelloWorldACommand(Client client) {
            super(HystrixCommandGroupKey.Factory.asKey("HelloWorldACommand"));
            clientA = client;
        }

        @Override
        protected String run() throws Exception {
            Random rand = new Random();
            Thread.sleep( rand.nextInt(10) );
            
            WebTarget webTarget = clientA.target("http://127.0.0.1:8080/");
            WebTarget helloworldWebTarget = webTarget.path("hello");
            Invocation.Builder invocationBuilder = helloworldWebTarget
                    .request(MediaType.TEXT_PLAIN_TYPE);
            invocationBuilder.header("some-header", "true");
            Response response = invocationBuilder.get();
            if( response.getStatus() != HttpStatus.OK_200) {
                throw new HTTPException(response.getStatus());
            }
            return response.readEntity(String.class) + " from A";
        }
        
    }

    private static class HelloWorldBCommand extends HystrixCommand<String> 
    {

        private Client clientB;

        public HelloWorldBCommand(Client client) {
            super(HystrixCommandGroupKey.Factory.asKey("HelloWorldBCommand"));
            clientB = client;
        }

        @Override
        protected String run() throws Exception {
            Random rand = new Random();
            Thread.sleep( rand.nextInt(10) );
            
            WebTarget webTarget = clientB.target("http://127.0.0.1:8080/");
            WebTarget helloworldWebTarget = webTarget.path("hello");
            Invocation.Builder invocationBuilder = helloworldWebTarget
                    .request(MediaType.TEXT_PLAIN_TYPE);
            invocationBuilder.header("some-header", "true");
            Response response = invocationBuilder.get();
            if( response.getStatus() != HttpStatus.OK_200) {
                throw new HTTPException(response.getStatus());
            }
            return response.readEntity(String.class) + " from B";
        }
        
    }

    private final Client client;

    public HelloWorldABCommand(Client client) {
        super(HystrixCommandGroupKey.Factory.asKey("HelloWorldABCommand"));
        this.client = client;
    }

    @Override
    protected String run() throws Exception {
        final ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(2);
        new HelloWorldACommand(client).observe().subscribe( new Action1<String>() {
            @Override
            public void call(String t) {
                try {
                    queue.put(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        
        new HelloWorldBCommand(client).observe().subscribe( new Action1<String>() {
            @Override
            public void call(String t) {
                try {
                    queue.put(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        
        return queue.take();
    }
    
}
