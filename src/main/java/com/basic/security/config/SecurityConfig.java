package com.basic.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

// https://github.com/koushikkothagal/spring-security-jwt

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private Logger logger = LoggerFactory.getLogger( SecurityConfig.class );

	  @Override
	  protected void configure(HttpSecurity http) throws Exception {

	    http
	      .authorizeRequests()
	        .antMatchers("/welcome").permitAll() // no auth requiered
	        .anyRequest().fullyAuthenticated() // auth required for every request.
	        .and()
	      .formLogin();
	  }


	
	@Bean
	public AuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
		logger.info("SecurityConfig AuthenticationProvider activeDirectoryLdapAuthenticationProvider()");
		ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider("uss.net",
				"ldaps://ldap-qts.uss.net");

		/*
		 * The LDAP filter string to search for the user being authenticated.
		 * Occurrences of {0} are replaced with the username@domain. Occurrences of {1}
		 * are replaced with the username only.
		 */
		provider.setSearchFilter("sAMAccountName={1}");
		provider.setConvertSubErrorCodesToExceptions(true);
		provider.setUseAuthenticationRequestCredentials(true);
		//provider.setUserDetailsContextMapper( myUserDetailsContextMapper );;

		return provider;
	}	
	
}
