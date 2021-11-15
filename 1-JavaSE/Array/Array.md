2021年10月7日00:54:23

#  Array

##  数组的特点

 * 1）数组是有序排列的
 * 2）数组属于引用数据类型的变量。数组的元素，既可以是基本数据类型，也可以是引用数据类型
 * 3）创建数组对象会在内存中开辟一整块连续的空间
 * 4）数组的长度一旦确定，就不能修改。

##  一维数组的使用

*   ① 一维数组的声明和初始化
 *   ② 如何调用数组的指定位置的元素
 *   ③ 如何获取数组的长度
 *   ④ 如何遍历数组
 *   ⑤ 数组元素的默认初始化值 ：见ArrayTest1.java
 *   ⑥ 数组的内存解析 ：见ArrayTest1.java

```java
int[] ids;//声明
//1.1 静态初始化:数组的初始化和数组元素的赋值操作同时进行
ids = new int[]{1001,1002,1003,1004};
//1.2动态初始化:数组的初始化和数组元素的赋值操作分开进行
String[] names = new String[5];

//printArray({1,2,3});  没有这种语法。
//如果直接传递一个静态数组的话，语法必须这样写
printArray(new int[]{1,2,3});
```

##  数组元素的默认初始化值

 * 		数组元素是整型：0
 * 		数组元素是浮点型：0.0
 * 		数组元素是char型：0或'\u0000'，而非'0'
 * 		数组元素是Boolean型：false
 * 		数组元素是引用数据类型：null

##  二维数组

- 二维数组：int[][] y 或者 int[ ] y[] 或者 int y[ ] [ ]
- （动态初始化）：int[][] arr = new int[ 3 ] [  ];
  - 二维数组中有3个一维数组。每个一维数组都是默认初始化值 `null `

##  排序算法

衡量排序算法的优劣： 

1. 时间复杂度：分析关键字的比较次数和记录的移动次数 
2. 空间复杂度：分析排序算法中需要多少辅助内存
3. 稳定性：若两个记录A和B的关键字值相等，但排序后A、B的先后次序保 持不变，则称这种排序算法是稳定的。

##  java.util.Arrays工具类的使用

1. boolean equals(int[] a,int[] b)  ：判断两个数组是否相等。
2. String toString(int[] a) 输出数组信息。
3. void fill(int[] a,int val) 将指定值填充到数组之中。
4. void sort(int[] a) 对数组进行排序。
5. int binarySearch(int[] a,int key) 对排序后的数组进行二分法检索指定的值。

##   数组使用中的常见异常

1. 数组脚标越界异常(ArrayIndexOutOfBoundsException)
2. 空指针异常(NullPointerException)

##  数组的拷贝

1. int[] arr2 = Arrays.copyOf(arr1,arr1.length);
2. System.arraycopy(arr1,0,arr3,0,arr1.length);//源，源的起始位置，目标，目标的起始位置，拷贝长度。

```java
//【1】数组拷贝
int[] arr1 = {1,2,5,8,9,10,22};
		int[] arr2 = Arrays.copyOf(arr1,arr1.length);
		for(int i = 0; i < arr2.length; i++) {
			System.out.print(arr2[i] + " ");
		}
		System.out.println("\n-----------------");
		//System.arraycopy();
		int[] arr3 = new int[arr1.length];
		System.arraycopy(arr1,0,arr3,0,arr1.length);//源，源的起始位置，目标，目标的起始位置，拷贝长度。
		for(int i = 0; i < arr3.length; i++) {
			System.out.print(arr3[i] + " ");
		}		
```

