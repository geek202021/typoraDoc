# Java练习题：

2021年9月2日：

```java
class Test
{
     private int data;
     int result = 0;
     public void m()
     {
         result += 2;
         data += 2;
         System.out.print(result + "  " + data);
     }
 }
 class ThreadExample extends Thread
 {
     private Test mv;
     public ThreadExample(Test mv)
     {
         this.mv = mv;
     }
     public void run()
     {
         synchronized(mv)
         {
             mv.m();
         }
     }
 }
 class ThreadTest
 {
     public static void main(String args[])
     {
         Test mv = new Test();
         Thread t1 = new ThreadExample(mv);
         Thread t2 = new ThreadExample(mv);
         Thread t3 = new ThreadExample(mv);
         t1.start();
         t2.start();
         t3.start();
     }
 }  //2 24 46 6
```

>Test mv =newTest()声明并初始化对data赋默认值 
>
>使用synchronized关键字加同步锁线程依次操作m() 
>
>t1.start();使得result=2,data=2,输出即为2 2 
>
>t2.start();使得result=4,data=4,输出即为4 4 
>
>t3.start();使得result=6,data=6,输出即为6 6 
>
> System.out.print(result +" "+ data);是print()方法不会换行,输出结果为2 24 46 6

```java
Math.round(11.5) 等于多少 (). Math.round(-11.5) 等于多少 (  ).
//1.Math类中提供了三个与取整有关的方法：ceil,floor,round,这些方法的作用于它们的英文名称的含义相对应，例如：ceil的英文意义是天花板，该方法就表示向上取整，Math.ceil（11.3）的结果为12，Math.ceil(-11.6)的结果为-11；floor的英文是地板，该方法就表示向下取整，Math.floor(11.6)的结果是11，Math.floor(-11.4)的结果-12；最难掌握的是round方法，他表示“四舍五入”，算法为Math.floor(x+0.5),即将原来的数字加上0.5后再向下取整，所以，Math.round(11.5)的结果是12，Math.round(-11.5)的结果为-11.
//2.round函数是取最接近整数，如果遇到一样近，则取最大值。
```

```java
public class Test{
static{
   int x=5;
}
static int x,y;
public static void main(String args[]){
   x--;
   myMethod( );
   System.out.println(x+y+ ++x);
}
public static void myMethod( ){
  y=x++ + ++x;
 }
}
```

>1.JVM加载class文件时，就会执行静态代码块，==静态代码块中初始化了一个变量x并初始化为5，由于该变量是个局部变量，静态代码快执行完后变被释放==。
>
>2.申明了两个静态成员变量x，y，并没有赋初值，会有默认出值，int类型为0，
>
>3.执行x--操作，变量单独进行自增或自减操作x--和--x的效果一样，此时x变为了-1
>
>4.调用MyMethod()方法，在该方法中对x和y进行计算，由于x和y都是静态成员变量，所以在整个类的生命周期内的x和y都是同一个
>
>5.y=x++ + ++x可以看成是y=(x++)+(++x)，当++或者--和其它变量进行运算时，x++表示先运算，再自增，++x表示先自增再参与运算
>
>所以就时x为-1参与运算，然后自增，x此时为0，++x后x为1，然后参与运算，那么y=-1+1就为0，此时x为1
>
>6.执行并打印x+y + ++x运算方式和第5步相同，最后计算结果就为3.

---

2021年9月3日：

- 接口中的变量默认是 public static final 的，方法默认是 public abstract 的
- \>> 为带符号右移，右移后左边的空位被填充为符号位。\>>> 为不带符号右移，右移后左边的空位被填充为 0 。没有 <<<  因为 << 后右边总是补 0 。

2021年9月5日：

- final和abstract在一个类中只能声明其中的一个，因为final是最终类，不能继承，必须可以创建实例；而abstract是抽象类，只能继承，不能有实例。
