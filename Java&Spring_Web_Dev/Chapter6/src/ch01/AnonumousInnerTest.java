package ch01;

class Outer2 {

    int outNum = 100;
    static int sNum = 200;

    Runnable getRunnable(int i){

        int num = 10;


        class MyRunnable implements Runnable {

            int localNum  = 1000;

            @Override
            public void run() {

                System.out.println("i = " + i);
                System.out.println("num = "+ num);
                System.out.println("localNum =" + localNum);
                System.out.println("outNum = " + outNum + "(외부 클래스 인스턴스 변수)");
                System.out.println("Outter.sNum  = " + Outer2.sNum + "(외부 클래스 정적 변수)");
            }
        }

        return new MyRunnable();
    }
}

public class AnonumousInnerTest {

    public static void main(String[] args) {

        Outer2 out = new Outer2();
        Runnable runnable = out.getRunnable(100);

        runnable.run();

    }
}
