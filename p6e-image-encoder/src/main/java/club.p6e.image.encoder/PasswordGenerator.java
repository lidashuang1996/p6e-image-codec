package club.p6e.image.encoder;

/**
 * @author lidashuang
 * @version 1.0
 */
public abstract class PasswordGenerator {

    public static class Model {
        private final Integer id;
        private final Integer version;
        private final Integer remark;
        private final String secret;

        public Model(Integer id, Integer version, Integer remark, String secret) {
            this.id = id;
            this.version = version;
            this.remark = remark;
            this.secret = secret;
        }

        public Integer getId() {
            return id;
        }

        public Integer getVersion() {
            return version;
        }

        public Integer getRemark() {
            return remark;
        }

        public String getSecret() {
            return secret;
        }

        public byte[] toBytes() {
            return new byte[0];
        }
    }

    public abstract Model execute(String number);

    public byte[] executeToBytes(String number) {
        return this.execute(number).toBytes();
    }

}
