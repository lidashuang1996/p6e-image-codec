package club.p6e.image.codec;

/**
 * @author lidashuang
 * @version 1.0
 */
public class P6eImageEncoderCore {

    private static volatile NumberGenerator NUMBER_GENERATOR = new NumberGeneratorDefaultImpl();

    private static volatile PasswordGenerator PASSWORD_GENERATOR = new PasswordGeneratorDefaultImpl();

    public static void setNumberGenerator(NumberGenerator numberGenerator) {
        NUMBER_GENERATOR = numberGenerator;
    }

    public static NumberGenerator getNumberGenerator() {
        return NUMBER_GENERATOR;
    }

    public static void setPasswordGenerator(PasswordGenerator passwordGenerator) {
        PASSWORD_GENERATOR = passwordGenerator;
    }

    public static PasswordGenerator getPasswordGenerator() {
        return PASSWORD_GENERATOR;
    }

    public static void run(Actuator actuator) throws Exception {
        actuator.executeEncrypt(NUMBER_GENERATOR.run(), PASSWORD_GENERATOR);
    }

}
