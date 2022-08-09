package club.p6e.image.codec;

/**
 * @author lidashuang
 * @version 1.0
 */
public abstract class PasswordGenerator {

    public static class Model {
        private final String secret;
        private final ActuatorModel actuatorModel;

        public Model(String secret, ActuatorModel actuatorModel) {
            this.secret = secret;
            this.actuatorModel = actuatorModel;
        }

        public String getSecret() {
            return secret;
        }

        public ActuatorModel getActuatorModel() {
            return actuatorModel;
        }
    }

    public interface Compiler {
        public Model execute(String number);
    }

    public abstract Model execute(String number);

    public abstract Model execute(ActuatorModel model);

}
