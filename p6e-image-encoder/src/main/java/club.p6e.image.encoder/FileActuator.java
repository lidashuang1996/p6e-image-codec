package club.p6e.image.encoder;

import org.apache.commons.codec.binary.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author lidashuang
 * @version 1.0
 */
public abstract class FileActuator extends Actuator {

    private final File file;

    public FileActuator(File file) {
        this.file = file;
    }

    @Override
    public void execute(String secret, Integer remark) {
        FileInputStream fileInputStream = null;
        FileChannel fileChannel = null;
        try {
            fileInputStream = new FileInputStream(this.file);
            fileChannel = fileInputStream.getChannel();
            fileChannel.read();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (fileChannel != null) {
                    fileChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
