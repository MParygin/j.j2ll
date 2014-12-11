package test;

public class Test {

    public static void main() {
	main();
    }

    public static void main(String[] a) {
        long t = System.currentTimeMillis();

        double v[] = new double[100000];
        for (int i = 0; i < 100000; i++) v[i] = i * 1.2d;

        for (int i = 0; i < 100000; i++)
        for (int j = 0; j < 100000; j++) v[i] = v[i] + v[j];

        long tt = System.currentTimeMillis();
        System.out.println(tt - t);
    }
}
