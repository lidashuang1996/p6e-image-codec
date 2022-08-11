package club.p6e.image.codec;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;

/**
 * 序号生成器的默认实现
 * @author lidashuang
 * @version 1.0
 */
public class NumberGeneratorDefaultImpl implements NumberGenerator {

    @Override
    public String execute() {
        return DigestUtils.md5Hex(UUID.randomUUID().toString().replaceAll("-", ""));
    }

}
