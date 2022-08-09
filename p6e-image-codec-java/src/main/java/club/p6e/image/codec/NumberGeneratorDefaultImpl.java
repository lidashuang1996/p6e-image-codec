package club.p6e.image.codec;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;

/**
 * @author lidashuang
 * @version 1.0
 */
public class NumberGeneratorDefaultImpl extends NumberGenerator {

    @Override
    public String execute() {
        return DigestUtils.md5Hex(UUID.randomUUID().toString().replaceAll("-", ""));
    }


    public static void main(String[] args) {
        System.out.println(DigestUtils.md5Hex("5f8a7291f2b74bae8ff942858e26c074"));
    }
}
