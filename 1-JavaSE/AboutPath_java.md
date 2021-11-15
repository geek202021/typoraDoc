2021-8-20  22：02

```java
//properties中的内容：className = java.lang.String
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;  
//1.获取一个文件的绝对路径
String path = Thread.currentThread().getContextClassLoader()
    .getResource("reflect.properties").getPath();
FileReader fr = new FileReader(path);

//2.直接以流的形式返回
InputStream inputStream = Thread.currentThread().getContextClassLoader()
    .getResourceAsStream("reflect.properties");
Properties pro = new Properties();
pro.load(inputStream);
inputStream.close();
//通过key获取value
String classname = pro.getProperty("className");

//3.资源绑定器的方式(只能绑定xxx.properties文件)
ResourceBundle bundle = ResourceBundle.getBundle("reflect");
String classname2 = bundle.getString("className");
```

