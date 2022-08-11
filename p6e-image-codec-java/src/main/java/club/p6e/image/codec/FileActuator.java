package club.p6e.image.codec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 文件解密加密执行器
 * 这个文件解密加密执行器是对解密加密执行器的实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class FileActuator extends Actuator {

    /**
     * 输入的文件
     */
    private final File inputFile;

    /**
     * 输出的文件
     */
    private final File outputFile;

    /**
     * 构造方法初始化
     * @param inputFile 输入的文件
     * @param outputFile 输出的文件
     */
    public FileActuator(File inputFile, File outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    /**
     * 验证是否是文件/文件的路径等
     */
    private void fileVerification() {
        if (!inputFile.exists()
                || !inputFile.isFile()) {
            throw new RuntimeException(this.getClass() + " input file object does not exist or is not a file.");
        }
        if (outputFile.exists()) {
            throw new RuntimeException(this.getClass() + " output file exists.");
        }
    }

    @Override
    public void performEncryption(HeadEncryptActuator headEncryptActuator) throws Exception {
        this.fileVerification();
        final FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        final String secret = headEncryptActuator.secret();
        headEncryptActuator.execute(fileOutputStream);
        fileOutputStream.flush();
        encrypt(new FileInputStream(inputFile), fileOutputStream, secret);
    }

    @Override
    public void performDecryption(HeadDecryptActuator headDecryptActuator) throws Exception {
        this.fileVerification();
        final FileInputStream fileInputStream = new FileInputStream(inputFile);
        headDecryptActuator.execute(fileInputStream);
        final String secret = headDecryptActuator.secret();
        decrypt(fileInputStream, new FileOutputStream(outputFile), secret);
    }

}
