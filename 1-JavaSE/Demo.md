#  Demo

##   二分查找法

```java
//二分查找法。必须有前提：数组中的元素要有序。 
public static int halfSeach_2(int[] arr,int key){
	int min,max,mid;
	min = 0;
	max = arr.length-1;
	mid = (max+min)>>1; //(max+min)/2;
	while(arr[mid]!=key){
		if(key>arr[mid]){
			min = mid + 1;
		}
		else if(key<arr[mid])
			max = mid - 1;
		if(max<min)
			return -1;
		mid = (max+min)>>1;	
	}
	return mid;
}
```

##  抽象方法：

```java
//模板方法设计模式：
abstract class GetTime{
	public final void getTime(){ //此功能如果不需要复写，可加final限定
		long start = System.currentTimeMillis();
		code(); //不确定的功能部分，提取出来，通过抽象方法实现
		long end = System.currentTimeMillis();
		System.out.println("毫秒是："+(end-start));
	}
	public abstract void code(); //抽象不确定的功能，让子类复写实现
}
class SubDemo extends GetTime{
	public void code(){ //子类复写功能方法
		for(int y=0; y<1000; y++){
			System.out.println("y");
		}
	}
}
```

