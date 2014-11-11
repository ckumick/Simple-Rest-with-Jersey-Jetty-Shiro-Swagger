package myStuff.rest.shiro;

import javax.servlet.annotation.WebFilter;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

@WebFilter("/*")
public class MyFilter extends BasicHttpAuthenticationFilter {
}