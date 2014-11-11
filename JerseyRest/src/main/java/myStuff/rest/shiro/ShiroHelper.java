package myStuff.rest.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

public class ShiroHelper {

	private static SecurityManager securityManager = null;
	private static FilterChainResolver filterChainResolver = null;

	public static SecurityManager getSecurityManager() {
		if (securityManager == null) {
			Realm realm = new MyRealm();

			securityManager = new DefaultWebSecurityManager(realm);
		}
		return securityManager;
	}

	public static FilterChainResolver getFilterChainResolver() {
		if (filterChainResolver == null) {
			BasicHttpAuthenticationFilter authcBasic = new MyFilter();
			authcBasic.setEnabled(true);

			FilterChainManager fcMan = new DefaultFilterChainManager();
			fcMan.addFilter("authcBasic[permissive]", authcBasic);
			fcMan.createChain("/**", "authcBasic[permissive]");

			PathMatchingFilterChainResolver resolver = new PathMatchingFilterChainResolver();
			resolver.setFilterChainManager(fcMan);
			filterChainResolver = resolver;
		}
		return filterChainResolver;
	}

}