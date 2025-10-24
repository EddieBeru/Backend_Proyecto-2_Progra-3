package model;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.*;
import java.util.Base64;

public final class PasswordUtil {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERACIONES = 10;
    private static final int KEY_LENGTH_BITS = 256;

    private static final byte[] SALT = "YonporkCompanyXd".getBytes();
    private PasswordUtil() {}

    public static String hashPassword(char[] password) {
        byte[] sal = generateSalt();
        byte[] hash = pbkdf2(password, sal, ITERACIONES, KEY_LENGTH_BITS);
        // Limpieza mínima
        java.util.Arrays.fill(password, '\0');
        return String.format("%s|%d|%s|%s",
                ALGORITHM,
                ITERACIONES,
                Base64.getEncoder().encodeToString(sal),
                Base64.getEncoder().encodeToString(hash));
    }

    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public static boolean verificar(char[] password, String almacenado) {
        try {
            String[] partes = almacenado.split("\\|");
            if (partes.length != 4) return false;
            int iters = Integer.parseInt(partes[1]);
            byte[] sal = Base64.getDecoder().decode(partes[2]);
            byte[] hashEsperado = Base64.getDecoder().decode(partes[3]);

            byte[] hashCalc = pbkdf2(password, sal, iters, hashEsperado.length * 8);
            java.util.Arrays.fill(password, '\0');
            return MessageDigest.isEqual(hashEsperado, hashCalc);
        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] pbkdf2(char[] password, byte[] sal, int iter, int keyLengthBits) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, sal, iter, keyLengthBits);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
            return skf.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new IllegalStateException("Error derivando clave", e);
        }
    }

    public static boolean ValidarClave(String Clave) {
        if (Clave.length() < 8) return false;
        if (Clave.toLowerCase().equals(Clave)) return false;
        if (Clave.toUpperCase().equals(Clave)) return false;
        if (!Clave.matches(".*\\d.*")) return false;
        return true;
    }

}
