package club.p6e.image.encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.Provider;
import java.security.SecureRandom;

/**
 * @author lidashuang
 * @version 1.0
 */
public abstract class Actuator {

    private static final int KEY_SIZE = 128;
    private static final String KEY_ALGORITHM = "AES";
    private static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    public SecretKey getSecretKey(String secret) throws Exception {
        final KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom random = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM);
        random.setSeed(secret.getBytes(StandardCharsets.UTF_8));
        keyGenerator.init(KEY_SIZE, random);
        return keyGenerator.generateKey();
    }


    public byte[] encrypt(byte[] data, String secret) throws Exception {
        final Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(secret));
        return cipher.doFinal(data);
    }

    public byte[] decrypt(byte[] data, String secret) throws Exception {
        final Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(secret));
        return cipher.doFinal(data);
    }

    public abstract void execute(String secret, Integer remark);
}
