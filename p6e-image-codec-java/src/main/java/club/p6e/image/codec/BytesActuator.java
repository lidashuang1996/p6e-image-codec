package club.p6e.image.codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 字节数组解密加密执行器
 * 这个字节数组解密加密执行器是对解密加密执行器的实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class BytesActuator extends Actuator {

    /**
     * 输入的 bytes
     */
    public byte[] input;

    /**
     * 输出的 bytes
     */
    private byte[] output;

    /**
     * 输入的方法
     *
     * @param input 输入的 bytes
     */
    public void input(byte[] input) {
        this.input = input;

        // 清空输出
        this.output = null;
    }

    /**
     * 输出的方法
     *
     * @return 输出的 bytes
     */
    public byte[] output() {
        return this.output;
    }

    @Override
    public void performEncryption(HeadEncryptActuator headEncryptActuator) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            final String secret = headEncryptActuator.secret();
            byteArrayOutputStream = new ByteArrayOutputStream();
            headEncryptActuator.execute(byteArrayOutputStream);
            byteArrayOutputStream.write(encrypt(input, secret));
            byteArrayOutputStream.flush();
            this.output = byteArrayOutputStream.toByteArray();

            // 清空输入
            this.input = null;
        } finally {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void performDecryption(HeadDecryptActuator headEncryptActuator) throws Exception {
        ByteArrayInputStream byteArrayInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(input);
            headEncryptActuator.execute(byteArrayInputStream);
            byteArrayOutputStream = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = byteArrayInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            final String secret = headEncryptActuator.secret();
            this.output = decrypt(byteArrayOutputStream.toByteArray(), secret);

            // 清空输入
            this.input = null;
        } finally {
            if (byteArrayInputStream != null) {
                try {
                    byteArrayInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
