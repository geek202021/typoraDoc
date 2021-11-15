public class SleepMethod {
    public static void main(String[] args) {
        //关于Thread.sleep()方法的一个面试题
//        MyThread1 t = new MyThread1();  //NoClassDefFoundError
        Thread t = new MyThread1();
        t.setName("t");
        t.start();
        //调用sleep方法
        try {
            //问：这行代码会让线程t进入休眠状态吗
            t.sleep(1000 * 3); //在执行的时候还是会转换成：Thread.sleep(1000 * 3);
            //这行代码的作用是：让当前线程进入休眠，也就是说main线程进入休眠。（出现在main方法中）
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //3s后这里才会执行
        System.out.println("helloWorld!");
    }
}

class MyThread1 extends Thread{
    public void run(){
        for (int i = 0; i < 1000; i++) {
            System.out.println(Thread.currentThread().getName() + "-->" + i);
        }
    }
}