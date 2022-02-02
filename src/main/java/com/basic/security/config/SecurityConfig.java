package com.basic.security.config;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

import com.basic.security.filters.JwtRequestFilter;

// https://github.com/koushikkothagal/spring-security-jwt

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private Logger logger = LoggerFactory.getLogger( SecurityConfig.class );

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	JwtRequestFilter jwtRequestFilter;

	/*
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		logger.debug("inside configure(AuthenticationManagerBuilder auth)");
		try {
			auth.userDetailsService( userDetailsService );
		}catch( Exception e ) {
			logger.error( "configure : " + e);
			throw e;
		}
	}
	*/
	
	@Override
	protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
		logger.info("overridden configure called for AuthenticationManagerBuilder...");
		authManagerBuilder.authenticationProvider(activeDirectoryLdapAuthenticationProvider())
				.userDetailsService( userDetailsService );
	}	

	/*
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/admin").hasRole("ADMIN")
			.antMatchers("/user").hasAnyRole("ADMIN", "USER")
			.antMatchers("/").permitAll()
			.and()
			.formLogin();
	}
	*/
	
	/*
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeRequests().antMatchers("/authenticate").permitAll()
			.anyRequest().authenticated()
			.and().sessionManagement()
			.sessionCreationPolicy( SessionCreationPolicy.STATELESS );
		http.addFilterBefore( jwtRequestFilter, UsernamePasswordAuthenticationFilter.class );
	}
	*/
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeRequests().antMatchers("/authenticate").permitAll()
			//.anyRequest().authenticated()
			.anyRequest().authenticated().and().httpBasic()
			.and().sessionManagement()
			.sessionCreationPolicy( SessionCreationPolicy.STATELESS );
		http.addFilterBefore( jwtRequestFilter, UsernamePasswordAuthenticationFilter.class );			
	}	
	
	/*
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManagerBean();
	}
	*/
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		logger.debug("AuthenticationManager called...");
		return new ProviderManager(Arrays.asList(activeDirectoryLdapAuthenticationProvider()));
	}	
	
	/*
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	*/
	
	@Bean
	public AuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
		logger.debug("activeDirectoryLdapAuthenticationProvider called...");
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

		return provider;
	}	
	
}
