package myStuff.rest.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.media.sse.EventListener;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;

public class MyEventSource implements Runnable {

    private CountDownLatch latch;
    private int receiveCount;
    private CountDownLatch internalLatch;
    private String name;
    private AtomicInteger total;

    public MyEventSource(String name, CountDownLatch latch, int receiveCount, AtomicInteger total) {
        this.name = name;
        this.latch = latch;
        this.receiveCount = receiveCount;
        this.total = total;
    }

    @Override
    public void run() {
        internalLatch = new CountDownLatch(receiveCount);
        Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
        WebTarget target = client.target("http://127.0.0.1:8080/attribute/updates");
        EventSource eventSource = EventSource.target(target).build();
        EventListener listener = new EventListener() {
            @Override
            public void onEvent(InboundEvent inboundEvent) {
                System.out.println(name + ":" + inboundEvent.getName() + "; "
                        + inboundEvent.readData(String.class));
                total.getAndIncrement();
                internalLatch.countDown();
            }

            @Override
            public void onOpen() {
                System.out.println("Source Opened");
            }

            @Override
            public void onError(ErrorState state) {
                System.out.println("Source Error " + state);
            }
        };
        eventSource.register(listener);
        eventSource.open();
        try {
            internalLatch.await();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        latch.countDown();
        eventSource.close();
    }
    
    
}
