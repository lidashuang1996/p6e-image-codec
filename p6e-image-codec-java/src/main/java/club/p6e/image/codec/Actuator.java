package club.p6e.image.codec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
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
     * @param fileInputStream  待加密的文件
     * @param fileOutputStream 加密后的文件
     * @param secret           密钥
     * @throws Exception 加密过程出现的异常
     */
    protected static void encrypt(FileInputStream fileInputStream, FileOutputStream fileOutputStream, String secret) throws Exception {
        final Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secret.getBytes(), KEY_ALGORITHM));
        stream(cipher, fileInputStream, fileOutputStream);
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
     * @param fileInputStream  待解密的文件
     * @param fileOutputStream 解密后的文件
     * @param secret           密钥
     * @throws Exception 解密过程出现的异常
     */
    protected static void decrypt(FileInputStream fileInputStream, FileOutputStream fileOutputStream, String secret) throws Exception {
        final Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secret.getBytes(), KEY_ALGORITHM));
        stream(cipher, fileInputStream, fileOutputStream);
    }

    /**
     * 流处理
     *
     * @param cipher           密钥对象
     * @param fileInputStream  输入的文件
     * @param fileOutputStream 输出的文件
     * @throws Exception 流处理过程出现的异常
     */
    private static void stream(Cipher cipher, FileInputStream fileInputStream, FileOutputStream fileOutputStream) throws Exception {
        CipherInputStream cipherInputStream = null;
        try {
            cipherInputStream = new CipherInputStream(fileInputStream, cipher);
            int rLength;
            byte[] bytes = new byte[1024];
            while ((rLength = cipherInputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, rLength);
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
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 执行解密
     *
     * @param model  处理器模型
     * @param secret 密钥
     */
    public abstract void executeEncrypt(String number, PasswordGenerator passwordGenerator) throws Exception;

    /**
     * 执行解加密
     *
     * @param passwordGenerator 密码生成器
     */
    public abstract void executeDecrypt(PasswordGenerator passwordGenerator) throws Exception;
}
