package test;

import java.util.Random;

public class Test {
    static void print() {
        int m = new Random().nextInt(1000);
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(m);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new Thread(() -> print()).start();
        }
    }
}
