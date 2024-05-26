package jwt;

import java.security.SecureRandom;
import java.util.Base64;

public class JwtConstants {
    public static final String SECRET_KEY = generateSecretKey();

    public static String getSecretKey() {
		return SECRET_KEY;
	}

	private static String generateSecretKey() {
        byte[] keyBytes = new byte[32]; 
        
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(keyBytes);


        return Base64.getEncoder().encodeToString(keyBytes);
    }
}