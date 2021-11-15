# day09_JSP

### 1. jsp简介

**1.1 为什么学习jsp?**

以【登录业务】为例，当登录失败后，跳转回login.html页面时，需要提示用户【输入用户名和密码有误】。

此时，目前有两种解决方案

1. 在Servlet中使用响应流，不足：大量html内容，占用响应流资源
2. 在html中重新创建一个【带有提示信息】的login2.html。不足：login.html和login2.html高度冗余。

**总结：完美解决方案，使用动态页面：jsp【在页面中将数据动态化显示】**

**1.2 jsp全称：Java Server pages, java服务器端页面**。

**1.3 jsp只能运行在服务器中，不能直接使用浏览器打开**

**1.4 jsp本质是一个Servlet**

- jsp更适合于显示页面数据，数据采集
- servlet更适合于业务处理【处理请求，做出响应】

**1.5 jsp能够以HTML页面的方式呈现数据，是一个可以嵌入Java代码的HTML。**

**1.6 jsp_Helloworld**，新建jsp步骤，如下图所示：

![](D:\typora_document\3_Javaweb\JSP\imgs\day09_jsp1.png)

![day09_jsp2](D:\typora_document\3_Javaweb\JSP\imgs\day09_jsp2.png)



### 2. jsp工作原理

**2.1 验证jsp本质是一个Servlet，如下图所示：**

- 查找jsp翻译&编译后的文件

  ![](D:\typora_document\3_Javaweb\JSP\imgs\day09_jsp_work2.png)

- 源码如下图所示

![](D:\typora_document\3_Javaweb\JSP\imgs\day09_jsp_work.png)

**2.2 jsp工作原理**

- 第一次运行xxx.jsp文件时，服务器将xxx.jsp文件先翻译成xxx_jsp.java文件，再编译成xxx_jsp.class文件。
- 以后再运行当前xxx.jsp文件时
  - 如代码改变时，服务器会再次翻译&编译当前xxx.jsp文件
  - 如代码未改变时，服务器不会再次翻译&编译当前xxx.jsp文件



### 3. jsp基本语法

**3.1 指令**

- 格式：\<%@%>
- 常用指令：\<%@ page %>、\<%@ include%>、 \<%@ taglib%>

**3.2 模板元素**

- html、css、js...

**3.3 代码脚本片段**

- 格式：\<%%>
- 作用：在_jspService()方法中，书写java代码

**3.4 表达式**

- 格式：\<%=%>
- 作用：将数据显示到页面。类似Servlet的PrintWriter的write()方法作用。【out.print()】

**3.5 声明**

- 格式：\<%!%>
- 作用：在当前翻译后的xxx_jsp.java类中，书写java代码

**3.6 注释**

- java注释：//		/**/			
- html注释：\<!---->
- jsp注释：\<%-- --%>



### 4. jsp中常用指令

**4.1 page指令**

- 语法：<%@ page  属性=属性值   属性2=属性值2 ...%>
- 属性：
  - language:设置当前jsp中语言。有且仅有java。
  - contentType:与response.setContentType()方法作用一致。【设置服务器编码与浏览器解码】
  - import:导包
  - pageEncoding:设置jsp页面的编码字符集
  - errorPage:设置当前页面错误时的跳转页面。【错误位置在脚本片段中，声明中错误无法捕获】
  - isErrorPage:设置当前页面是否为错误页面，默认值:false
    - true:设置当前页面为异常页面，可以使用exception内置对象，捕获异常。
    - false:设置当前页面不为异常页面，不可以使用exception内置对象，捕获异常。

**4.2 include指令**【静态包含】

- 语法：\<%@ include file="" %>
- 属性：file="设置被包含文件的路径"
- 特点：被包含的文件不会被翻译&编译。（先包含，再翻译&编译）
- 作用：在当前文件中，包含其他文件。
- 应用场景：包含静态文件或有较少的java代码的文件。



### 5. jsp中常用的动作标签

> jsp的动作标签语法：<jsp: 标签名 >

**5.1 动态包含**

- 语法：\<jsp: include page=""/>
- 属性：page="设置被包含文件的路径"
- 特点：被包含的文件同时会被翻译&编译。（先翻译&编译，再包含）
- 作用：在当前文件中，包含其他文件。
- 应用场景：包含文件中含义大量的java代码。

**5.2 转发**

- 语法：\<jsp:forward   page="">\</jsp:forward>

- 属性：page:"设置转发的路径"

- 作用：转发请求

- 子标签：

  ```jsp
  <jsp:forward page="include_demo.jsp">
      <jsp:param name="stuName" value="zs"/>
  </jsp:forward>
  ```

  > **注意：如转发中不需要传递参数时，那么该标签的开始标签与结束标签之间，不允许书写任何内容。【包括空格都不能书写】，正确书写语法如下：**
  >
  > ```jsp
  > <jsp:forward page="include_demo.jsp"></jsp:forward>
  > ```



### 6. jsp中九大隐式对象（内置对象、隐含对象）【重点】

> 什么是隐式对象？
>
> 无需程序员自己实例化的对象。【jsp隐式对象，是服务器预先定义&实例化的对象】
>
> **注意： **Jsp默认页面中只有8个隐式对象，需要设置isErrorPage=“true”时，才会出现exception这个隐式对象。
>
> 具体隐式对象如下图所示:

![](D:\typora_document\3_Javaweb\JSP\imgs\day10_jsp1.png)

**6.1 pageContext**

- 类型：javax.servlet.jsp.PageContext

- 定义：代表页面【域对象】

- 作用：

  1. **页面域对象**，具体见【7.jsp中四大域对象】

  2. 获取其他八个隐式对象。【九大隐式对象的“大哥”，“大哥”可以调用其他8个“小弟”】

      pageContext.getRequest()或pageContext.getXXX()

- 在Servlet中的获取方式：无

**6.2 request**

- 类型：javax.servlet.http.HttpServletRequest

- 定义：代表浏览器向服务器发送请求报文，封装到了request对象中。该对象由服务器【容器】创建，并以参数的形式，传入到service()方法中，最终传入到doGet()和doPost()方法中。

- 作用：

  1. 获取请求参数

     > 什么是请求参数【请求参数有哪些】？
     >
     > 请求参数，整体分为两类
     >
     > 1. 附加在url的?后面的值，就是请求参数。
     > 2. 表单中存在name属性值的表单项，也是请求参数。

     - request.getParameter():获取单个请求参数
     - request.getParameterValues():获取多个请求参数【name相同】

  2. 获取URL信息【<http://localhost:8080/day07_http_servlet】

     - 获取协议名：request.getScheme()
     - 获取主机名：request.getServerName()
     - 获取主机端口号：request.getServerPort()
     - 获取上下文路径【带/+项目名】：request.getContextPath()

  3. 获取请求头信息

     - request.getHeader()

  4. 转发【路径跳转】,转发后地址栏不变。

     - 获取转发器：RequestDispatcher rd = request.getRequestDispatcher("跳转URL");
     - 执行转发：rd.forward(request,response);

  5. **请求域对象**具体见【7.jsp中四大域对象】

     - ServletContext
     - HttpServletRequest

- 在Servlet中的获取方式：可以在doGet()方法中，doPosst()方法中直接使用。

**6.3 session**

- 类型：javax.servlet.http.HttpSession
- 定义：代表浏览器与服务器之间的会话。
- 作用：**会话域对象**，具体见【7.jsp中四大域对象】
- 在Servlet中的获取方式：request.getSession()

**6.4 application**

- 类型：javax.servlet.ServletContext
- 定义：代表当前web应用程序，在服务器启动时被创建。每个应用程序都有唯一的ServletContext对象。
- 作用：
  1. 获取当前上下文路径【带/+项目名】：servletContext.getContextPath()
  2. 获取指定文件的真实路径：servletContext.getRealPath()
  3. 获取【当前上下文】初始化参数：servletContext.getInitParam()
  4. **上下文域对象**，共享数据【共4个域对象，聚齐再讲】servletContext.getAttribute() 
- 在Servlet中的获取方式：直接在doGet()或doPost()中调用getServletContext()获取。

**6.5 page**

- 类型：java.lang.Object
- 源码：final java.lang.Object page = this;
- 作用：当前类的对象。
- 在Servlet中的获取方式：this

**6.6 response**

- 类型：javax.servlet.http.HttpServletResponse
- 定义：与Servlet中讲解一致【参考Servlet笔记即可】
- 作用：与Servlet中讲解一致【参考Servlet笔记即可】
- 在Servlet中的获取方式：可以在doGet()方法中，doPost()方法中直接使用。

**6.7 config**

- 类型：javax.servlet.ServletConfig
- 定义：ServletConfig接口封装了Servlet配置信息，每一个Servlet都有唯一对应的ServletConfig对象。
- 作用：
  1. 获取当前Servlet名称：servletConfig.getServletName()
  2. 获取ServletContext对象：servletConfig.getServletContext()
  3. 获取【当前Servlet】初始化参数：servletConfig.getInitParam()
- 在Servlet中的获取方式：直接在doGet()或doPost()中调用getServletConfig()获取。

**6.8 out**

- 类型：javax.servlet.jsp.JspWriter
- 定义&作用：与Servlet中的PrintWriter作用一致。【响应数据到页面】JspWriter与PrintWriter均继承java.io.Writer。
- 作用：与Servlet中的PrintWriter作用一致。【响应数据到页面】JspWriter与PrintWriter均继承java.io.Writer。
- 在Servlet中的获取方式：无【PrintWriter writer = response.getWriter();】

**6.9 exception**

- 类型：java.lang.Throwable
- 定义&作用：捕获处理异常
- 在Servlet中的获取方式：直接实例化。

### 7. jsp中四大域对象【重点】

**7.1 什么是【域对象】**

- 生活中的“域对象”：以快递行业为例：全球快递、全国快递、同城快递、同区快递。主要在不同区域直接传递【共享】商品、物品。

- 程序中的域对象：主要负责在不同web资源之间进行数据交换【数据共享】
- jsp中四大域对象：pageContext、request、session、application。注意Servlet中只有：request、session、application。

**7.2 域对象共性&使用**

- 每个域对象内部都维护一个Map<String,Object>，域对象的共同方法如下：
  - void setAttribute(String key,Object value):将数据存放到域中。
  - Object getAttribute(String key):从域对象中获取数据
  - void removeAttribute(String key):将数据从域中移除

**7.3 域对象有效范围**

- pageContext【页面域】

  - 作用域范围：当前页面中共享数据有效，离开当前页面失效。

    每个页面都有唯一的pageContext对象。

- request【请求域】

  - 作用域范围：当前请求有效，离开当前请求失效。
  - 当前请求：转发、直接访问某个页面时为当前请求。
  - 离开当前请求：重定向、\<a>、location等跳转页面时，即为离开当前页面。
  - 总结：只要【未使用转发时】路径发生改变，就离开当前请求。

- session【会话域】

  - 作用域范围：当前会话中有效，离开当前会话失效。
  - 当前会话：当前浏览器不关闭或浏览器不更换时，即为当前会话。【会话与浏览器一一对应,与服务器无关】
  - 离开当前会话：关闭浏览器即为会话结束，不同浏览器之间不能共享会话【默认情况】。

- application【上下文域，web域】

  - 作用域范围：当前web应用在服务器中启动状态，即有效。当前web应用关闭或卸载，即失效。
  - 有效：当前web应用为启动状态
  - 失效：关闭服务器、重启服务器、卸载web应用时，即为失效。

- 总结：域对象的大小关系：pageContexet<request<session<application

  ​	在使用域对象时，能用小域就不用大域。















