#  书城项目

##  表单验证

>**val()** 它可以设置和获取表单项的value属性值。 跟dom属性value一样

```JavaScript
<--regist.jsp页面-->
<script type="text/javascript">
   $(function () {
      // 给验证码图片绑定单击事件
      $("#img_code").click(function () {
         // 在事件响应的function函数中有一个this对象。这个this对象，是当前正在响应事件的dom对象
         // src 属性表示验证码 img 标签的 图片路径。它可读，可写
         // alert(this.src);
         this.src = "${basePath}kaptcha.jpg?dt=" + new Date();
      });

      $("#sub_btn").click(function () {
         var usernameText = $("#username").val();
         var usernameRE = /^\w{5,12}$/;
         if (!usernameRE.test(usernameText)) {
            $("span.errorMsg").text("用户名只能是由字母，数字下划线组成，并且长度为5到12位");
            return false;
         }
         var passwordText = $("#password").val();
         var passwordRE = /^\w{5,12}$/;
         if (!passwordRE.test(passwordText)) {
            $("span.errorMsg").text("密码只能是由字母，数字下划线组成，并且长度为5到12位");
            return false;
         }
         var repwText = $("#repwd").val();
         if (repwText != passwordText) {
            $("span.errorMsg").text("两次输入密码不一致！请重新输入");
            return false;
         }
         var emailText = $("#email").val();
         var emailRE = /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/;
         // var emailPatt = /^[a-z\d]+(\.[a-z\d]+)*@([\da-z](-[\da-z])?)+(\.{1,2}[a-z]+)+$/;
         if (!emailRE.test(emailText)) {
            $("span.errorMsg").text("邮箱格式有误,请重新输入！");
            return false;
         }
         var codeText = $("#code").val();
         codeText = $.trim(codeText);
         if (codeText == null || codeText == "") {
            $("span.errorMsg").text("验证码不能为空！");
            return false;
         }
         $("span.errorMsg").text("");
      });
   });
</script>
```

##   JavaEE项目的三层架构

###   JdbcUtils

> 2021年10月14日23:14:10

```java
public class JdbcUtils {
    //1.使用Druid数据库连接池技术
    private static DruidDataSource datasource;
    static {
        try {
            Properties pro = new Properties();
            InputStream ins = JdbcUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
            pro.load(ins);
//            ins.close();
            datasource = (DruidDataSource)DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //2.获取数据库连接池中的连接
    public static Connection getConnection()  {
        Connection connect = null;
        try {
            connect = datasource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connect;
    }

    //3.关闭连接，放回数据库连接池
    public static void close(Connection connect) {
        if (connect != null) {
            try {
                connect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
```

###   BaseDao

```java
public abstract class BaseDao {
    //使用DbUtils操作数据库
    private QueryRunner queryRunner = new QueryRunner();

    //1.通用的增删改操作：update(),执行：Insert\Update\Delete 语句
    public int update(String sql, Object... args) {
        Connection connect = JdbcUtils.getConnection();
        try {
            return queryRunner.update(connect, sql, args);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           JdbcUtils.close(connect);
        }
        return -1;
    }
    //2.查询返回一个 javaBean 的 sql 语句
    public <T>T queryForOne(Class<T> type, String sql, Object... args) {
        Connection connect = JdbcUtils.getConnection();
        try {
            return queryRunner.query(connect, sql, new BeanHandler<T>(type), args);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(connect);
        }
        return null;
    }
    //3.查询返回一个 javaBean 的 sql 语句,获取对象集合
    public <T> List<T> queryForList(Class<T> type, String sql, Object... args){
        Connection connect = JdbcUtils.getConnection();
        try {
            return queryRunner.query(connect, sql, new BeanListHandler<T>(type), args);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(connect);
        }
        return null;
    }
    //4.执行返回一行一列的 sql 语句,获取某个简单的值
    public Object queryForSingleValue(String sql, Object... args) {
        Connection connect = JdbcUtils.getConnection();
        try {
            return queryRunner.query(connect, sql, new ScalarHandler(), args);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(connect);
        }
        return null;
    }
}
```

###   UserDao & UserDaoImpl

```java
public interface UserDao {
    //1. 根据用户名查询用户信息(如果返回null,说明没有这个用户。反之亦然)
    public User queryUserByUsername(String username);
    //2.根据 用户名和密码查询用户信息(如果返回 null,说明用户名或密码错误)
    public User queryUserByUsernameAndPwd(String username, String password);
    //3.保存用户信息(返回-1 表示操作失败，其他是 sql 语句影响的行数)
    public int saveUser(User user);
}

public class UserDaoImpl extends BaseDao implements UserDao {
    @Override
    public User queryUserByUsername(String username) {
        String sql = "select `id`,`username`,`password`,`email` from t_user2 where username = ?";
        return queryForOne(User.class,sql,username);
    }
    @Override
    public User queryUserByUsernameAndPwd(String username, String password) {
        String sql = "select `id`,`username`,`password`,`email` from t_user2 where username = ? and password = ?";
        return queryForOne(User.class,sql,username,password);
    }
    @Override
    public int saveUser(User user) {
        String sql = "insert into t_user2(`username`,`password`,`email`) values(?,?,?)";
        return update(sql,user.getUsername(),user.getPassword(),user.getEmail());
    }
}

public class UserDaoTest {
    UserDao userDao = new UserDaoImpl();
    @Test
    public void queryUserByUsername() {
        if (userDao.queryUserByUsername("admin") == null) {
            System.out.println("用户名可用！");
        } else{
            System.out.println("用户名已存在！");
        }
    }
    @Test
    public void queryUserByUsernameAndPwd() {
        if ( userDao.queryUserByUsernameAndPwd("admin","admin") == null) {
            System.out.println("用户名或密码错误，登录失败");
        } else {
            System.out.println("查询成功");
        }
    }
    @Test
    public void saveUser() {
        System.out.println( userDao.saveUser(new User(null,"wangwu", "wangwu", "wangwu@qq.com")) );
    }
}
```

###   UserService & UserServiceImpl

```java
public interface UserService {
    //1.用于注册判断
    public void registUser(User user);
    //2.用于登录判断
    public User login(User user);
    //3.检查用户名是否可用
    public boolean existsUsername(String username);
}

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();
    @Override
    public void registUser(User user) {
        userDao.saveUser(user);
    }
    @Override
    public User login(User user) {
        return userDao.queryUserByUsernameAndPwd(user.getUsername(),user.getPassword());
    }
    @Override
    public boolean existsUsername(String username) {
        if(userDao.queryUserByUsername(username) == null){
            return false;
        }
        return true;
    }
}

public class UserServiceTest {
    UserService userService = new UserServiceImpl();
    @Test
    public void registUser() {
        userService.registUser(new User(null,"zhaoliu","zhaoliu","zhaoliu@qq.com"));
    }
    @Test
    public void login() {
        System.out.println(userService.login(new User(null,"admin","admin",null)));
    }
    @Test
    public void existsUsername() {
        if (userService.existsUsername("admin")) {
            System.out.println("用户名已存在！");
        } else {
            System.out.println("用户名可用！");
        }
    }
}
```

>2021年10月16日11:39:21

###  动态化base标签

```jsp
<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:
${pageContext.request.serverPort}${pageContext.request.contextPath}/">

<% String basePath=request.getScheme()+"://"+request.getServerName()+
    	":"+request.getServerPort()+request.getContextPath()+"/"; %>
<base href="<%=basePath%>">

<%@ include file="/pages/common/head.jsp" %>
<%@include file="/pages/common/foot.jsp"%>
<%@include file="/pages/common/login_success_menu.jsp"%>
```

###   BaseServlet

```java
public abstract class BaseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 解决post请求中文乱码问题
        // 一定要在获取请求参数之前调用才有效
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        String action = req.getParameter("action");
        // 获取 action 业务鉴别字符串，获取相应的业务 方法反射对象
        //参数1 ：指明获取的方法的名称  参数2：指明获取的方法的形参列表
        try {
            Method method = this.getClass().getDeclaredMethod(action, HttpServletRequest.class, HttpServletResponse.class);
            //2.保证当前方法是可访问的
            method.setAccessible(true);
            //3.调用方法的invoke():参数1：方法的调用者  参数2：给方法形参赋值的实参
            method.invoke(this, req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

```java
public class UserServletTest{
String action = "regist";
Method method = UserServletTest.class.getDeclaredMethod(action);
method.invoke(new UserServletTest()); //第一个参数：当前的对象实例
}//this代表当前类的实例(谁调用这个方法，这个this就是谁，这是个实现类，在没有继承它的情况下，肯定有它的实例，那么这个this就是它自己)
```

###  WebUtils

>>数据的封装与[BeanUtils工具类](https://www.bilibili.com/video/BV1Y7411K7zz?p=229)的使用：
>
>- 使用一行代码注入：获取请求的参数req.getParameter();把所有请求的参数都注入到user对象中。 需要导入两个BeanUtils的jar包
>- BeanUtils工具类，它可以一次性的把所有请求的参数(Map中的值)注入到JavaBean中,或者是对象属性值的拷贝操作。

```java
public class WebUtils {
    // 把Map中的值注入到对应的JavaBean属性中。
    public static <T> T copyParamToBean(Map value , T bean ){
        try {          
            System.out.println("注入之前：" + bean);
            // 把所有请求的参数都注入到user对象中
            BeanUtils.populate(bean, value);
            System.out.println("注入之后：" + bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }
}
```

###  UserServlet

```java
public class UserServlet extends BaseServlet{
    private UserService userService = new UserServiceImpl();
    //1.处理登录的功能
    protected void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        // 调用 userService.login()登录处理业务
        User loginUser = userService.login(new User(null, username, password, null));
        // 如果等于null,说明登录 失败!
        if (loginUser == null) {
            // 把错误信息，和回显的表单项信息，保存到Request域中
            req.setAttribute("msg", "用户名或密码错误！");
            req.setAttribute("username", username);
            //转发到登录页面
            req.getRequestDispatcher("/pages/user/login.jsp").forward(req, resp);
        } else{
            //登录成功,转发到login_success.jsp页面
            req.getRequestDispatcher("/pages/user/login_success.jsp").forward(req,resp);
        }
    }
    //2.处理注册的功能
    protected void regist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.获取请求的参数
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String code = req.getParameter("code");
        //使用WebUtils注入所有参数
        User user = WebUtils.copyParamToBean(req.getParameterMap(), new User());
        //2.首先判断验证码是否正确
        if ("abcde".equalsIgnoreCase(code)) {
            //2-1验证码输入正确,检查用户名是否可用
            if (userService.existsUsername(username)) {
                //true表示用户名存在，不可用,转发回注册页面，信息回显
                req.setAttribute("msg", "用户名已存在！");
                req.setAttribute("username",username);
                req.setAttribute("email",email);
                req.getRequestDispatcher("/pages/user/regist.jsp").forward(req, resp);
            } else{
                //false表示用户名不存在，可用,调用Service保存到数据库,转发到注册成功页面
                userService.registUser(new User(null,username,password,email));
                req.getRequestDispatcher("/pages/user/regist_success.jsp").forward(req, resp);
            }
        } else{
            //验证码输入错误，把信息回显，保存到Request域中
            req.setAttribute("msg", "验证码输入有误");
            req.setAttribute("username",username);
            req.setAttribute("email",email);
            //转发回注册页面
            req.getRequestDispatcher("/pages/user/regist.jap").forward(req, resp);
        }
    }
}
```

####  session优化登录

>>UserServlet 程序中保存用户登录的信息
>
>```java
>} else{
>    //使用session优化登录和注销(保存用户登录后的信息)2021年10月18日16:28:43
>    req.getSession().setAttribute("user",loginUser);
>    //登录成功,转发到login_success.jsp页面
>    req.getRequestDispatcher("/pages/user/login_success.jsp").forward(req,resp);
>}
>```
>
>> 修改 login_succuess_menu.jsp
>
>- 欢迎\<span class="um_span">${sessionScope.user.username}\</span>光临XXX书城
>- \<a href="pages/order/order.jsp">我的订单\</a>
>- \<a href="userServlet?action=logout">注销\</a>
>
>>修改首页 index.jsp 页面的菜单:
>
>- 需求：登录后返回首页显示用户信息，没有登录则显示注册等信息。
>
>```jsp
><div class="user">
>   <%--如果用户还没有登录，显示：【登录和注册菜单】--%>
>   <c:if test="${empty sessionScope.user}">
>      <a href="pages/user/login.jsp">登录</a> |
>      <a href="pages/user/regist.jsp">注册</a>
>   </c:if>
>   <%--如果用户已经登录,则显示:【登录成功之后的用户信息】--%>
>   <c:if test="${not empty sessionScope.user}">
>      <span>欢迎<span class="um_span">${sessionScope.user.username}</span>光临尚硅谷书城</span>
>      <a href="pages/order/order.jsp">我的订单</a>
>      <a href="userServlet?action=logout">注销</a>&nbsp;&nbsp;
>   </c:if>
>   <a href="pages/cart/cart.jsp">购物车</a>
>   <a href="pages/manager/manager.jsp">后台管理</a>
></div>
>```

####   注销用户logout

>> UserServlet 程序中添加 logout 方法：
>
>```java
>protected void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
>    //1、销毁 Session 中用户登录的信息（或者销毁 Session）
>    req.getSession().invalidate();
>    //2、重定向到首页（或登录页面）。
>    resp.sendRedirect(req.getContextPath());
>}
>```

####  Kaptcha验证码

>> 表单重复提交有三种常见的情况：
>
> 一：提交完表单。服务器使用请求转来进行页面跳转。这个时候，用户按下功能键 F5，就会发起最后一次的请求。 造成表单重复提交问题。解决方法：使用 ``重定向`` 来进行跳转
>
> 二：用户正常提交服务器，但是由于网络延迟等原因，迟迟未收到服务器的响应，这个时候，用户以为提交失败， 就会着急，然后多点了几次提交操作，也会造成表单重复提交。
>
> 三：用户正常提交服务器。服务器也没有延迟，但是提交完成后，用户回退浏览器。重新提交。也会造成表单重复 提交。(后面两种情况采用验证码解决)
>
>>1.导入jar包：谷歌kaptcha，在web.xml中配置生成验证码的servlet类。生成验证码并存在session中。
>
>```xml
><!--复制KaptchaServlet的相对路径-->
><servlet>
>    <servlet-name>KaptchaServlet</servlet-name>
>    <servlet-class>com.google.code.kaptcha.servlet.KaptchaServlet</servlet-class>
></servlet>
><servlet-mapping>
>    <servlet-name>KaptchaServlet</servlet-name>
>    <url-pattern>/kaptcha.jpg</url-pattern>
></servlet-mapping>
>```
>
>>2.在表单中使用 img 标签去显示验证码图片并使用它。
>
>```jsp
><label>验证码：</label>
><input class="itxt inp" type="text" name="code" style="width: 140px;" id="code"/>
><img id="img_code" alt="" src="kaptcha.jpg" style="float: right; margin-right: 22px;width:100px;height:35px;"><br /><br />
>```
>
>>3.在服务器获取谷歌生成的验证码和客户端发送过来的验证码比较使用。
>
>```java
>//UserServlet.java页面
>protected void regist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
>    //获取session中的验证码
>    String token = (String)req.getSession().getAttribute(KAPTCHA_SESSION_KEY);
>    //删除session中的验证码
>    req.getSession().removeAttribute(KAPTCHA_SESSION_KEY);
>    //……
>    //2.首先判断验证码是否正确
>	// if ("abcde".equalsIgnoreCase(code)) {
>    if (token != null && token.equalsIgnoreCase(code)) {……}
>}
>```
>
>> 4.切换验证码：给验证码图片绑定单击事件
>
>```javascript
>// 给验证码图片绑定单击事件
>$("#img_code").click(function () {
>   // 在事件响应的function函数中有一个this对象。这个this对象，是当前正在响应事件的dom对象
>   // src 属性表示验证码 img 标签的 图片路径。它可读，可写
>   // alert(this.src);
>   this.src = "${basePath}kaptcha.jpg?dt=" + new Date();
>});
>```

##  图书模块

###  BookDao & BookDaoImpl

```java
public interface BookDao {
    public int addBook(Book book);
    public int deleteBookById(Integer id);
    public int updateBook(Book book);
    public Book queryBookById(Integer id);
    public List<Book> queryBooks();
}

public class BookDaoImpl extends BaseDao implements BookDao {
    @Override
    public int addBook(Book book) {
        String sql ="insert into t_book2(`name`,`author`,`price`,`sales`,`stock`,`img_path`) values(?,?,?,?,?,?)";
        return update(sql, book.getName(), book.getAuthor(), book.getPrice(), book.getSales(), book.getStock(), book.getImgPath());
    }
    @Override
    public int deleteBookById(Integer id) {
        String sql = "delete from t_book2 where id = ?";
        return update(sql,id);
    }
    @Override
    public int updateBook(Book book) {
        String sql = "update t_book2 set `name`=?,`author`=?,`price`=?,`sales`=?,`stock`=?,`img_path`=? where id =?";
        return update(sql,book.getName(),book.getAuthor(),book.getPrice(),book.getSales(),book.getStock(),book.getImgPath(),book.getId());
    }
    @Override
    public Book queryBookById(Integer id) {
        String sql = "select `id` , `name` , `author` , `price` , `sales` , `stock` , `img_path` imgPath from t_book2 where id = ?";
        return queryForOne(Book.class, sql, id);
    }
    @Override
    public List<Book> queryBooks() {
        String sql = "select `id` , `name` , `author` , `price` , `sales` , `stock` , `img_path` imgPath from t_book2";
        return queryForList(Book.class, sql);
    }
}
```

###   BookService & BookServiceImpl

```java
public interface BookService {
    public void addBook(Book book);
    public void deleteBookById(Integer id);
    public void updateBook(Book book);
    public Book queryBookById(Integer id);
    public List<Book> queryBooks();
}

public class BookServiceImpl implements BookService {
    private BookDao bookDao = new BookDaoImpl();
    @Override
    public void addBook(Book book) {
        bookDao.addBook(book);
    }
    @Override
    public void deleteBookById(Integer id) {
        bookDao.deleteBookById(id);
    }
    @Override
    public void updateBook(Book book) {
        bookDao.updateBook(book);
    }
    @Override
    public Book queryBookById(Integer id) {
        return bookDao.queryBookById(id);
    }
    @Override
    public List<Book> queryBooks() {
        return bookDao.queryBooks();
    }
}
```

> 后台管理=》图书管理=》如果访问jsp无法直接得到数据，那么可以让程序先访问Servlet程序，再转发。
>
> 2021年10月17日10:40:16

###   BookServlet中的list方法

```java
<a href="manager/bookServlet?action=list"> 图书管理/</a>
public class BookServlet extends BaseServlet{
    private BookService bookService = new BookServiceImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }
    //1.列出所有图书的信息
    protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Book> books = bookService.queryBooks();
        //把全部图书信息保存到Request域中
        req.setAttribute("books", books);
        //请求转发到/pages/manager/book_manager.jsp页面
        req.getRequestDispatcher("/pages/manager/book_manager.jsp").forward(req, resp);
    }
}
```

###  add方法

>表单重复提交bug：（采用转发跳转到图书列表页面）
>
>- 当用户提交完请求，浏览器会记录下最后一次请求的全部信息。当用户按下功能键F5，就会发起浏览器记录的最后一次请求（add操作）。
>- 请求转发是一次请求，重定向是两次请求。

```java
//2.添加图书
protected void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //1、获取请求的参数==封装成为Book对象
    Book book = WebUtils.copyParamToBean(req.getParameterMap(), new Book());
    bookService.addBook(book);
    //3、跳到图书列表页面
    resp.sendRedirect(req.getContextPath() + "/manager/bookServlet?action=list");
}

<form action="manager/bookServlet" method="get">
<input type="hidden" name="action" value="add"/>
```

###  delete方法

```java
 //3.删除图书
    protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求的参数id
//        String id = req.getParameter("id");
//        int i = 0;  
//        try {
//            i = Integer.parseInt(id);//会抛NumberFormatException
//        } catch (NumberFormatException e) {
//            e.printStackTrace();  //在WebUtils中写入该方法。
//        }
        int id = WebUtils.parseInt(req.getParameter("id"), 0);
        bookService.deleteBookById(id);
        //3.重定向回图书管理页面
        resp.sendRedirect(req.getContextPath() + "/manager/bookServlet?action=list");
    }
```

> WebUtils中的方法：

```java
//将字符串转换成为int类型的数据
public static int parseInt(String strInt, int defaultValue) {
    try {
        return Integer.parseInt(strInt);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return defaultValue;
}
```

> 给删除添加确认提示操作

```jsp
<td><a class="deleteClass" href="manager/bookServlet?action=delete&id=${book.id}">删除</a></td>
<script type="text/javascript">
//给删除的a标签绑定单击事件，用于删除的确认提示操作
$(function () {
	$("a.deleteClass").click(function () {
//在事件的function函数中，有一个this对象。这个this对象，是当前正在响应事件的dom对象
	return confirm("你是否要删除【" + $(this).parent().parent().find("td:first").text() + "】?");
	});
});
</script>
```

> 修改图书有两个步骤：

1. 把修改的图书信息回显到表单项中 getBookId
2. 提交修改后的数据给服务器保存修改 modify

###  getBookId & modify方法

[图书模块&分页](https://subeily.blog.csdn.net/article/details/109231954)

```java
//4.修改图书功能的实现:第一步：获取要修改的图书id
protected void getBookId(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //1.获取请求参数的id
    int id = WebUtils.parseInt(req.getParameter("id"), 0);
    //2.根据id查询图书
    Book book = bookService.queryBookById(id);
    //3.保存到Request域中
    req.setAttribute("book", book);
    //4 请求转发到。pages/manager/book_edit.jsp 页面
    req.getRequestDispatcher("/pages/manager/book_edit.jsp").forward(req, resp);
}
//5.修改图书功能的实现:第二步：将修改的图书信息保存到数据库
protected void modify(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //1.获取请求的参数，封装成为Book对象
    Book book = WebUtils.copyParamToBean(req.getParameterMap(), new Book());
    bookService.updateBook(book);
    //3、重定向回图书列表管理页面
    resp.sendRedirect(req.getContextPath() + "/manager/bookServlet?action=list");
}
<input type="hidden" name="action" value="${empty param.id ? "add" : "update"}"/>
<input type="hidden" name="id" value="${requestScope.book.id}"/>
```

> 解决 book_edit.jsp 页面，即要实现添加，又要实现修改操作

```jsp
方案一：
<td><a href="pages/manager/book_edit.jsp?method=add">添加图书</a></td>
<td><a href="manager/bookServlet?action=getBookId&id=${book.id}&method=update">修改</a></td>
<input type="hidden" name="action" value="${param.method}"/>
方案二：
<input type="hidden" name="action" value="${empty param.id ? "add" : "update"}"/>
方案三：可以通过判断request域中是否包含有修改的图书信息对象，如果没有说明是添加操作。如果没有说明是修改操作。<input type="hidden" name="action" value="${empty requestScope.book ? "add" : "update"}"/>
```

##  分页模块

>首页 上一页 3 【4】5 下一页 末页 共10页，30条记录 到第___页  确定

###   模型Page的抽取

>当前页数pageNo，总页数pageTotal，总记录数TotalCount,每页记录数pageSize，当前页数据items

###  分页的初步实现

>BookDaoImpl.java

```java
@Override
public Integer queryForTotalCount() {
    String sql = "select count(*) from t_book";
    Number count = (Number) queryForSingle(sql);
    return count.intValue();
}
@Override
public List<Book> queryForPageItems(int begin, int pageSize) {
    String sql = "select `id` , `name` , `author` , `price` , `sales` , `stock` , `img_path` imgPath from t_book limit ?,?";
    return queryForList(Book.class,sql,begin,pageSize);
}
```

>BookServiceImpl.java

```java
@Override
    public Page<Book> page(int pageNo, int pageSize) {  //处理分页业务
        Page<Book> page = new Page<Book>();
        //设置当前页码
        page.setPageNo(pageNo);
        //设置每页显示的数量
        page.setPageSize(pageSize);
        //求总记录数
        Integer totalCount = bookDao.queryForTotalCount();
        //设置总记录数
        page.setTotalCount(totalCount);
        //求总页码
        Integer pageTotal = totalCount / pageSize;
        if (totalCount % pageSize > 0) {
            pageTotal += 1;
        }
        //设置总页码
        page.setPageTotal(pageTotal);
        //设置当前页数据的开始索引
        int begin = (page.getPageTotal() -1) * pageSize;
        //求当前页数据
        List<Book> items = bookDao.queryForPageItems(begin, pageSize);
        //设置当前页数据
        page.setItems(items);
        return page;
    }
```

>BookServlet.java

```java
//6.处理分页功能
protected void page(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1 获取请求的参数 pageNo 和 pageSize
        int pageNo = WebUtils.parseInt(req.getParameter("pageNo"), 1);
        int pageSize = WebUtils.parseInt(req.getParameter("pageSize"), Page.PAGE_SIZE);
        //2 调用BookService.page(pageNo，pageSize)：Page对象
        Page<Book> page = bookService.page(pageNo,pageSize);
        //3 保存Page对象到Request域中
        req.setAttribute("page",page);
        //4 请求转发到pages/manager/book_manager.jsp页面
        req.getRequestDispatcher("/pages/manager/book_manager.jsp").forward(req,resp);
    }
```

>manager_menu.jsp中【图书管理】请求地址的修改:

```jsp
<a href="manager/bookServlet?action=list">图书管理</a> 
改为：<a href="manager/bookServlet?action=page">图书管理</a>
```

>book_manager.jsp修改：

```jsp
<c:forEach items="${requestScope.books}" var="book"> 
改为：<c:forEach items="${requestScope.page.items}" var="book">
table标签下面：
<div id="page_nav">
			<a href="#">首页</a>
			<a href="#">上一页</a>
			<a href="#">3</a>
			【${requestScope.page.pageNo}】
			<a href="#">5</a>
			<a href="#">下一页</a>
			<a href="#">末页</a>
			共${requestScope.page.pageTotal}页，${requestScope.page.totalCount}条记录
			到第<input value="4" name="pn" id="pn_input"/>页
			<input type="button" value="确定">
		</div>
```

###   首页，末页，上一页，下一页实现

```jsp
<div id="page_nav">
   <%--大于首页，才显示:上一页和首页--%>
   <c:if test="${requestScope.page.pageNo > 1}">
      <a href="manager/bookServlet?action=page&pageNo=1">首页</a>
      <a href="manager/bookServlet?action=page&pageNo=${requestScope.page.pageNo - 1}">上一页</a>
   </c:if>
   <a href="#">3</a>
   【${requestScope.page.pageNo}】
   <a href="#">5</a>
   <%--如果已经是最后一页，则不显示下一页，末页--%>
   <c:if test="${requestScope.page.pageNo < requestScope.page.pageTotal}">
      <a href="manager/bookServlet?action=page&pageNo=${requestScope.page.pageNo + 1}">下一页</a>
      <a href="manager/bookServlet?action=page&pageNo=${requestScope.page.pageTotal}">末页</a>
   </c:if>
   共${requestScope.page.pageTotal}页，${requestScope.page.totalCount}条记录
   到第<input value="4" name="pn" id="pn_input"/>页
   <input type="button" value="确定">
</div>
```

###   跳转到指定页数功能的实现

>head.jsp页面basePath的修改

```jsp
<%
String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
    pageContext.setAttribute("basePath",basePath);
%>
```

>book_manager.jsp页面的修改

```java
到第<input value="${param.pageNO}" name="pn" id="pn_input"/>页
<input id="searchPageBtn" type="button" value="确定">
<script type="text/javascript">
   $(function () {
      //跳到指定的页码
      $("#searchPageBtn").click(function () {
         let pageNo = $("#pn_input").val();
         // javaScript语言中提供了一个location地址栏对象
         // 它有一个属性叫href.它可以获取浏览器地址栏中的地址
         // href属性可读，可写
         location.href = "${pageScope.basePath}manager/bookServlet?action=page&pageNo="+pageNo;
      });
   });
</script>
```

>数据边界的有效检查

```java
public Integer getPageNo() {
    //如果当前页码小于1，则返回1
    if (pageNo < 1){
        return 1;
        //如果当前页码大于总页数，则返回总页数
    }else if (pageNo > getPageTotal()){
        return getPageTotal();
    }else {
        //如果当前页码大于等于1，小于等于总页数，则返回当前页码
        return pageNo;
    }
}
```

###   页码 1,2,【3】,4,5 的显示

- 需求：显示 5 个连续的页码，而且当前页码在中间。除了当前页码之外，每个页码都可以点击跳到指定页。

>情况 1：如果总页码小于等于 5 的情况，页码的范围是：1~ 总页码



>情况 2：总页码大于 5 的情况。假设一共 10 页。
>
>>小情况 1：当前页码为前面 3 个：1，2，3 的情况，页码范围是：1-5。
>
>- 【1】2，3，4，5
>- 1【2】3，4，5
>- 1，2【3】4，5
>
>>小情况 2：当前页码为最后 3 个，8，9，10，页码范围是：总页码减 4 - 总页码。
>
>- 6，7【8】9，10
>- 6，7，8【9】10
>
>- 6，7，8，9【10】
>
>>小情况 3：4，5，6，7，页码范围是：当前页码减 2 - 当前页码加 2
>
>- 2，3，4，5，6
>- 3，4，5，6，7
>- 4，5，6，7，8
>- 5，6，7，8，9

####  <c:forEach 

```jsp
<%--遍历1到10，输出
       begin属性设置开始的索引
       end 属性设置结束的索引
       var 属性表示循环的变量(也是当前正在遍历到的数据)
       for (int i = 1; i < 10; i++)--%>
<c:forEach begin="1" end="10" var="i"> </c:forEach>
```

####  <c:choose

```jsp
<%--
   <c:choose> <c:when> <c:otherwise>标签
    作用：多路判断。跟switch ... case .... default非常接近
    choose标签开始选择判断
    when标签表示每一种判断情况
        test属性表示当前这种判断情况的值
    otherwise标签表示剩下的情况
    <c:choose> <c:when> <c:otherwise>标签使用时需要注意的点：
        1、标签里不能使用html注释，要使用jsp注释
        2、when标签的父标签一定要是choose标签
--%>
```

```jsp
<c:choose>
   <%--情况1：如果总页码小于等于5的情况，页码的范围是：1-总页码--%>
   <c:when test="${requestScope.page.pageTotal <= 5}">
      <c:set var="begin" value="1"/>
      <c:set var="end" value="${requestScope.page.pageTotal}"/>
   </c:when>
   <%--情况2：总页码大于5的情况.假设一共10页--%>
   <c:when test="${requestScope.page.pageTotal > 5}">
      <c:choose>
         <%--小情况1：当前页码为前面3个：1，2，3的情况，页码范围是：1-5.--%>
         <c:when test="${requestScope.page.pageNo <= 3}">
            <c:set var="begin" value="1"/>
            <c:set var="end" value="5"/>
         </c:when>
         <%--小情况2：当前页码为最后3个，8，9，10，页码范围是：总页码减4-总页码--%>
         <c:when test="${requestScope.page.pageNo > requestScope.page.pageTotal-3}">
            <c:set var="begin" value="${requestScope.page.pageTotal-4}"/>
            <c:set var="end" value="${requestScope.page.pageTotal}"/>
         </c:when>
         <%--小情况3：4，5，6，7，页码范围是：当前页码减2-当前页码加2--%>
         <c:otherwise>
            <c:set var="begin" value="${requestScope.page.pageNo-2}"/>
            <c:set var="end" value="${requestScope.page.pageNo+2}"/>      
         </c:otherwise>
      </c:choose>
   </c:when>
</c:choose>
<c:forEach begin="${begin}" end="${end}" var="i">
   <c:if test="${i==requestScope.page.pageNo}">
      【${i}】
   </c:if>
   <c:if test="${i != requestScope.page.pageNo}">
      <a href="manager/bookServlet?action=page&pageNo=${i}">${i}</a>
   </c:if>
</c:forEach>
```

###  修改分页对原来添加，删除，修改的影响

- 以前的add、delete、modify操作后重定向……action=list；如今要修改为action=page
  - 修改完成后，添加数据成功后，还是显示的第一页，
  - 想要完成添加数据后自动跳到最后一页查看新添加的数据

```jsp
book_manager.jsp
<td><a href="pages/manager/book_edit.jsp">添加图书</a></td>--%>
修改为：				
<td><a href="pages/manager/book_edit.jsp?pageNo=${requestScope.page.pageTotal}">添加图书</a></td>
<td><a class="deleteClass" href="manager/bookServlet?action=delete&id=${book.id}&pageNo=${requestScope.page.pageNo}">删除</a></td>

book_edit.jsp
<form action="manager/bookServlet" method="get">
增加：    <input type="hidden" name="pageNo" value="${param.pageNo}">
```

```java
 protected void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //由于添加一条数据后，有可能页码正好加1,导致不会真正显示最后一条数据所在的页面
        int pageNo = WebUtils.parseInt(req.getParameter("pageNo"), 0);
        pageNo+=1;
        //1、获取请求的参数==封装成为Book对象
        Book book = WebUtils.copyParamToBean(req.getParameterMap(), new Book());
        bookService.addBook(book);
        //3、跳到图书列表页面
//        req.getRequestDispatcher("/pages/manager/book_manager.jsp").forward(req, resp);
        //这里不能使用转发操作，会出现bug，要使用重定向。
//        resp.sendRedirect(req.getContextPath() + "/manager/bookServlet?action=list");
        resp.sendRedirect(req.getContextPath() + "/manager/bookServlet?action=page&pageNo="+pageNo);
    }
```

###  前后台分页的初步实现

>2021年10月18日08:28:58  首页index.jsp的跳转
>
>>需求：web目录下的index.jsp页面 需要查询好的分页数据，然后使用JSTL标签库遍历输出到页面上
>>
>>局限：需要分页数据要通过ClientBookServlet程序处理分页，如果直接访问：http://localhost:8080/book/clientBooServlet?action=page  路径太长
>>
>>解决：web目录下的index.jsp页面请求转发到ClientBookServlet程序，然后再转发到web/pages/client/index.jsp页面

 ```java
 //①在web下新建ClientBookServlet.java文件:
 public class ClientBookServlet extends BaseServlet {
     //处理分页功能
     …………
     //4 请求转发到pages/client/index.jsp页面
     req.getRequestDispatcher("/pages/client/index.jsp").forward(req,resp);    
 }
 //②WEB-INF文件夹下的web.xml文件配置:
 <url-pattern>/client/bookServlet</url-pattern>
 //③在page目录下新建client文件夹，在此文件夹下新建index.jsp文件：
 <c:forEach items="${requestScope.page.items}" var="book"></c:forEach>
 //④将web文件夹下的index.jsp文件修改：<%--只负责请求转发--%> 
 <jsp:forward page="/client/bookServlet?action=page"></jsp:forward>
 ```

###  分页条的抽取

> 抽取分页条中请求地址为url变量：
>
> - 前台的分页条和后台的分页条除了请求地址client/manager不同之外，其他的完全一样。

```java
//1.在page对象中添加url属性,并重新生成get、set方法与tostring:
//2.在Servlet程序的page分页方法中设置url的分页请求地址:
	//（分页条的抽取）设置前台的url
    page.setUrl("client/bookServlet?action=page");
	//（分页条的抽取）设置后台的url
	page.setUrl("manager/bookServlet?action=page");
//3.修改分页条中请求地址为url变量输出,并抽取一个单独的jsp页面(选中修改目标Ctrl+R)
	//以后台manager为例
	<a href="manager/bookServlet?action=page&pageNo=1">首页</a>
 改为：<a href="${requestScope.page.url}&pageNo=1">首页</a>
//4.提取出来前后台的分页条为：page_nav.jsp页面，然后静态包含于前后台页面。
```

###   首页价格搜索

>需求：显示价格在[min,max]区间的图书数据
>
>- 求满足区间的总记录数：select count(*) from t_book2 where price between min and max;
>- 显示价格区间内的数据：select * from t_book2 where price between min and max limit begin,size

```java
//1.修改client文件夹下的index.jsp文件：
<form action="client/bookServlet" method="get">
<input type="hidden" name="action" value="pageByPrice">
//2.ClientBookServlet.java文件添加如下代码：
	//首页价格搜索
    protected void pageByPrice(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1 获取请求的参数 pageNo 和 pageSize /min/max
        int pageNo = WebUtils.parseInt(req.getParameter("pageNo"), 1);
        int pageSize = WebUtils.parseInt(req.getParameter("pageSize"), Page.PAGE_SIZE);
        //价格的最小值和最大值
        int min = WebUtils.parseInt(req.getParameter("min"), 0);
        int max = WebUtils.parseInt(req.getParameter("max"), Integer.MAX_VALUE);
        //2 调用bookService.pageByPrice(pageNo,pageSize,min,max)：Page对象
        Page<Book> page = bookService.pageByPrice(pageNo, pageSize, min, max);
        page.setUrl("client/bookServlet?action=pageByPrice");
        //3 保存Page对象到Request域中
        req.setAttribute("page", page);
        //4 请求转发到pages/client/index.jsp页面
        req.getRequestDispatcher("/pages/client/index.jsp").forward(req, resp);
    }
//3.在BookService.java文件下，新建如下方法:
    Page<Book> pageByPrice(int pageNo, int pageSize, int min, int max);
//4.继续修改BookServiceImpl.java
 	//首页价格区间搜索
    @Override
    public Page<Book> pageByPrice(int pageNo, int pageSize, int min, int max) {
        ……
        //求总记录数
        Integer totalCount = bookDao.queryForTotalCountByPrice(min,max);
        //求当前页数据
        List<Book> items = bookDao.queryForPageItemsByPrice(begin, pageSize,min,max);
    }
//5.在BookDao.java添加如下代码:
Integer queryForTotalCountByPrice(int min, int max);
List<Book> queryForPageItemsByPrice(int begin, int pageSize, int min, int max);
//6.在BookDaoImpl.java文件下添加如下：
@Override
    public Integer queryForTotalCountByPrice(int min, int max) {
        String sql = "select count(*) from t_book2 where price between ? and ?";
        Number count = (Number) queryForSingleValue(sql,min,max);
        return count.intValue();
    }
    @Override
    public List<Book> queryForPageItemsByPrice(int begin, int pageSize, int min, int max) {
        String sql = "select `id`,`name`,`author`,`price`,`sales`,`stock`,`img_path` imgPath " +
                "from t_book2 where price between ? and ? order by price limit ?,?";
        return queryForList(Book.class, sql, min, max, begin, pageSize);
    }
//7.局部具体测试：
	@Test
    public void queryForTotalCountByPrice(){
        System.out.println(bookDao.queryForTotalCountByPrice(10,50));
    }
    @Test
    public void queryForPageItemsByPrice(){
        for (Book book : bookDao.queryForPageItemsByPrice(1, 4,10,50)){
            System.out.println(book);
        }
    }
```

>> 搜索价格区间的回显:
>
>- 价格：<input id="min" type="text" name="min" value="${param.min}">
>
>>解决分页条中不带价格区间的bug
>
>- 如果查到的数据有两页，点击下一页时，立马跳转到全部数据，失去查询到的结果

 ```java
 //向ClientBookServlet.java文件中的调用pageByPrice方法后面添加如下代码：
 //设置url请求地址的时候要带上价格区间
 //        page.setUrl("client/bookServlet?action=pageByPrice");
         StringBuilder sb = new StringBuilder("client/bookServlet?action=pageByPrice");
         //如果有最小价格参数，追加到分页条的地址参数中
         if (req.getParameter("min") != null) {
             sb.append("&min=").append(req.getParameter("min"));
         }
         //如果有最大价格参数，追加到分页条的地址参数中
         if (req.getParameter("max") != null) {
             sb.append("&max=").append(req.getParameter("max"));
         }
         page.setUrl(sb.toString());
 ```

##   购物车模块

2021年10月19日21:03:17

>实现技术版本：Session版本，把购物车信息保存到Session域中。没有与数据库交互，所以不需要DAO。

###   购物车模型的创建

```java
public class Cart {
//    private Integer totalCount;
//    private BigDecimal totalPrice;
    //    private List<CartItem> items = new LinkedList<CartItem>();
    //key是商品编号，value是商品信息。使用LinkedHashMap是为了保证购物车中添加图书的顺序。
    private Map<Integer, CartItem> items = new LinkedHashMap<Integer, CartItem>();
    //添加功能方法：
    public void addItem(CartItem cartItem) {
        //不能直接items.add()，要先查看购物车中是否已经添加过此商品，如果已添加，则数额累加，总金额更新
        //如果没有添加过(通过对比编号)，直接放到集合中即可
//        for (CartItem item : items) {
//            cartItem.getId() == item.getId()
//        }
        CartItem item = items.get(cartItem.getId());
        if (item == null) {
            //之前没有添加过此商品
            items.put(cartItem.getId(), cartItem);
        } else{
            //已经添加过的情况
            item.setCount(item.getCount() + 1);
            //更新总金额，乘法multiply()
            item.setTotalPrice(item.getPrice().multiply(new BigDecimal(item.getCount())));
        }
    }

    public void deleteItem(Integer id) {
        items.remove(id);
    }

    public void clear() {
        items.clear(); //直接清空集合
    }

    public void updateCount(Integer id,Integer count) {
        //先查看购物车中是否有此商品，如果有，修改此商品数量，更新总金额。
        CartItem cartItem = items.get(id);
        if (cartItem != null) {
            cartItem.setCount(count);  //修改商品数量
            cartItem.setTotalPrice(cartItem.getPrice().multiply(new BigDecimal(cartItem.getCount())));
        }
    }

    public Integer getTotalCount() {
        Integer totalCount = 0;
        for (Map.Entry<Integer, CartItem> entry : items.entrySet()) {
            totalCount += entry.getValue().getCount();
        }
        return totalCount;
    }
    
    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = new BigDecimal(0);
        for (Map.Entry<Integer, CartItem> entry : items.entrySet()) {
            //当前商品的总金额 + 每个商品的总金额
            totalPrice = totalPrice.add(entry.getValue().getTotalPrice());
        }
        return totalPrice;
    }
    //…………
    @Override
    public String toString() {
        return "Cart{" +
                "totalCount=" + getTotalCount() +
                ", totalPrice=" + getTotalPrice() +
                ", items=" + items +
                '}';
    }
} //CartTest.java 
```

###   加入购物车addItem

>>client下的index.jsp页面
>
>```javascript
><script type="text/javascript">
>		$(function () {
>			$("button.addToCart").click(function () {
>				//在事件响应的function函数中，有一个this对象，这个this对象
>				//是当前正在响应事件的dom对象
>				//attr() 可以设置和获取属性的值
>				let bookId = $(this).attr("bookId");
>				location.href = "http://localhost:8080/bookStore/cartServlet?action=addItem&id=" + bookId;
>			});
>		});
></script>
>
><div class="book_add">
>   <button bookId="${book.id}" class="addToCart">加入购物车</button>
></div>
>```
>
>>CartServlet中的addItem方法
>
>- 在HTTP协议中有一个请求头，叫Referer,它可以把请求发起时，浏览器地址栏中的地址发送给服务器。
>- System.out.println("请求头 Referer 的值：" + req.getHeader("Referer"));

```java
public class CartServlet extends BaseServlet{
    BookService bookService = new BookServiceImpl();
    //1.加入购物车
    protected void addItem(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求的参数，商品编号
        int id = WebUtils.parseInt(req.getParameter("id"), 0);
        //调用bookService.queryBookById(id):Book 得到图书信息
        Book book = bookService.queryBookById(id);
        //把图书信息，转换成为CartItem商品项
        CartItem cartItem = new CartItem(book.getId(), book.getName(), 1, book.getPrice(), book.getPrice());
        //调用Cart.addItem(CartItem)；添加商品项
//        Cart cart = new Cart(); //采用这种写法会导致totalCount始终为1，因为会创建多个Cart对象
        Cart cart = (Cart)req.getSession().getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            req.getSession().setAttribute("cart",cart);
        }
        cart.addItem(cartItem);
        //重定向回商品列表页面
//        resp.sendRedirect(req.getContextPath()); //此种写法有bug,如果点击第二页的“加入购物车“按钮，还是会跳到首页
        resp.sendRedirect(req.getHeader("Referer"));
    }
}
```

###   购物车的展示

2021年10月21日16:20:10

```jsp
<%--cart.jsp--%>
<div id="main" class="box_order">
		<table>
			<tr>
				<td>商品名称</td>
				<td>数量</td>
				<td>单价</td>
				<td>金额</td>
				<td>操作</td>
			</tr>
			<%--购物车为空的情况下--%>
			<c:if test="${empty sessionScope.cart.items}">
				<tr>
					<td colspan="5"><a href="index.jsp">当前购物车为空,点击继续浏览商品吧~</a></td>
				</tr>
			</c:if>
			<%--购物车非空的情况下--%>
			<c:if test="${not empty sessionScope.cart.items}">
				<c:forEach items="${sessionScope.cart.items}" var="entry">
					<tr>
						<td>${entry.value.name}</td>
						<td>${entry.value.count}</td>
						<td>${entry.value.price}</td>
						<td>${entry.value.totalPrice}</td>
						<td><a class="deleteItem" href="cartServlet?action=deleteItem&id=${entry.value.id}">删除</a></td>
					</tr>
				</c:forEach>
			</c:if>
		</table>
		<%--购物车非空的情况下才输出下面的内容--%>
		<c:if test="${not empty sessionScope.cart.items}">
			<div class="cart_info">
				<span class="cart_span">购物车中共有<span class="b_count">${sessionScope.cart.totalCount}</span>件商品</span>
				<span class="cart_span">总金额<span class="b_price">${sessionScope.cart.totalPrice}</span>元</span>
				<span class="cart_span"><a href="#">清空购物车</a></span>
				<span class="cart_span"><a href="#">继续购物</a></span>
				<span class="cart_span"><a href="pages/cart/checkout.jsp">去结账</a></span>
			</div>
		</c:if>
</div>
```

###   删除&清空购物车商品项

```java
//cart.jsp
<a class="deleteItem" href="cartServlet?action=deleteItem&id=${entry.value.id}">删除</a>
<a id="clearCart" href="cartServlet?action=clear">清空购物车</a>
<script type="text/javascript">
		$(function () {
			//删除的确认提示操作给删除事件绑定单击事件
			$("a.deleteItem").click(function () {
				return confirm("你确定要删除【" + $(this).parent().parent().find("td:first").text() + "】吗？");
			});
			//给清空购物车绑定单击事件
			$("#clearCart").click(function () {
				return confirm("你确定要清空购物车吗？");
			});
		});
</script>   
//2.删除购物车商品项
    protected void deleteItem(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取商品编号
        int id = WebUtils.parseInt(req.getParameter("id"), 0);
        //获取购物车对象
        Cart cart = (Cart) req.getSession().getAttribute("cart");
        if (cart != null) {
            //删除了购物车商品项
            cart.deleteItem(id);
            //重定向回原来购物车展示页面
            resp.sendRedirect(req.getHeader("Referer"));
      }
	}
//3.清空购物车
    protected void clear(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cart cart = (Cart) req.getSession().getAttribute("cart");
        if (cart != null) {
            cart.clear();
            resp.sendRedirect(req.getHeader("Referer"));
        }
    }
```

###   修改购物车商品数量

```java
//4.修改购物车商品数量
    protected void updateCount(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = WebUtils.parseInt(req.getParameter("id"), 0);
        int count = WebUtils.parseInt(req.getParameter("count"), 1);
        Cart cart = (Cart) req.getSession().getAttribute("cart");
        if (cart != null) {
            //修改商品数量
            cart.updateCount(id, count);
            resp.sendRedirect(req.getHeader("Referer"));
        }
    }

//cart.jsp
<td><input class="updateCount" style="width:80px;" bookId="${entry.value.id}"type="text" value="${entry.value.count}"></td>
    
//给数量修改框绑定onchange内容发生改变事件
			$("input.updateCount").change(function () {
				let name = $(this).parent().parent().find("td:first").text();
				let id = $(this).attr("bookId");
				//获取商品数量
				let count = this.value;
				if (confirm("你确定将【" + name + "】商品数量修改为：" + count + "吗？")) {
					location.href="http://localhost:8080/bookStore/cartServlet?action=updateCount&count="+count+"&id="+id;
				} else{
					// defaultValue 属性是表单项 Dom 对象的属性。它表示默认的 value 属性值。
					this.value = this.defaultValue;
				}
			});    
```

###   购物车数据回显

>在cartServlet.java中的重定向回商品列表页面之前：添加
>
>//购物车数据回显:保存最后一个添加的商品名称
>`req.getSession().setAttribute("lastName",cartItem.getName());`

```javascript
<div style="text-align:center">
	<c:if test="${empty sessionScope.cart.items}">
	<%--购物车为空的输出--%>
		<div>
			<span style="color:	red">当前购物车为空</span>
		</div>
	</c:if>
	<c:if test="${not empty sessionScope.cart.items}">
	<%--购物车为非空的输出--%>
		<div class="cont">
			<span>您的购物车中有${sessionScope.cart.totalCount}件商品</span>
			<div>
			您刚刚将<span style="color: red">${sessionScope.lastName}</span>加入到了购物车中
			</div>
		</div>
	</c:if>
</div>
```

##   订单模块

>> 创建订单模块的数据库表
>
>```sql
>CREATE TABLE t_order(
>	`order_id` VARCHAR(50) PRIMARY KEY,
>	`create_time` DATETIME,
>	`price` DECIMAL(11,2),
>	`status` INT,
>	`user_id` INT,
>	FOREIGN KEY(`user_id`) REFERENCES t_user(`id`)
>);
>CREATE TABLE t_order_item(
>	`id` INT PRIMARY KEY AUTO_INCREMENT,
>	`name` VARCHAR(100),
>	`count` INT,
>	`price` DECIMAL(11,2),
>	`total_price` DECIMAL(11,2),
>	`order_id` VARCHAR(50),
>	FOREIGN KEY(`order_id`) REFERENCES t_order(`order_id`)
>);
>```
>
>>创建订单块的数据模型
>
>```java
>public class Order {
>private String orderId;
>private Date createTime;
>private BigDecimal price;
>// 0 未发货，1 已发货，2 表示已签收
>private Integer status = 0;
>private Integer userId;
>
>public class OrderItem {
>private Integer id;
>private String name;
>private Integer count;
>private BigDecimal price;
>private BigDecimal totalPrice;
>private String orderId;
>```
>
>>编写订单模块的 DAO,Service程序和测试
>
>```java 
>public class OrderDaoImpl extends BaseDao implements OrderDao {
>  @Override
>  public int saveOrder(Order order) {
>      String sql = "insert into t_order2(`order_id`,`create_time`,`price`,`status`,`user_id`) values(?,?,?,?,?)";
>      return update(sql,order.getOrderId(),order.getCreateTime(),order.getPrice(),order.getStatus(),order.getUserId());
>  }
>}
>public class OrderItemDaoImpl extends BaseDao implements OrderItemDao {
>  @Override
>  public int saveOrderItem(OrderItem orderItem) {
>      String sql = "insert into t_order_item2(`order_id`,`name`,`count`,`price`,`total_price`) values(?,?,?,?,?)";
>      return update(sql,orderItem.getOrderId(),orderItem.getName(),orderItem.getCount(),orderItem.getPrice(),orderItem.getTotalPrice());
>  }
>}
>public class OrderServiceImpl implements OrderService {
>  private OrderDao orderDao = new OrderDaoImpl();
>  private OrderItemDao orderItemDao = new OrderItemDaoImpl();
>  @Override
>  public String createOrder(Cart cart, Integer userId) {
>      //订单号（唯一性）
>      String orderId = System.currentTimeMillis() + "" + userId;
>      //创建一个订单对象
>      Order order = new Order(orderId,new Date(),cart.getTotalPrice(),0,userId);
>      //保存订单
>      orderDao.saveOrder(order);
>      // 遍历购物车中每一个商品项转换成为订单项保存到数据库
>      for (Map.Entry<Integer, CartItem> entry : cart.getItems().entrySet()) {
>          // 获取每一个购物车中的商品项
>          CartItem cartItem = entry.getValue();
>          // 转换为每一个订单项
>          OrderItem orderItem = new OrderItem(null, orderId, cartItem.getName(), cartItem.getCount(), cartItem.getPrice(), cartItem.getTotalPrice());
>          // 保存订单项到数据库
>          orderItemDao.saveOrderItem(orderItem);
>      }
>      //清空购物车
>      cart.clear();
>      return orderId;
>  }
>}
>public class OrderServiceTest {
>   @Test
>   public void createOrder() {
>       Cart cart = new Cart();
>       cart.addItem(new CartItem(1,"小塔山",1,new BigDecimal(100),new BigDecimal(100)));
>       cart.addItem(new CartItem(1,"小塔山",1,new BigDecimal(100),new BigDecimal(100)));
>       cart.addItem(new CartItem(2,"小龟山",1,new BigDecimal(50),new BigDecimal(100)));
>       OrderService orderService = new OrderServiceImpl();
>       System.out.println("订单号是：" + orderService.createOrder(cart,1));
>   }
>}
>
>public class OrderServlet extends BaseServlet{
>   private OrderService orderService = new OrderServiceImpl();
>   protected void createOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
>       //先获取Cart购物车对象
>       Cart cart = (Cart) req.getSession().getAttribute("cart");
>       //如果用户未登录
>       User loginUser = (User) req.getSession().getAttribute("user");
>       if (loginUser == null) {
>           req.getRequestDispatcher("/pages/user/login.jsp").forward(req, resp);
>           return;
>       }
>       Integer userId = loginUser.getId();
>       String orderId = orderService.createOrder(cart,userId);
>       req.getSession().setAttribute("orderId",orderId);
>       resp.sendRedirect(req.getContextPath()+"/pages/cart/checkout.jsp");
>   }
>}
><span class="cart_span"><a href="orderServlet?action=createOrder">去结账</a></span>
>```

## 权限检查

>**ThreadLocal**：由于在分布式多线程环境下，如果希望处理数据库事务时能有回滚操作，就必须保证操作前后是针对于同一个数据库连接进程。因此，考虑到`ThreadLocal`的特性，这里可以在/utils/中的的`JDBCUtils`定义一个静态变量
>
>private static ThreadLocal<Connection> conns = new ThreadLocal<Connection>();
>
>- 每个ThreadLocal对象实例定义时，一般都是static类型
>- ThreadLocal中保存的数据，在线程销毁后，会由JVM虚拟机释放
>
>说明：当创建了一个数据库连接后，就将其存储在当前线程的**线程域**中，之后设置事务，执行操作以及关闭时都从该线程域中获取对应的数据库连接，从而保证了**前后都是同一个数据库连接**（事务处理要求）

###  Filter拦截manager页面的内容

```java
public class ManagerFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        Object user = httpServletRequest.getSession().getAttribute("user");
        if (user == null) {
            httpServletRequest.getRequestDispatcher("/pages/user/login.jsp").forward(servletRequest, servletResponse);
        } else{
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
    @Override
    public void destroy() {}
}
```

```xml
<filter>
    <filter-name>ManagerFilter</filter-name>
    <filter-class>com.jun.filter.ManagerFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>ManagerFilter</filter-name>
    <url-pattern>/pages/manager/*</url-pattern>
    <url-pattern>/manager/bookServlet</url-pattern>
</filter-mapping>
```

###   JdbcUtils工具类的修改

>使用 ThreadLocal 来确保所有 dao 操作都在同一个 Connection 连接对象中完成。

```java
public class JdbcUtils {
    private static DruidDataSource datasource;
    private static ThreadLocal<Connection> conns = new ThreadLocal<Connection>();   
    static {
        try {
            Properties pro = new Properties();
            InputStream ins = JdbcUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
            pro.load(ins);
//            ins.close();
            datasource = (DruidDataSource)DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection(){
        Connection connect = conns.get();
        if (connect == null) {
            try {
                connect = datasource.getConnection();
                //保存到ThreadLocal对象中,供后面的JDBC使用
                conns.set(connect);
                connect.setAutoCommit(false);//设置为手动管理事务
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connect;
    }
    public static void commitAndClose() {
        Connection connection = conns.get();
        if (connection != null) {
            //如果不等于null，说明之前使用过连接，操作过数据库
            try {
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        //一定要执行remove操作，否则会出错。（因为Tomcat服务器底层使用了线程池技术）
        conns.remove();
    }
    public static void rollbackAndClose() {
        Connection connection = conns.get();
        if (connection != null) {
            //如果不等于null，说明之前使用过连接，操作过数据库
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }      
        conns.remove();
    }
}    
```

###   BaseDao的修改

```java
public abstract class BaseDao {
    //使用DbUtils操作数据库
    private QueryRunner queryRunner = new QueryRunner();
    //1.通用的增删改操作：update(),执行：Insert\Update\Delete 语句
    public int update(String sql, Object... args) {
        Connection connect = JdbcUtils.getConnection();
        try {
            return queryRunner.update(connect, sql, args);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    //2.查询返回一个 javaBean 的 sql 语句
    public <T>T queryForOne(Class<T> type, String sql, Object... args) {
        Connection connect = JdbcUtils.getConnection();
        try {
            return queryRunner.query(connect, sql, new BeanHandler<T>(type), args);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    //3.查询返回一个 javaBean 的 sql 语句,获取对象集合
    public <T> List<T> queryForList(Class<T> type, String sql, Object... args){
        Connection connect = JdbcUtils.getConnection();
        try {
            return queryRunner.query(connect, sql, new BeanListHandler<T>(type), args);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    //4.执行返回一行一列的 sql 语句,获取某个简单的值
    public Object queryForSingleValue(String sql, Object... args) {
        Connection connect = JdbcUtils.getConnection();
        try {
            return queryRunner.query(connect, sql, new ScalarHandler(), args);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
```

###   Filter统一给所有Service方法都加上try-catch

>单独给每一个xxxService.xxxx()方法加try……catch太麻烦

```java
public class OrderServlet extends BaseServlet{
    private OrderService orderService = new OrderServiceImpl();
    protected void createOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //先获取Cart购物车对象
        Cart cart = (Cart) req.getSession().getAttribute("cart");
        //如果用户未登录
        User loginUser = (User) req.getSession().getAttribute("user");
        if (loginUser == null) {
            req.getRequestDispatcher("/pages/user/login.jsp").forward(req, resp);
            return;
        }
        Integer userId = loginUser.getId();
        
        String orderId = orderService.createOrder(cart,userId);
        
//        String orderId = null;
//        try {
//            orderId = orderService.createOrder(cart,userId);
//            JdbcUtils.commitAndClose();//提交事务
//        } catch (Exception e) {
//            JdbcUtils.rollbackAndClose();//回滚事务
//            e.printStackTrace();
//        }
        req.getSession().setAttribute("orderId",orderId);
        resp.sendRedirect(req.getContextPath()+"/pages/cart/checkout.jsp");
    }
}
```

>一次性给所有请求加上过滤，此时就不需要单独给orderService.createOrder加try……catch了。

```java
public class TransactionFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
            JdbcUtils.commitAndClose();
        } catch (Exception e) {
            JdbcUtils.rollbackAndClose();
            e.printStackTrace();
            throw new RuntimeException(e);//把异常抛给Tomcat管理展示友好的错误页面           
        }
    }
    @Override
    public void destroy() {

    }
}
```

```xml
<filter>
    <filter-name>TransactionFilter</filter-name>
    <filter-class>com.jun.filter.TransactionFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>TransactionFilter</filter-name>
    <!-- /*表示当前工程下的所有请求 -->
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

###   所有异常都统一交给 Tomcat

```xml
<error-page>
    <!--error-code是错误类型-->
    <error-code>404</error-code>
    <!--location标签表示。要跳转去的页面路径-->
    <location>/pages/error/error404.jsp</location>
</error-page>
```

##  Ajax请求验证

>>UserServlet.java
>
>```java
>//3.使用Ajax验证用户名是否可用
>protected void ajaxExistUsername(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
>String username = req.getParameter("username");
>boolean b = userService.existsUsername(username);
>//把返回的结果封装成为map对象
>Map<String, Object> resultMap = new HashMap<String, Object>();
>resultMap.put("existUsername", b);
>Gson gson = new Gson();
>String json = gson.toJson(resultMap);
>resp.getWriter().write(json);
>}
>```
>
>>rejist.jsp
>
>- $.getJSON方法：
>  url			请求的url地址
>  data		发送给服务器的数据
>  callback	成功的回调函数
>
>```javascript
>//使用Ajax验证用户名是否可用
>$("#username").blur(function () {
>  var username = this.value;
>  $.getJSON("http://localhost:8080/bookStore/userServlet", "action=ajaxExistUsername&username="+username, function (data) {
>     if (data.existUsername) {
>        $("span.errorMsg").text("用户名已存在！")
>     } else{
>        $("span.errorMsg").text("用户名可用！")
>     }
>  });
>});
>```
>
>- $.get方法和$.post方法
>  url			请求的url地址
>  data		发送的数据
>  callback	成功的回调函数
>  type		返回的数据类型

###  Ajax修改添加商品到购物车

>由于加入购物车只是针对当前页面的某一个小部分操作，如果将整个页面刷新以获取回传数据及其浪费，因此使用局部刷新对方法，能够最大限度降低资源损耗。

```javascript
index.jsp
<script type="text/javascript">
   // 给加入购物车按钮绑定单击事件
   $(function () {
      $("button.addToCart").click(function () {
         let bookId = $(this).attr("bookId");
         // location.href = "http://localhost:8080/bookStore/cartServlet?action=addItem&id=" + bookId;
         //使用Ajax请求来发
         $.getJSON("http://localhost:8080/bookStore/cartServlet", "action=ajaxAddItem&id=" + bookId, function (data) {
            $("span.cartTotalCount").text("您的购物车中有" + data.totalCount + "件商品");
            $("span.cartLastName").text(data.lastName);
         });
      });
   });
</script>
```

>CartServlet.java

```java
//5.使用Ajax请求修改添加商品到购物车的实现
protected void ajaxAddItem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {        // 获取请求的参数 商品编号
    String ids = request.getParameter("id");
    int id = WebUtils.parseInt(ids, 0);
    // 调用bookService.queryBookById(id):Book得到图书的信息
    Book book = bookService.queryBookById(id);
    // 把图书信息，转换成为CartItem商品项
    CartItem cartItem = new CartItem(book.getId(), book.getName(), 1, book.getPrice(), book.getPrice());
    // 调用Cart.addItem(CartItem);添加商品项
    Cart cart = (Cart) request.getSession().getAttribute("cart");
    if (cart == null) {
        cart = new Cart();
        request.getSession().setAttribute("cart", cart);
    }
    cart.addItem(cartItem);
    System.out.println(cart);
    // 最后一个添加的商品名称
    request.getSession().setAttribute("lastName", cartItem.getName());
    //6、返回购物车总的商品数量和最后一个添加的商品名称
    Map<String, Object> resultMap = new HashMap<>();
    resultMap.put("totalCount", cart.getTotalCount());
    resultMap.put("lastName", cartItem.getName());
    Gson gson = new Gson();
    String json = gson.toJson(resultMap);
    response.getWriter().write(json);
}
```

