package test;

public class Test {
    int c;

    public Test() {
        int a= 10;
        int b = a * 10;
        c = b >> 1;
    }





    //    public int ST_INT = 10;
//    public double ST_DBL = 10.0;

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

        Test tt = new Test();
        linux.glibc.put(tt.c);

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

///        int[][][][][][][][][][][][][][][][] ppp = new int[1][2][5][6][7][8][9][4][5][5][4][4][4][4][4][4];
///        if (ppp != null) {
///            System.out.println("dd");
///        }

        for (int i = 0; i < 16; i++) {
            arr_int[i] = 123456;
            arr_long[i] = 1234567890123456789L;
            arr_float[i] = 1.25f;
            arr_double[i] = 12345.6789d;
        }



//        linux.glibc.put(1111111111111111111L);

        for (int i = 0; i < 16; i++) {
            linux.glibc.put(i);
            //linux.glibc.put(arr_int[i]);
            //linux.glibc.put(arr_long[i]);
            //linux.glibc.put((int) arr_float[i]);
            if ((64.45d * i) < 300) {
                linux.glibc.put(1111111111);
            }
            //linux.glibc.put(10000L << i);
        }


/*        linux.glibc.put(7777777777777777777L);

        for (int i = 0; i < 10; i++) {
            linux.glibc.put(int_mul(i, i));
        }

*/

    }
}
