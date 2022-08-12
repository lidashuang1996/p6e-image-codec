package club.p6e.image.codec;

import java.util.List;

/**
 * 密钥生成编译器
 * @author lidashuang
 * @version 1.0
 */
public interface SecretGeneratorCompiler {

    /**
     * 获取编号信息
     */
    public int id();

    /**
     * 获取版本信息
     */
    public int version();

    /**
     * 获取其他参数
     */
    public List<String> other();

    /**
     * 执行密钥生成器
     * @param number 序号
     * @return 密钥
     */
    public String execute(String number);

}
