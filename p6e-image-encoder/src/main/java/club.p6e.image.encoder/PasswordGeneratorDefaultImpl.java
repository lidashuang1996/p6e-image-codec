package club.p6e.image.encoder;

import org.apache.commons.codec.digest.DigestUtils;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author lidashuang
 * @version 1.0
 */
public class PasswordGeneratorDefaultImpl extends PasswordGenerator {

    public final List<String> list = new ArrayList<>();

    public final Map<String, PasswordGenerator> compiler = new HashMap<>(16);

    public PasswordGeneratorDefaultImpl() {
        compiler.put("ID@1_VERSION@1", new PasswordGenerator() {
            /** 编号 */
            private static final int ID = 1;
            /** 版本号 */
            private static final int VERSION = 1;
            /** 备注 */
            private static final int REMARK = 0;

            @Override
            public Model execute(String number) {
                return new Model(ID, VERSION, REMARK, DigestUtils.md5Hex(DigestUtils.md5Hex(number)));
            }
        });
        compiler.put("ID@2_VERSION@1", new PasswordGenerator() {
            /** 编号 */
            private static final int ID = 2;
            /** 版本号 */
            private static final int VERSION = 1;
            /** 备注 */
            private static final int REMARK = 0;

            @Override
            public Model execute(String number) {
                final String m = DigestUtils.md5Hex(number);
                final int mIndex = m.charAt(0);
                final int wIndex = mIndex % m.length();
                return new Model(ID, VERSION, REMARK, m.substring(wIndex) + m.substring(0, wIndex));
            }
        });
        compiler.put("ID@3_VERSION@1", new PasswordGenerator() {
            /** 编号 */
            private static final int ID = 3;
            /** 版本号 */
            private static final int VERSION = 1;
            /** 备注 */
            private static final int REMARK = 0;

            @Override
            public Model execute(String number) {
                final String m = DigestUtils.md5Hex(number);
                final int mIndex = m.charAt(0);
                final int wIndex = mIndex % m.length();
                final int eIndex = m.length() - wIndex;
                return new Model(ID, VERSION, REMARK, m.substring(eIndex) + m.substring(0, eIndex));
            }
        });
        list.addAll(compiler.keySet());
    }

    @Override
    public Model execute(String number) {
        return compiler.get(list.get(ThreadLocalRandom.current().nextInt(0, list.size()))).execute(number);
    }
}
