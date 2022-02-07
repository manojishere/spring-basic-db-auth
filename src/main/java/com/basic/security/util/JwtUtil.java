package com.basic.security.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {
	
	private Logger logger = LoggerFactory.getLogger( JwtUtil.class );
	
	private String SECRET_KEY = "secret";
	
	//retrieve username from jwt token
	public String extractUsername(String token) {
		return extractClaim( token, Claims::getSubject );
	}
	
	public Date extractExpiration( String token ) {
		return extractClaim( token, Claims::getExpiration );
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
		    //for retrieveing any information from token we will need the secret key
	public Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey( SECRET_KEY ).parseClaimsJws(token).getBody();
	}
	
	//check if the token has expired
	public  Boolean isTokenExpired(String token) {
		return extractExpiration( token ).before( new Date() );
	}	
	
	public String generateToken( UserDetails userDetails ) {
		logger.debug(" generateToken : userDetails : " + userDetails.getUsername());
		Map<String, Object> claims  = new HashMap<>();
		return createToken( claims, userDetails.getUsername() );
		
	}
	
	public String generateToken( String authorities, String username ) {
		logger.info(" generateToken username : authorities :: " + username + " : " + authorities);
		Map<String, Object> claims  = new HashMap<>();
		return createToken( authorities, username );
		
	}	
	
	public String createToken( String authorities, String subject ) {
		return Jwts.builder().claim( "AUTHORITIES_KEY",  authorities ).setSubject( subject ).setIssuedAt( new Date( System.currentTimeMillis() ) )
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}	
 	
	
	public String createToken( Map<String, Object> claims, String subject ) {
		return Jwts.builder().setClaims( claims ).setSubject( subject ).setIssuedAt( new Date( System.currentTimeMillis() ) )
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}
	
	//validate token
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		logger.debug("validateToken username : " + username );
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}	
	
}


