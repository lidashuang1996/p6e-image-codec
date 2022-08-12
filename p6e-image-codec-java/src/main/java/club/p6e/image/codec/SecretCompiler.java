package club.p6e.image.codec;

import java.util.List;
import java.util.Map;

/**
 * 密钥编译器
 * @author lidashuang
 * @version 1.0
 */
public interface SecretCompiler {

    /**
     * 获取随机的密钥生成编译器
     * @return 密钥生成编译器
     */
    public SecretGeneratorCompiler get();

    /**
     * 获取制定的密钥生成编译器
     * @param id 序号
     * @param version 版本号
     * @return 密钥生成编译器
     */
    public SecretGeneratorCompiler get(int id, int version);

    /**
     * 获取密钥生成编译器
     * @return 密钥生成编译器列表
     */
    public List<SecretGeneratorCompiler> list();

    /**
     * 获取密钥生成编译器
     * @return 密钥生成编译器字典
     */
    public Map<String, SecretGeneratorCompiler> map();

    /**
     * 添加密钥生成编译器
     * @param compiler 密钥生成编译器对象
     */
    public void install(SecretGeneratorCompiler compiler);

    /**
     * 卸载密钥生成编译器
     * @param compiler 密钥生成编译器对象
     */
    public void uninstall(SecretGeneratorCompiler compiler);

}


