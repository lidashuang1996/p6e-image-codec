package club.p6e.image.codec;

import java.io.OutputStream;
import java.util.List;

/**
 * 头部加密执行器
 * @author lidashuang
 * @version 1.0
 */
public interface HeadEncryptActuator {

    /**
     * 获取是否加密
     * @return 是否加密
     */
    public boolean isEnable();

    /**
     * 获取编号
     * @return 编号
     */
    public Integer getId();

    /**
     * 获取版本号
     * @return 版本号
     */
    public Integer getVersion();

    /**
     * 获取其他参数
     * @return 其他参数
     */
    public List<String> getOther();

    /**
     * 获取序号
     * @return 序号
     */
    public String getNumber();

    /**
     * 执行写入加密头内容
     */
    public void execute(OutputStream outputStream);

    /**
     * 获取密钥
     * @return 密钥
     */
    public String secret();

}
