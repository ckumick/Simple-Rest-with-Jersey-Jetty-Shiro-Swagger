package myStuff.rest.shiro;

import org.apache.shiro.web.env.DefaultWebEnvironment;

public class ShiroEnvironment extends DefaultWebEnvironment {

	public ShiroEnvironment() {
		super();
		setFilterChainResolver(ShiroHelper.getFilterChainResolver());
		setSecurityManager(ShiroHelper.getSecurityManager());
	}

}