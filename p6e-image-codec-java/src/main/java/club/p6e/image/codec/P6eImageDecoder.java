package club.p6e.image.codec;

/**
 * 解密
 *
 * @author lidashuang
 * @version 1.0
 */
public final class P6eImageDecoder {

    /**
     * 回调函数
     */
    public interface Callback {

        /**
         * 执行回调
         */
        public void execute(SecretCompiler secretCompiler);

    }

    /**
     * 密钥编译器
     */
    private static volatile SecretCompiler SECRET_COMPILER = new SecretCompilerDefaultImpl();

    /**
     * 获取密钥编译器
     *
     * @return 密钥编译器
     */
    public static SecretCompiler getSecretCompiler() {
        return SECRET_COMPILER;
    }

    /**
     * 设置密钥编译器
     *
     * @param sc 密钥编译器
     */
    public static void setSecretCompiler(SecretCompiler sc) {
        SECRET_COMPILER = sc;
    }


    /**
     * 初始化
     */
    public static void init() {
        SECRET_COMPILER.install(new SecretGeneratorCompilerDefaultImpl1());
        SECRET_COMPILER.install(new SecretGeneratorCompilerDefaultImpl2());
    }

    /**
     * 回调函数初始化
     */
    public static void init(Callback callback) {
        callback.execute(SECRET_COMPILER);
    }

    /**
     * 执行解密的操作
     * @param actuator 加密解密执行器
     * @throws Exception 执行解密的操作过程出现的异常
     */
    public static void run(Actuator actuator) throws Exception {
        actuator.performDecryption(new HeadDecryptActuatorDefaultImpl(SECRET_COMPILER));
    }
}
