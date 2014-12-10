package j2ll;

import org.objectweb.asm.ClassReader;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 */
public class Main {



    public static void main(String[] args) throws IOException {
        //String className = "test.Linpack";
        String className = "test.Test_array";
        //String className = "fenix.core$f4";


        String out = "test.Test.ll";
        PrintStream ps = new PrintStream(new File("./tmp", out));
        CV cv = new CV(ps);
        ClassReader cr = new ClassReader(className);
        cr.accept(cv, 0);
        ps.flush();
    }

}
