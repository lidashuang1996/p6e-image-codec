package club.p6e.image.codec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 编码解码执行器
 *
 * @author lidashuang
 * @version 1.0
 */
public abstract class Actuator {

    /**
     * AES 加密算法
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * 默认的填充算法
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * 加密
     *
     * @param data   待加密的内容
     * @param secret 密钥
     * @return 加密的内容
     * @throws Exception 加密过程出现的异常
     */
    protected static byte[] encrypt(byte[] data, String secret) throws Exception {
        final Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secret.getBytes(), KEY_ALGORITHM));
        return cipher.doFinal(data);
    }

    /**
     * 加密
     *
     * @param inputStream  待加密的文件
     * @param outputStream 加密后的文件
     * @param secret       密钥
     * @throws Exception 加密过程出现的异常
     */
    protected static void encrypt(InputStream inputStream, OutputStream outputStream, String secret) throws Exception {
        final Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secret.getBytes(), KEY_ALGORITHM));
        stream(cipher, inputStream, outputStream);
    }

    /**
     * 解密
     *
     * @param data   待解密的内容
     * @param secret 密钥
     * @return 解密的内容
     * @throws Exception 解密过程出现的异常
     */
    protected static byte[] decrypt(byte[] data, String secret) throws Exception {
        final Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secret.getBytes(), KEY_ALGORITHM));
        return cipher.doFinal(data);
    }

    /**
     * 解密
     *
     * @param inputStream  待解密的文件
     * @param outputStream 解密后的文件
     * @param secret       密钥
     * @throws Exception 解密过程出现的异常
     */
    protected static void decrypt(InputStream inputStream, OutputStream outputStream, String secret) throws Exception {
        final Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secret.getBytes(), KEY_ALGORITHM));
        stream(cipher, inputStream, outputStream);
    }

    /**
     * 流处理
     *
     * @param cipher       密钥对象
     * @param inputStream  输入的文件
     * @param outputStream 输出的文件
     * @throws Exception 流处理过程出现的异常
     */
    private static void stream(Cipher cipher, InputStream inputStream, OutputStream outputStream) throws Exception {
        CipherInputStream cipherInputStream = null;
        try {
            cipherInputStream = new CipherInputStream(inputStream, cipher);
            int rLength;
            byte[] bytes = new byte[1024];
            while ((rLength = cipherInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, rLength);
            }
        } finally {
            try {
                if (cipherInputStream != null) {
                    cipherInputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 执行加密
     *
     * @param headEncryptActuator 头部加密执行器
     */
    public abstract void performEncryption(HeadEncryptActuator headEncryptActuator) throws Exception;

    /**
     * 执行解密
     *
     * @param headEncryptActuator 头部解密执行器
     */
    public abstract void performDecryption(HeadDecryptActuator headEncryptActuator) throws Exception;
}
