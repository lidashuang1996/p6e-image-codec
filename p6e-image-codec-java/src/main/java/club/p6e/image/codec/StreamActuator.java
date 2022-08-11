package club.p6e.image.codec;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 流解密加密执行器
 * 这个流解密加密执行器是对解密加密执行器的实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class StreamActuator extends Actuator {

    /**
     * 输入的流
     */
    private final InputStream inputStream;

    /**
     * 输出的流
     */
    private final OutputStream outputStream;

    /**
     * 构造方法初始化
     *
     * @param inputStream  输入的流
     * @param outputStream 输出的流
     */
    public StreamActuator(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void performEncryption(HeadEncryptActuator headEncryptActuator) throws Exception {
        final String secret = headEncryptActuator.secret();
        headEncryptActuator.execute(outputStream);
        outputStream.flush();
        encrypt(inputStream, outputStream, secret);
    }

    @Override
    public void performDecryption(HeadDecryptActuator headDecryptActuator) throws Exception {
        headDecryptActuator.execute(inputStream);
        final String secret = headDecryptActuator.secret();
        decrypt(inputStream, outputStream, secret);
    }
}

