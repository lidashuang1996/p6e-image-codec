package club.p6e.image.codec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

/**
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

    public FileActuator(File inputFile, File outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public void fileVerification() {
        if (!inputFile.exists()
                || !inputFile.isFile()) {
            throw new RuntimeException(this.getClass() + " input file object does not exist or is not a file.");
        }
        if (outputFile.exists()) {
            throw new RuntimeException(this.getClass() + " output file exists.");
        }
    }

    @Override
    public void executeEncrypt(String number, PasswordGenerator passwordGenerator) throws Exception {
        this.fileVerification();
        final PasswordGenerator.Model model = passwordGenerator.execute(number);
        final FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        fileOutputStream.write(model.getActuatorModel().toBytes());
        fileOutputStream.flush();
        encrypt(new FileInputStream(inputFile), fileOutputStream, model.getSecret());
    }

    @Override
    public void executeDecrypt(PasswordGenerator passwordGenerator) throws Exception {
        this.fileVerification();
        final FileInputStream fileInputStream = new FileInputStream(inputFile);
        final ActuatorModel actuatorModel = ActuatorModel.stream(fileInputStream);
        decrypt(fileInputStream, new FileOutputStream(outputFile), passwordGenerator.execute(actuatorModel).getSecret());
    }

    public static byte[] hexToByte(String hex) {
        int byteLen = hex.length() / 2;
        byte[] ret = new byte[byteLen];

        for(int i = 0; i < byteLen; ++i) {
            int m = i * 2 + 1;
            int n = m + 1;
            int intVal = Integer.decode("0x" + hex.substring(i * 2, m) + hex.substring(m, n));
            ret[i] = (byte)intVal;
        }

        return ret;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        byte[] var2 = bytes;
        int var3 = bytes.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            byte aByte = var2[var4];
            String hex = Integer.toHexString(aByte & 255);
            if (hex.length() < 2) {
                sb.append(0);
            }

            sb.append(hex);
        }

        return sb.toString();
    }
}
