package club.p6e.image.codec;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 密钥编译器的默认的实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class SecretCompilerDefaultImpl implements SecretCompiler {

    /**
     * 密钥生成编译器-LIST
     */
    private final List<SecretGeneratorCompiler> LIST = new ArrayList<>();

    /**
     * 密钥生成编译器-MAP
     */
    private final Map<String, SecretGeneratorCompiler> MAP = new Hashtable<>();

    /**
     * 读取名称
     *
     * @param compiler 密钥生成编译器
     * @return 读取的名称信息
     */
    private String key(SecretGeneratorCompiler compiler) {
        return key(compiler.id(), compiler.version());
    }

    /**
     * 读取名称
     *
     * @return 读取的名称信息
     */
    private String key(int id, int version) {
        return "ID@" + id + "_VERSION@" + version;
    }

    @Override
    public SecretGeneratorCompiler get() {
        if (LIST.size() <= 0) {
            throw new RuntimeException();
        } else {
            return LIST.get(ThreadLocalRandom.current().nextInt(LIST.size()));
        }
    }

    @Override
    public SecretGeneratorCompiler get(int id, int version) {
        return MAP.get(key(id, version));
    }

    @Override
    public List<SecretGeneratorCompiler> list() {
        return LIST;
    }

    @Override
    public Map<String, SecretGeneratorCompiler> map() {
        return MAP;
    }

    @Override
    public void install(SecretGeneratorCompiler compiler) {
        LIST.add(compiler);
        MAP.put(key(compiler), compiler);
    }

    @Override
    public void uninstall(SecretGeneratorCompiler compiler) {
        LIST.remove(compiler);
        MAP.remove(key(compiler));
    }

}
