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

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.media.sse.SseFeature;

import myStuff.rest.providers.AttributeValueListener;
import myStuff.rest.providers.AttributeValueProvider;

@Singleton
@Path("attribute/updates")
public class SseBroadcastAttributeUpdates implements AttributeValueListener {

	private final AttributeValueProvider provider;
	private final SseBroadcaster broadcaster = new SseBroadcaster();

	@Inject
	public SseBroadcastAttributeUpdates(AttributeValueProvider provider) {
		this.provider = provider;
		provider.addListenner(this);

	}

	@GET
	@Produces(SseFeature.SERVER_SENT_EVENTS)
	public EventOutput listenToBroadcast() {
		final EventOutput eventOutput = new EventOutput();
		broadcaster.add(eventOutput);
		return eventOutput;
	}

	@Override
	public void update(String attribute, String value) {
		OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
		OutboundEvent event = eventBuilder.name("message")
				.mediaType(MediaType.TEXT_PLAIN_TYPE)
				.data(String.class, attribute + " updated to " + value).build();
		broadcaster.broadcast(event);
	}
}
