package club.p6e.image.codec;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author lidashuang
 * @version 1.0
 */
public class Test {

    public static void main(String[] args) throws Exception {
//        System.out.println(
//                Arrays.toString(FileActuator.encrypt("123456".getBytes(StandardCharsets.UTF_8), "123456781234567812345678"))
//        );
//        System.out.println(
//                Arrays.toString(FileActuator.decrypt(new byte[] {
//                        1, 125, -91, -59, -64, 69, 110, -88, 105, 73, 1, -32, -7, -114, 81, -88
//                }, "123456781234567812345678"))
//        );

        System.out.println(System.currentTimeMillis());
        P6eImageEncoderCore.run(new FileActuator(new File("123.jpeg"), new File("333.pie")));
        System.out.println(System.currentTimeMillis());
        P6eImageDecoderCore.run(new FileActuator(new File("123.pie"), new File("666.jpeg")));
        System.out.println(System.currentTimeMillis());

//        System.out.println(new NumberGeneratorDefaultImpl().execute());
    }

}
