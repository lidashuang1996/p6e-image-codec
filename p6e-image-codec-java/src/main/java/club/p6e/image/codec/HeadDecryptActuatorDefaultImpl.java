package club.p6e.image.codec;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lidashuang
 * @version 1.0
 */
public class HeadDecryptActuatorDefaultImpl implements HeadDecryptActuator {

    private boolean enable;
    private Integer id;
    private Integer version;
    private List<String> other;
    private String number;
    private SecretCompiler secretCompiler;

    public HeadDecryptActuatorDefaultImpl() {}

    public HeadDecryptActuatorDefaultImpl(SecretCompiler secretCompiler) {
        this.secretCompiler = secretCompiler;
    }

    private static byte[] readBytes(InputStream inputStream, int len) throws IOException {
        final byte[] bytes = new byte[len];
        if (inputStream.read(bytes) == len) {
            return bytes;
        } else {
            throw new IOException();
        }
    }

    private static int bytesToIntLittle(byte[] bytes) {
        return bytes[0] & 255 | (bytes[1] & 255) << 8 | (bytes[2] & 255) << 16 | (bytes[3] & 255) << 24;
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
    public void execute(InputStream inputStream) {
        try {
            final int r1 = inputStream.read();
            if (r1 == 0) {
                this.enable = false;
            } else {
                final int r2 = bytesToIntLittle(readBytes(inputStream, 4));
                final int r3 = bytesToIntLittle(readBytes(inputStream, 4));
                final int r4 = inputStream.read();
                if (r4 != 0) {
                    final List<String> list = new ArrayList<>();
                    final int listSize = bytesToIntLittle(readBytes(inputStream, 4));
                    for (int i = 0; i < listSize; i++) {
                        final int contentLength = bytesToIntLittle(readBytes(inputStream, 4));
                        list.add(new String(readBytes(inputStream, contentLength), StandardCharsets.UTF_8));
                    }
                    this.other = list;
                }
                final int r5 = bytesToIntLittle(readBytes(inputStream, 4));
                final String r6 = new String(readBytes(inputStream, r5), StandardCharsets.UTF_8);
                this.enable = true;
                this.id = r2;
                this.version = r3;
                this.number = r6;
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
            return secretCompiler.get(id, version).execute(number);
        }
    }

}
