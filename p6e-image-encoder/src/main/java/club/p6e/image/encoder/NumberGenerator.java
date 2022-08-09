package club.p6e.image.encoder;

/**
 * @author lidashuang
 * @version 1.0
 */
public abstract class NumberGenerator {

    private static final int NUMBER_DATA_LENGTH = 32;

    public String run() {
        final String num = this.execute();
        if (num == null || num.length() != NUMBER_DATA_LENGTH) {
            throw new RuntimeException();
        }
        return num;
    }

    public abstract String execute();

}
