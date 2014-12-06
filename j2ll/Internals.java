package j2ll;

/**
 * Internal Functions
 */
public class Internals {

    public static final String BYTE = "i8";
    public static final String CHAR = "i16";
    public static final String SHORT = "i16";
    public static final String INT = "i32";
    public static final String LONG = "i64";
    public static final String FLOAT = "float";
    public static final String DOUBLE = "double";

    public static int sizeOf(String type) {
        if (BYTE.equals(type)) return 1;
        if (CHAR.equals(type)) return 2;
        if (SHORT.equals(type)) return 2;
        if (INT.equals(type)) return 4;
        if (LONG.equals(type)) return 8;
        if (FLOAT.equals(type)) return 4;
        if (DOUBLE.equals(type)) return 8;
        return 1;
    }

    public static String newJVMArray(int type) {
        switch (type) {
            case 6: return structireArrayFloat();
            case 7: return structireArrayDouble();
            case 8: return structireArrayByte();
            case 9: return structireArrayShort();
            case 10: return structireArrayInt();
            case 11: return structireArrayLong();
        }
        return null;
    }

    public static String arrayOf(String type) {
        return "{i32, [0 x " + type + "]}*";
    }

    public static String structireArrayByte() {
        return "{i32, [0 x i8]}*";
    }

    public static String structireArrayShort() {
        return "{i32, [0 x i16]}*";
    }

    public static String structireArrayChar() {
        return "{i32, [0 x i16]}*";
    }

    public static String structireArrayInt() {
        return "{i32, [0 x i32]}*";
    }

    public static String structireArrayLong() {
        return "{i32, [0 x i64]}*";
    }

    public static String structireArrayFloat() {
        return "{i32, [0 x float]}*";
    }

    public static String structireArrayDouble() {
        return "{i32, [0 x double]}*";
    }

}
