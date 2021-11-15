#  Typora的使用

[Typora的使用-GitHub](https://github.com/younghz/Markdown)    |     [Typora的使用-CSDN](https://blog.csdn.net/weixin_39751195/article/details/109971095)    |     [中文文案排版指南](https://github.com/sparanoid/chinese-copywriting-guidelines/blob/master/README.zh-CN.md)    |    [程序员鱼皮](https://www.code-nav.cn/resources)  

 [Java学习路线by-yupi](https://doc.code-nav.cn/roadmap/java)

- 段落的前后要有空行，所谓的空行是指没有文字内容。若想在**段内强制换行**的方式是使用**两个以上**空格加上回车（引用中换行省略回车）。
- 在段落的每行或者只在第一行使用符号`>`,还可使用多个**嵌套**引用，如：\> 区块引用 \>> 嵌套引用
- 在**强调**内容两侧分别加上`*`或者`_`，如：*斜体*   **粗体**  ~~删除线的内容~~ 或按快捷键Alt+Shift+5
- **分割线**最常使用就是三个或以上`*`，还可以使用`-`和`_`和+。**【回车】**
- 符号`: 起到标记作用。如： :`` ctrl+a``
- []() 行内式链接  ；
- Ctrl + F 搜索 ；Ctrl + H 替换
- **使用表情** : + 表情单词(| 注意，英文冒号之后紧跟一个单词，支持智能提示)
- **插入代码块**\``` + 对应编程语种

>常用文本编辑快捷键：

- home键：光标回到行首；end：光标回到行尾
- ctrl + home键：光标回到文章开始；ctrl + end键：光标回到文章末尾
- shift + home 或 end 键：选中一行
- 鼠标双击：选中一个单词 / 鼠标连续击3次：选中一行
- ctrl+ shift + 右箭头或左箭头：选中一个单词
- ctrl + f ：查找

---

#  JDBC

1.  Class.forName("com.mysql.jdbc.Driver");	
   1. Class.forName需要捕获ClassNotFoundException.
   2. Class.forName是把这个类加载到JVM中，加载的时候，就会执行其中的静态初始化块，完成驱动的初始化的相关工作。 
2.  注册驱动、获取链接、获取数据库操作对象（专门执行sql语句的对象）、执行sql语句 、处理查询结果集、释放资源 
3.  加载驱动程序类的时候，会自动完成注册，注册给DriverManager，通过DriverManager再获取连接，需要提供url，user、password
4.  **java.sql.Driver** 接口是所有 JDBC 驱动程序需要实现的接口。在程序中不需要直接去访问实现了 Driver 接口的类，而是由``驱动程序管理器类(java.sql.DriverManager)``去调用这些Driver实现。
    1.  Oracle的驱动：oracle.jdbc.driver.OracleDriver
        1.  Oralce数据库连接：jdbc:oracle:thin:@localhost:1521:dat01
    2.  Mysql的驱动：com.mysql.jdbc.Driver
        1.  MySQL数据库连接：jdbc:mysql://localhost:3306/dat01
5.  **使用配置文件的好处：**①实现了代码和数据的分离，如果需要修改配置信息，直接在配置文件中修改，不需要深入代码。②如果==修改了配置信息，省去重新编译的过程==。
    1.  使用类加载器的方式,配置文件必须放在src路径下，不能放在项目目录下：ImputStream ins = XXX.class.getClassLoader().getResourceAsStream("xxx.properties");
6.  导致SQL注入的根本原因是：输入的用户名信息以及密码中，含有SQL语句的关键字。这个SQL语句的关键字和底层的SQL语句进行了“字符串拼接”，导致原SQL语句的 含义被扭曲了。最主要的原因是：**用户提供的信息参与了SQL语句的编译**。
    1.  怎么解决SQL注入的：即使用户信息中有sql关键字，但是不参加编译就没事。
7.  

##  连接池

>>c3p0
>
>1. ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
>2. Connection connect = cpds.getConnection();
>
>>druid
>
>1. Properties pro = new Properties();
>2. InputStream ins = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
>3. pro.load(ins); ins.close();
>4. DruidDataSource source = DuridDataSourceFactory.createDataSource(pro);
>5. Connection connect = source.getConnection();
>
>- driverClassName=com.mysql.jdbc.Driver
>- url=jdbc:mysql://localhost:3306/dat01?rewriteBatchedStatement=true
>- username=root、password=hrj、initialSize=?、minIdle=?、maxActive=?、
>- 【#max wait time (5000 mil seconds)】maxWait=?
>
>
>
>
>
>
