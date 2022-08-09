package club.p6e.image.codec;

/**
 * @author lidashuang
 * @version 1.0
 */
public final class P6eImageDecoderCore {

    private static volatile PasswordGenerator PASSWORD_GENERATOR = new PasswordGeneratorDefaultImpl();

    public static void setPasswordGenerator(PasswordGenerator passwordGenerator) {
        PASSWORD_GENERATOR = passwordGenerator;
    }

    public static PasswordGenerator getPasswordGenerator() {
        return PASSWORD_GENERATOR;
    }

    public static void run(Actuator actuator) throws Exception {
        actuator.executeDecrypt(PASSWORD_GENERATOR);
    }
}
