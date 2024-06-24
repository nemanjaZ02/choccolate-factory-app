package jwt;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import beans.Customer;
import beans.User;
import enums.Role;

import com.nimbusds.jwt.JWTClaimsSet.Builder;

import java.util.Date;
import java.util.Map;

import javax.ws.rs.core.Response;

public class JwtUtils {

    public static String generateToken(User user, String secretKey) throws JOSEException {
        JWSSigner signer = new MACSigner(secretKey);

        JWTClaimsSet jwtClaims = new JWTClaimsSet.Builder()
                .claim("user", user) 
                .claim("userId", String.valueOf(user.getId()))
                .claim("role", user.getRole())
                .expirationTime(new Date(System.currentTimeMillis() + 3600_000)) 
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), jwtClaims);
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }
    
    private static String convertAuthorizationHeader(String authorizationHeader)
    {
    	String jwtToken = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            return jwtToken;
        } else {
            return "";
        }
    }
    
    public static JWTClaimsSet validateToken(String token) throws ParseException, JOSEException {
        JWSObject jwsObject;
		try {
			jwsObject = JWSObject.parse(token);
			JWSVerifier verifier = new MACVerifier(JwtConstants.SECRET_KEY);
			if (!jwsObject.verify(verifier)) {
	            throw new JOSEException("Token signature is invalid");
	        }

	        return JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

        return null;
    }
    
    public static boolean isManager(String authorizationHeader) throws ParseException {
    
		String jwt = convertAuthorizationHeader(authorizationHeader);
		
		if(jwt == "")
		{
			return false;
		}
		
        try {
            SignedJWT signedJWT = SignedJWT.parse(jwt);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            String role = (String) claimsSet.getClaim("role");
            return role != null && role.equals(Role.MANAGER.toString());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean isAdministrator(String authorizationHeader) throws ParseException {
        
		String jwt = convertAuthorizationHeader(authorizationHeader);
		
		if(jwt == "")
		{
			return false;
		}
		
        try {
            SignedJWT signedJWT = SignedJWT.parse(jwt);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            String role = (String) claimsSet.getClaim("role");
            return role != null && role.equals(Role.ADMIN.toString());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean isEmployee(String authorizationHeader) throws ParseException {
        
		String jwt = convertAuthorizationHeader(authorizationHeader);
		
		if(jwt == "")
		{
			return false;
		}
		
        try {
            SignedJWT signedJWT = SignedJWT.parse(jwt);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            String role = (String) claimsSet.getClaim("role");
            return role != null && role.equals(Role.EMPLOYEE.toString());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean isCustomer(String authorizationHeader) throws ParseException {
        
		String jwt = convertAuthorizationHeader(authorizationHeader);
		
		if(jwt == "")
		{
			return false;
		}
		
        try {
            SignedJWT signedJWT = SignedJWT.parse(jwt);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            String role = (String) claimsSet.getClaim("role");
            return role != null && role.equals(Role.CUSTOMER.toString());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static int getCustomerId(String authorizationHeader) {
    	String jwt = convertAuthorizationHeader(authorizationHeader);
		
		if(jwt == "")
		{
			return 0;
		}
		
        try {
            SignedJWT signedJWT = SignedJWT.parse(jwt);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            String id = (String) claimsSet.getClaim("userId");
            return Integer.valueOf(id);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
