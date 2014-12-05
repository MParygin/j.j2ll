package j2ll;

/**
 * Internal Functions
 */
public class Internals {

    public static String newJVMArray(int type) {
        switch (type) {
            case 10: return structireArrayInt();
            case 11: return structireArrayLong();
        }
        return null;
    }

    public static String structireArrayInt() {
        return "{i32, [0 x i32]}*";
    }

    public static String structireArrayLong() {
        return "{i32, [0 x i64]}*";
    }

    public static String structireArrayFloat() {
        return "{i32, [0 x float]}";
    }

    public static String structireArrayDouble() {
        return "{i32, [0 x double]}";
    }

}
