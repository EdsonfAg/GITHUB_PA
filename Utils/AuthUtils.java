package Utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AuthUtils {
	public static String hashear(String textoPlano) {
	    try {
	        MessageDigest md = MessageDigest.getInstance("SHA-256");
	        byte[] hash = md.digest(textoPlano.getBytes(StandardCharsets.UTF_8));
	        return String.format("%064x", new BigInteger(1, hash)); // HEXADECIMAL 🔥
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("Error al hashear contraseña", e);
	    }
	}


    public static boolean verificar(String entrada, String hashGuardado) {
        return hashear(entrada).equals(hashGuardado);
    }
}
