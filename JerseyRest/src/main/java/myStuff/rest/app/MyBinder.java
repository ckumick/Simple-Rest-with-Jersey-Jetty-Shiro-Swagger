package myStuff.rest.app;

import javax.inject.Singleton;

import myStuff.rest.providers.AttributeValueProvider;
import myStuff.rest.providers.AttributeValueProviderImpl;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class MyBinder extends AbstractBinder {

	@Override
	protected void configure() {
		bind(new AttributeValueProviderImpl()).to(AttributeValueProvider.class);
		
		bind(BasicHttpAuthenticationFilter.class).in(Singleton.class);
//		filter("/secure/*").through(BasicHttpAuthenticationFilter.class);
	}

}
