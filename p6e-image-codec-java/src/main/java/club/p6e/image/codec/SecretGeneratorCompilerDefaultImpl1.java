package club.p6e.image.codec;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

/**
 * 密钥生成编译器默认实现-1
 * @author lidashuang
 * @version 1.0
 */
public class SecretGeneratorCompilerDefaultImpl1 implements SecretGeneratorCompiler {

    private static final int ID = 1;
    private static final int VERSION = 1;
    private static final List<String> OTHER = null;

    @Override
    public int id() {
        return ID;
    }

    @Override
    public int version() {
        return VERSION;
    }

    @Override
    public List<String> other() {
        return OTHER;
    }

    @Override
    public String execute(String number) {
        return DigestUtils.md5Hex(number);
    }

}
