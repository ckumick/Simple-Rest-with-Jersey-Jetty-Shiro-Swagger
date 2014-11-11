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