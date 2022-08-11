package club.p6e.image.codec;

import java.io.File;

/**
 * @author lidashuang
 * @version 1.0
 */
public class Test {

    public static void main(String[] args) throws Exception {

        P6eImageEncoder.init();
        P6eImageDecoder.init();

        System.out.println(System.currentTimeMillis());
        P6eImageEncoder.run(new FileActuator(new File("123.jpeg"), new File("2.pie")));
        System.out.println(System.currentTimeMillis());
        P6eImageDecoder.run(new FileActuator(new File("1.pie"), new File("a.jpeg")));
        System.out.println(System.currentTimeMillis());

//        System.out.println(new NumberGeneratorDefaultImpl().execute());
    }

}
