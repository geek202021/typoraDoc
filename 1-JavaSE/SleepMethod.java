public class SleepMethod {
    public static void main(String[] args) {
        //����Thread.sleep()������һ��������
//        MyThread1 t = new MyThread1();  //NoClassDefFoundError
        Thread t = new MyThread1();
        t.setName("t");
        t.start();
        //����sleep����
        try {
            //�ʣ����д�������߳�t��������״̬��
            t.sleep(1000 * 3); //��ִ�е�ʱ���ǻ�ת���ɣ�Thread.sleep(1000 * 3);
            //���д���������ǣ��õ�ǰ�߳̽������ߣ�Ҳ����˵main�߳̽������ߡ���������main�����У�
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //3s������Ż�ִ��
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