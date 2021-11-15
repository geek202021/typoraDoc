##  购物车模块

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
> //购物车数据回显:保存最后一个添加的商品名称
> `req.getSession().setAttribute("lastName",cartItem.getName());`

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
>   @Override
>   public int saveOrder(Order order) {
>       String sql = "insert into t_order2(`order_id`,`create_time`,`price`,`status`,`user_id`) values(?,?,?,?,?)";
>       return update(sql,order.getOrderId(),order.getCreateTime(),order.getPrice(),order.getStatus(),order.getUserId());
>   }
>}
>public class OrderItemDaoImpl extends BaseDao implements OrderItemDao {
>   @Override
>   public int saveOrderItem(OrderItem orderItem) {
>       String sql = "insert into t_order_item2(`order_id`,`name`,`count`,`price`,`total_price`) values(?,?,?,?,?)";
>       return update(sql,orderItem.getOrderId(),orderItem.getName(),orderItem.getCount(),orderItem.getPrice(),orderItem.getTotalPrice());
>   }
>}
>public class OrderServiceImpl implements OrderService {
>   private OrderDao orderDao = new OrderDaoImpl();
>   private OrderItemDao orderItemDao = new OrderItemDaoImpl();
>   @Override
>   public String createOrder(Cart cart, Integer userId) {
>       //订单号（唯一性）
>       String orderId = System.currentTimeMillis() + "" + userId;
>       //创建一个订单对象
>       Order order = new Order(orderId,new Date(),cart.getTotalPrice(),0,userId);
>       //保存订单
>       orderDao.saveOrder(order);
>       // 遍历购物车中每一个商品项转换成为订单项保存到数据库
>       for (Map.Entry<Integer, CartItem> entry : cart.getItems().entrySet()) {
>           // 获取每一个购物车中的商品项
>           CartItem cartItem = entry.getValue();
>           // 转换为每一个订单项
>           OrderItem orderItem = new OrderItem(null, orderId, cartItem.getName(), cartItem.getCount(), cartItem.getPrice(), cartItem.getTotalPrice());
>           // 保存订单项到数据库
>           orderItemDao.saveOrderItem(orderItem);
>       }
>       //清空购物车
>       cart.clear();
>       return orderId;
>   }
>}
>public class OrderServiceTest {
>    @Test
>    public void createOrder() {
>        Cart cart = new Cart();
>        cart.addItem(new CartItem(1,"小塔山",1,new BigDecimal(100),new BigDecimal(100)));
>        cart.addItem(new CartItem(1,"小塔山",1,new BigDecimal(100),new BigDecimal(100)));
>        cart.addItem(new CartItem(2,"小龟山",1,new BigDecimal(50),new BigDecimal(100)));
>        OrderService orderService = new OrderServiceImpl();
>        System.out.println("订单号是：" + orderService.createOrder(cart,1));
>    }
>}
>
>public class OrderServlet extends BaseServlet{
>    private OrderService orderService = new OrderServiceImpl();
>    protected void createOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
>        //先获取Cart购物车对象
>        Cart cart = (Cart) req.getSession().getAttribute("cart");
>        //如果用户未登录
>        User loginUser = (User) req.getSession().getAttribute("user");
>        if (loginUser == null) {
>            req.getRequestDispatcher("/pages/user/login.jsp").forward(req, resp);
>            return;
>        }
>        Integer userId = loginUser.getId();
>        String orderId = orderService.createOrder(cart,userId);
>        req.getSession().setAttribute("orderId",orderId);
>        resp.sendRedirect(req.getContextPath()+"/pages/cart/checkout.jsp");
>    }
>}
><span class="cart_span"><a href="orderServlet?action=createOrder">去结账</a></span>
>```
>

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
>    String username = req.getParameter("username");
>    boolean b = userService.existsUsername(username);
>    //把返回的结果封装成为map对象
>    Map<String, Object> resultMap = new HashMap<String, Object>();
>    resultMap.put("existUsername", b);
>    Gson gson = new Gson();
>    String json = gson.toJson(resultMap);
>    resp.getWriter().write(json);
>}
>```
>
>>rejist.jsp
>
>- $.getJSON方法：
>      url			请求的url地址
>      data		发送给服务器的数据
>      callback	成功的回调函数
>
>```javascript
>//使用Ajax验证用户名是否可用
>$("#username").blur(function () {
>   var username = this.value;
>   $.getJSON("http://localhost:8080/bookStore/userServlet", "action=ajaxExistUsername&username="+username, function (data) {
>      if (data.existUsername) {
>         $("span.errorMsg").text("用户名已存在！")
>      } else{
>         $("span.errorMsg").text("用户名可用！")
>      }
>   });
>});
>```
>
>- $.get方法和$.post方法
>     url			请求的url地址
>     data		发送的数据
>     callback	成功的回调函数
>     type		返回的数据类型

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

