package myStuff.rest.app;

import myStuff.rest.providers.AttributeValueProvider;
import myStuff.rest.providers.AttributeValueProviderImpl;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class MyBinder extends AbstractBinder {

	@Override
	protected void configure() {
		bind(new AttributeValueProviderImpl()).to(AttributeValueProvider.class);
	}

}
