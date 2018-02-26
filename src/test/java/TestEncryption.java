import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

public class TestEncryption {
    private static final String ALGO = "AES/ECB/PKCS5Padding";
    private static final byte[] keyValue =
            new byte[]{'i', '2', 'e', 'B', 'e', 'Y', 't', 'R', 'e', 'c', 'r', 'f', 't', 'w', 'e', 'P'};

    /**
     * Encrypt a string with AES algorithm.
     *
     * @param data is a string
     * @return the encrypted string
     */
    public static String encrypt(String data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encVal);
    }

    /**
     * Decrypt a string with AES algorithm.
     *
     * @param encryptedData is a string
     * @return the decrypted string
     */
    public static String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue);
    }

    /**
     * Generate a new encryption key.
     */
    private static Key generateKey() {
        return new SecretKeySpec(keyValue, "AES");
    }

    public static void main(String[] args) throws Exception {
        String out = encrypt("Hola mundo!\nSegunda linea.");
        String in = decrypt(out);
        System.out.println(out + " = " + in);
    }

}
