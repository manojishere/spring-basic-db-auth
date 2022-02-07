package com.basic.security.resource;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.basic.security.model.AuthenticationRequest;
import com.basic.security.model.AuthenticationResponse;
import com.basic.security.service.MyUserDetailsService;
import com.basic.security.util.JwtUtil;

@RestController
public class HomeResource {
	
	private Logger logger = LoggerFactory.getLogger( HomeResource.class );
	
	@Autowired
	AuthenticationManager authenticationManager; 
	
	@Autowired
	MyUserDetailsService userDetailsService;
	
	@Autowired
	JwtUtil jwtTokenUtil;	
	/*
	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken( 
			@RequestBody AuthenticationRequest authRequest ) throws Exception{
		try {
			authenticationManager.authenticate( 
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		}catch( BadCredentialsException bce ) {
			logger.error( "BadCredentialsException Incorrect username and password : " + bce );
		}catch( Exception e) {
			logger.error( "Exception : " + e );
		}
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername( 
				authRequest.getUsername() );
		final String jwt = jwtTokenUtil.generateToken( userDetails );
		return ResponseEntity.ok( new AuthenticationResponse( jwt ) );
	}
	*/
	/*
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		logger.debug("HomeResourece authenticate :: " + authenticationRequest.getUsername() + " : " + authenticationRequest.getPassword());

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(token));
	}
	*/
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken( 
			@RequestBody AuthenticationRequest authRequest ) throws Exception{
		Authentication authentication;
		try {
			authentication = authenticationManager.authenticate( 
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		}catch( BadCredentialsException bce ) {
			logger.error( "BadCredentialsException Incorrect username and password : " + bce );
			throw new Exception( "Incorrect username and password : ");
		}catch( Exception e) {
			logger.error( "Exception : " + e );
			throw new Exception( "Incorrect username and password");
		}
		getUserDetails( authentication );
		/*
		final UserDetails userDetails = userDetailsService.loadUserByUsername( 
				authRequest.getUsername() );
		final String jwt = jwtTokenUtil.generateToken( userDetails );
		*/
		final String jwt = generateToken( authentication );
		return ResponseEntity.ok( new AuthenticationResponse( jwt ) );
	}
	
	private String generateToken( Authentication authentication ) {
		logger.info(" generateToken : " );
        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        final String username = authentication.getName();
        logger.info(" generateToken username : authorities :: " + username + " : " + authorities);
        final String jwt = jwtTokenUtil.createToken( authorities,  username);
        logger.info(" generateToken : jwt :: " + jwt );
		return jwt;
	}
	
	
	private void getUserDetails( Authentication authentication) {
		
        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));		
		
		logger.info(" getUserDetails : " + authorities );
	}	
	
	private void authenticate(String userName, String password) throws Exception {
		try {
			logger.debug("HomeResourece authenticate --> " + userName );
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	@GetMapping("/")
	public String home() {
		return "welcome home";
	}
	
	@GetMapping("/user")
	public String user() {
		return "welcome user";
	}
	
	@GetMapping("/admin")
	public String admin() {
		return "welcome admin";
	}
	

}
