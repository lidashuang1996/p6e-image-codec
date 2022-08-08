package club.p6e.image.encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.channels.FileChannel;

/**
 * @author lidashuang
 * @version 1.0
 */
public abstract class FileActuator implements Actuator {

    private final File file;

    public FileActuator(File file) {
        this.file = file;
    }

    @Override
    public void execute(String secret, Integer remark) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(this.file);
            FileChannel fileChannel = fileInputStream.getChannel();
            fileChannel.read();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
