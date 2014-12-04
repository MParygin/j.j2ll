package test;

public class Test {


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

        int[] fff = new int[16];
        for (int i = 0; i < 4; i++) {
            fff[i] = i * i;
        }


        linux.glibc.put(1111111111111111111L);
        for (int i = 0; i < 4; i++) {
            linux.glibc.put(fff[i]);
        }

        linux.glibc.put(7777777777777777777L);

        for (int i = 0; i < 10; i++) {
            linux.glibc.put(int_mul(i, i));
        }


    }
}
