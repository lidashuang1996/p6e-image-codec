package club.p6e.image.encoder;

/**
 * @author lidashuang
 * @version 1.0
 */
public final class P6eImageEncoderCore {

    private static volatile Actuator ACTUATOR;

    private static volatile NumberGenerator NUMBER_GENERATOR;

    private static volatile PasswordGenerator PASSWORD_GENERATOR;

    public static void setActuator(Actuator actuator) {
        P6eImageEncoderCore.ACTUATOR = actuator;
    }

    public static Actuator getActuator() {
        return ACTUATOR;
    }

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

    public static void run() {
        final PasswordGenerator.Model model = PASSWORD_GENERATOR.execute(NUMBER_GENERATOR.run());
        ACTUATOR.execute(model.getSecret(), model.getRemark());
    }

}
