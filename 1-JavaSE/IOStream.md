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

>>常用文本编辑快捷键：
>
>- home键：光标回到行首；end：光标回到行尾
>- ctrl + home键：光标回到文章开始；ctrl + end键：光标回到文章末尾
>- shift + home 或 end 键：选中一行
>- 鼠标双击：选中一个单词 / 鼠标连续击3次：选中一行
>- ctrl+ shift + 右箭头或左箭头：选中一个单词
>- ctrl + f ：查找

---

#  IO Stream

##  Serialize

>>private static final long serialVersionUID = 1L; 序列化版本号
>
>

## File类的方法

>  exists、createNewFile、mkdir、mkdirs、getParent()、getParentFile()、getAbsolutePath()、getName()、isDirectory()、isFile()、lastModified()、length()、File[] listFiles();
>
> 

## 读写操作

>>**FileReader**
>
>- FileReader只能读和项目一致的编码方式的文件。【而转换流InputStreamReader可以在转换时特别指定编码方式】
>- read(char[] cbuf):返回每次读入cbuf数组中的字符的个数。如果达到文件末尾，返回-1。
>
>>**FileWriter**
>
>- new FileWriter(file,``true``):不会对原文件覆盖，而是在原文件基础上追加内容。
>- void write(char[] buff, int offset, int length); 第二个参数是开始索引，第三个参数是要写的字符数。
>- 输出流最后要**刷新**：fw.flush()；
>
>>**FileInputStream**
>
>- int readCount = fis.read(bytes); //返回值是：读取到的字节数量。（不是字节本身）
>- System.out.println(new String(bytes, 0, readCount));
>- 1.int available(); 返回流当中剩余的没有读到的字节数量
>- 2.long skip(long n); 跳过几个字节不读
>
>>**FileOutputSteam**
>
>- new FileOutputStream("2021828.txt", true);以追加的方式在文件末尾写入。
>
>```java
>String str = "xiaomifengxuejava";
>byte[] bs = str.getBytes(); //将字符串转换为byte数组
>fos.write(bs);
>fos.write(bs, 0, 2); //将bs数组的一部分写出
>```
>
>- fos.flush(); //写完之后一定要**刷新**
>
>>**BufferdeReader**  缓冲输入流
>
>- new BufferedReader(new FileReader(("hello1.txt"));
>
>```java
>String line = null;
>//br.readLine()方法读取一个文本行，但不带换行符。(特有的方法)
>while ((line = br.readLine()) != null) {
>    System.out.println(line);
>}
>```
>
>>**InputStreamReader ** 转换输入流
>
>```java
>InputStreamReader isr = new InputStreamReader(new
>			 FileInputStream("input.txt","UTF-8");
>//具体使用哪个字符集，取决于文件input.txt保存时使用的是哪个字符集
>```
>
>- 将一个字节的输入流InputStream转换为字符的输入流Reader
>
>>**OutputStreamWriter**  
>
>- 将一个字符的输出流Writer转换为字节的输出流OutputStream

