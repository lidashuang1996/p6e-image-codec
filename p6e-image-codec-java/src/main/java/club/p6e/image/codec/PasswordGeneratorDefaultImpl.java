package club.p6e.image.codec;

import org.apache.commons.codec.digest.DigestUtils;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author lidashuang
 * @version 1.0
 */
public class PasswordGeneratorDefaultImpl extends PasswordGenerator {

    private final List<String> list = new ArrayList<>();

    private final Map<String, Compiler> compiler = new HashMap<>(16);

    private static String key(Integer id, Integer version) {
        if (id == null) {
            id = 0;
        }
        if (version == null) {
            version = 0;
        }
        return "ID@" + id + "_VERSION@" + version;
    }

    public PasswordGeneratorDefaultImpl() {
        compiler.put(key(1, 1), new Compiler() {
            /** 编号 */
            private static final int ID = 1;
            /** 版本号 */
            private static final int VERSION = 1;

            @Override
            public Model execute(String number) {
                return new Model(DigestUtils.md5Hex(DigestUtils.md5Hex(number)), new ActuatorModel(true, ID, VERSION, number));
            }
        });
        compiler.put(key(2, 1), new Compiler() {
            /** 编号 */
            private static final int ID = 2;
            /** 版本号 */
            private static final int VERSION = 1;

            @Override
            public Model execute(String number) {
                final String m = DigestUtils.md5Hex(number);
                final int mIndex = m.charAt(0);
                final int wIndex = mIndex % m.length();
                return new Model(m.substring(wIndex) + m.substring(0, wIndex), new ActuatorModel(true, ID, VERSION, number));
            }
        });
        compiler.put(key(3, 1), new Compiler() {
            /** 编号 */
            private static final int ID = 3;
            /** 版本号 */
            private static final int VERSION = 1;

            @Override
            public Model execute(String number) {
                final String m = DigestUtils.md5Hex(number);
                final int mIndex = m.charAt(0);
                final int wIndex = mIndex % m.length();
                final int eIndex = m.length() - wIndex;
                return new Model(m.substring(eIndex) + m.substring(0, eIndex), new ActuatorModel(true, ID, VERSION, number));
            }
        });
        list.addAll(compiler.keySet());
    }

    @Override
    public Model execute(String number) {
        return compiler.get(list.get(ThreadLocalRandom.current().nextInt(0, list.size()))).execute(number);
    }

    @Override
    public Model execute(ActuatorModel model) {
        final Compiler c = compiler.get(key(model.getId(), model.getVersion()));
        return c == null ? null : c.execute(model.getNumber());
    }
}
