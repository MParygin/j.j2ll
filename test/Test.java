package test;

public class Test {

//    public int ST_INT = 10;
//    public double ST_DBL = 10.0;

    // INT
    public static int int_misc(int a, int b) {
        return ((a + b - 3) + 16) * 2;
    }

    public static int int_mul(int a, int b) {
        return a * b;
    }

    public static int int_div(int a, int b) {
        return a / b;
    }

    public static int int_rem(int a, int b) {
        return a % b;
    }

    // LONG
    public static long long_add(long a, long b) {
        return a + b;
    }

    public static long long_sub(long a, long b) {
        return a - b;
    }

    public static long long_mul(long a, long b) {
        return a * b;
    }

    public static long long_div(long a, long b) {
        return a / b;
    }

    public static long long_rem(long a, long b) {
        return a % b;
    }

    // float
    public static float float_add(float a, float b) {
        return a + b;
    }

    public static float float_sub(float a, float b) {
        return a - b;
    }

    public static float float_mul(float a, float b) {
        return a * b;
    }

    public static float float_div(float a, float b) {
        return a / b;
    }

//    public static float float_rem(float a, float b) {
//        return a % b;
//    }

    // double
    public static double double_add(double a, double b) {
        return a + b;
    }

    public static double double_sub(double a, double b) {
        return a - b;
    }

    public static double double_mul(double a, double b) {
        return a * b;
    }

    public static double double_div(double a, double b) {
        return a / b;
    }

    // short
/*
    public static short short_add(short a, short b) {
        return (short) (a + b);
    }

    public static short short_sub(short a, short b) {
        return (short) (a - b);
    }

    public static short short_mul(short a, short b) {
        return (short) (a * b);
    }

    public static short short_div(short a, short b) {
        return (short) (a / b);
    }
*/


    public static void main() {
/*
        linux.glibc.put(0);
        linux.glibc.put(int_misc(10, 20));
        linux.glibc.put(int_mul(10, 20));
        linux.glibc.put(int_div(100, 8));
        linux.glibc.put(int_rem(100, 8));

        linux.glibc.put(long_add(12345678L, 87654321L));
        linux.glibc.put(long_sub(87654321L, 12345678L));
        linux.glibc.put(long_mul(6, 12345678L));
        linux.glibc.put(long_div(87654321L, 12345678L));
        linux.glibc.put(long_rem(87654321L, 12345678L));
*/

        int[] arr_int = new int[16];
        long[] arr_long = new long[16];
        float[] arr_float = new float[16];
        double[] arr_double = new double[16];

        int[][][][][][][][][][][][][][][][] ppp = new int[1][2][5][6][7][8][9][4][5][5][4][4][4][4][4][4];
        if (ppp != null) {
            System.out.println("dd");
        }

        for (int i = 0; i < 16; i++) {
            arr_int[i] = 123456;
            arr_long[i] = 1234567890123456789L;
            arr_float[i] = 1.25f;
            arr_double[i] = 12345.6789d;
        }



//        linux.glibc.put(1111111111111111111L);

        for (int i = 0; i < 16; i++) {
            linux.glibc.put(arr_int[i]);
            linux.glibc.put(arr_long[i]);
            linux.glibc.put((int) arr_float[i]);
            linux.glibc.put((int) (64.45d * i));
            linux.glibc.put(1111111111);
            linux.glibc.put(10000L << i);
        }


/*        linux.glibc.put(7777777777777777777L);

        for (int i = 0; i < 10; i++) {
            linux.glibc.put(int_mul(i, i));
        }

*/

    }
}
