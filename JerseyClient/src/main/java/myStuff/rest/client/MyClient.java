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
package myStuff.rest.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.EventListener;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;

import rx.Observable;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import com.netflix.hystrix.exception.HystrixRuntimeException;

public class MyClient {

    public static void main(String[] args) throws Exception {

        
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
                    for (int i = 0; i < 10000; ++i) {
                        HelloWorldABCommand command = new HelloWorldABCommand(client);
                        try {
                            String result = command.execute();
                            System.out.println("Result " + i + " is " + result);
                            Thread.sleep(10);
                        } catch (HystrixRuntimeException e) {
                            System.out.println("Command " + i + " failed " + e.getMessage());
                        }
                    }
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

        Server server = new Server(8085);
        ServletContextHandler servletContextHandler = new ServletContextHandler(server, "/", true,
                false);
        servletContextHandler.addServlet(HystrixMetricsStreamServlet.class, "/hystrix.stream");
        server.start();
        server.join();

        // AtomicInteger total = new AtomicInteger(0);
        // int sseCount = 1000;
        // CountDownLatch latch = new CountDownLatch(sseCount);
        // for(int i = 0; i < sseCount; ++i) {
        // Executors.newSingleThreadExecutor().execute( new MyEventSource("ES" +
        // i, latch, 3, total) );
        // }
        // latch.await();
        // System.out.println("All Event Sources received updates; total " +
        // total.get() + ".");

        // WebTarget target =
        // client.target("http://127.0.0.1:8080/attribute/updates");
        // EventInput eventInput = target.request().get(EventInput.class);
        // while (!eventInput.isClosed()) {
        // final InboundEvent inboundEvent = eventInput.read();
        // if (inboundEvent == null) {
        // // connection has been closed
        // break;
        // }
        // System.out.println(inboundEvent.getName() + "; " +
        // inboundEvent.readData(String.class));
        // }

        // WebTarget target =
        // client.target("http://127.0.0.1:8080/attribute/updates");
        // EventSource eventSource = EventSource.target(target).build();
        // EventListener listener = new EventListener() {
        // @Override
        // public void onEvent(InboundEvent inboundEvent) {
        // System.out.println(inboundEvent.getName() + "; "
        // + inboundEvent.readData(String.class));
        // }
        // };
        // eventSource.register(listener);
        // eventSource.open();
        // Thread.sleep(30000);
        // eventSource.close();
    }
}
