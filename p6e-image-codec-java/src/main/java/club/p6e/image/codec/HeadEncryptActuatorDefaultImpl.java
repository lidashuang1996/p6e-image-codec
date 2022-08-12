package club.p6e.image.codec;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author lidashuang
 * @version 1.0
 */
public class HeadEncryptActuatorDefaultImpl implements HeadEncryptActuator {

    private final boolean enable;
    private Integer id;
    private Integer version;
    private List<String> other;
    private String number;
    private SecretCompiler secretCompiler;

    private static byte[] intToBytesLittle(int value) {
        return new byte[]{(byte) (value & 255), (byte) (value >> 8 & 255), (byte) (value >> 16 & 255), (byte) (value >> 24 & 255)};
    }

    public HeadEncryptActuatorDefaultImpl() {
        this.enable = false;
    }

    public HeadEncryptActuatorDefaultImpl(String number, SecretCompiler secretCompiler) {
        this.enable = true;
        this.number = number;
        this.secretCompiler = secretCompiler;
    }

    @Override
    public boolean isEnable() {
        return enable;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public List<String> getOther() {
        return other;
    }

    @Override
    public String getNumber() {
        return number;
    }

    @Override
    public void execute(OutputStream outputStream) {
        try {
            if (enable) {
                ByteArrayOutputStream byteArrayOutputStream = null;
                try {
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    byteArrayOutputStream.write(1);
                    byteArrayOutputStream.write(intToBytesLittle(id));
                    byteArrayOutputStream.write(intToBytesLittle(version));
                    if (other == null) {
                        byteArrayOutputStream.write(0);
                    } else {
                        byteArrayOutputStream.write(1);
                        byteArrayOutputStream.write(intToBytesLittle(other.size()));
                        for (final String content : other) {
                            final byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
                            byteArrayOutputStream.write(intToBytesLittle(contentBytes.length));
                            byteArrayOutputStream.write(contentBytes);
                        }
                    }
                    final byte[] numberBytes = number.getBytes(StandardCharsets.UTF_8);
                    byteArrayOutputStream.write(intToBytesLittle(numberBytes.length));
                    byteArrayOutputStream.write(numberBytes);
                    byteArrayOutputStream.flush();
                    outputStream.write(byteArrayOutputStream.toByteArray());
                } finally {
                    try {
                        if (byteArrayOutputStream != null) {
                            byteArrayOutputStream.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                outputStream.write(0);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String secret() {
        if (secretCompiler == null) {
            return null;
        } else {
            final SecretGeneratorCompiler compiler = secretCompiler.get();
            this.id = compiler.id();
            this.version = compiler.version();
            this.other = compiler.other();
            return compiler.execute(number);
        }
    }

}
