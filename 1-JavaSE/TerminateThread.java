
public class TerminateThread{
	public static void main(String[] args){
		MyThread m = new MyThread();
		Thread t = new Thread(m);
		t.setName("5s后合理终止线程：");
		t.start();
		
		try{
			Thread.sleep(1000 * 5);
		} catch(InterruptedException e){
			e.printStackTrace();
		}
		
		m.flag = false;
	}
}

class MyThread implements Runnable{
	public boolean flag = true;
	
	@Override
	public void run(){
		for(int i = 0; i < 10; i++){
			if(flag){
				System.out.println(Thread.currentThread().getName() + "-->" + i);
				try{
					Thread.sleep(1000);
				} catch(InterruptedException e){
					e.printStackTrace();
				}
			} else {
				return;
			}
		}
	}
}