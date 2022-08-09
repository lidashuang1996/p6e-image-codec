package club.p6e.image.codec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lidashuang
 * @version 1.0
 */
public class ActuatorModel implements Serializable {

    private final boolean enable;
    private final Integer id;
    private final Integer version;
    private final List<String> other;
    private final String number;

    private static byte[] readBytes(InputStream inputStream, int len) throws IOException {
        final byte[] bytes = new byte[len];
        if (inputStream.read(bytes) == len) {
            return bytes;
        } else {
            throw new IOException();
        }
    }

    private static byte[] intToBytesLittle(int value) {
        return new byte[]{(byte)(value & 255), (byte)(value >> 8 & 255), (byte)(value >> 16 & 255), (byte)(value >> 24 & 255)};
    }

    private static int bytesToIntLittle(byte[] bytes) {
        return bytes[0] & 255 | (bytes[1] & 255) << 8 | (bytes[2] & 255) << 16 | (bytes[3] & 255) << 24;
    }

    public static ActuatorModel stream(InputStream inputStream) {
        try {
            final int r1 = inputStream.read();
            final int r2 = bytesToIntLittle(readBytes(inputStream, 4));
            final int r3 = bytesToIntLittle(readBytes(inputStream, 4));
            final int r4 = inputStream.read();
            if (r4 == 0) {
                final String number = new String(readBytes(inputStream, 32), StandardCharsets.UTF_8);
                return new ActuatorModel(r1 != 0, r2, r3, number);
            } else {
                final List<String> list = new ArrayList<>();
                final int size = bytesToIntLittle(readBytes(inputStream, 4));
                for (int i = 0; i < size; i++) {
                    final int len = bytesToIntLittle(readBytes(inputStream, 4));
                    list.add(new String(readBytes(inputStream, len), StandardCharsets.UTF_8));
                }
                final String number = new String(readBytes(inputStream, 32), StandardCharsets.UTF_8);
                return new ActuatorModel(r1 != 0, r2, r3, list, number);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ActuatorModel(boolean enable, Integer id, Integer version, String number) {
        this.enable = enable;
        this.id = id;
        this.version = version;
        this.other = null;
        this.number = number;
    }

    public ActuatorModel(boolean enable, Integer id, Integer version, List<String> other, String number) {
        this.enable = enable;
        this.id = id;
        this.version = version;
        this.other = other;
        this.number = number;
    }

    public boolean isEnable() {
        return enable;
    }

    public Integer getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public List<String> getOther() {
        return other;
    }

    public String getNumber() {
        return number;
    }

    public byte[] toBytes() {
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
                byteArrayOutputStream.write(number.getBytes(StandardCharsets.UTF_8));
                byteArrayOutputStream.flush();
                return byteArrayOutputStream.toByteArray();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
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
            return new byte[] { 0 };
        }
    }
}
