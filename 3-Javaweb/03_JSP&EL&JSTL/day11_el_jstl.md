# day11 EL&JSTL

## EL

### 1. EL简介

**1.1 为什么使用EL:EL可以简化JSP中的表达式代码。**

**1.2 EL全称：Expression Language,是JSP内置的表达式语言。**

**1.3 EL作用：访问域中的数据【对象&对象中的属性&变量】，也可以执行一些简单的运算或判断操作。**

**1.4 EL特点**

- EL在获取数据时，会自动进行数据类型转换。
- EL在输出数据时，如果有数据则输出，如果没有数据【null】,则什么都不输出。
- EL表达式只能用来读取数据，不能对数据进行修改。

### 2. EL使用

**2.1 基本使用**

- ${标识符}：默认从小域向大域进行检索数据，如果始终未检索到数据，则什么都不显示。

- ${el域对象.标识符}：直接从指定域中获取数据

  - el与jsp域对象对照表

    | 域称呼                       | jsp域对象   | el域对象         |
    | ---------------------------- | ----------- | ---------------- |
    | page域（页面域）             | pageContext | pageScope        |
    | request域（请求域）          | request     | requestScope     |
    | session域（会话域）          | session     | sessionScope     |
    | application域（web上下文域） | application | applicationScope |

**2.2 EL获取对象及对象中的属性问题**

- 语法
  - ${el域对象.对象名.属性名}   
  - ${el域对象.对象名["属性"]}
  - ${el域对象.对象名.getXXX()}
- 注意：严格意义属性：封装后的全局变量，称之为属性。el中调用对象中的属性，本质调用的是getXXX()。

```jsp
<%
	Student stu = new Student(1001,"zhangsan");
	session.setAttribute("stu",stu);
%>
jsp:<%=((Student)session.getAttribute("stu")).getName()%>
el:${sessionScope.stu.name} <br>
el:${sessionScope.stu.getName()}
```



### 3. EL中运算符

**3.1 EL中支持java中的运算符，如:算术、关系、逻辑、三元运算符**

**3.2 EL中特有【empty】运算符**

- 语法：${empty 标识符}

- empty:判断空值的运算符，判断数据为空时，返回true。
- empty支持三种空值
  1. “”
  2. null
  3. 空的数组或集合【数组length=0或集合的size==0】

- 判断非空语法：${!empty 标识符}或 ${not empty 标识符}

### 4. EL中11个内置对象【隐含对象、隐式对象】

**4.1 4个域对象**

- **pageScope**
- **requestScope**
- **sessionScope**
- **applicationScope**

**4.2 7个其他对象**

- **pageContext**:jsp中pageContext对象一致，EL中pageContext作用：调用其他八个内置对象。
- **param**:获取请求参数（单个），相当于jsp中的request.getParameter();
- paramValues:获取请求参数（多个）相当于jsp中的request.getParameterValues();【name相同】
- **header**:获取请求头信息（单个），相当于jsp中的request.getHeader();
- headerValues:获取请求头信息（多个）
- **cookie**:获取cookie信息
- initParam:获取web上下文初始化参数，相当于ServletContext中的getInitParameter()

**4.3 记忆技巧**

- jsp：pageContext作用 = el：pageScope【域对象】 + pageContext【获取其他8个对象】
- jsp：request作用 = el：requestScope【域对象】 + param【获取请求参数】 + header【获取请求头信息】

**4.4 扩展问题：在EL中需要request对象的【获取URL信息】功能，怎么办？**【el中需要使用jsp的session对象怎么办？】

- ${pageContext.request.contextPath}
- ${pageContext.session}

## JSTL

### 1. JSTL简介

**1.1 为什么学习JSTL?EL只能代替jsp中的表达式，需要使用JSTL代替Jsp中的脚本。**

**1.2 JSTL全称：JSP Standard Tag Library，JSP标准标签库**

**1.3 JSTL作用**

- JSTL注意用于代替JSP中的脚本代码，提高JSP页面代码的可读性。
- JSTL是以类似【HTML标签】形式，书写Java代码。

**1.4 JSTL组成：共有5组不同功能标签库，需要掌握3组，具体如下所示：**

- 核心标签库

  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

- 格式化标签库

  <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

- 函数库

   <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

### 2. JSTL使用步骤

**2.1 先引入JSTL的jar包，到WEB-INF下**

- taglibs-standard-impl-1.2.1.jar
- taglibs-standard-spec-1.2.1.jar

**2.2 再使用taglib指令，引入需要的功能标签库**

- 如：<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
- prefix:指定标签库前缀
- uri:指定标签库路径

**2.3 使用JSTL标签库**



### 3. JSTL常用标签

- **3.1 核心标签库**

  - **\<c:set>标签**

    - **作用：**\<c:set>标签 用于 添加 或 修改 域中的属性。

    - **属性：**

      - scope是哪个作用域
      - var 是属性名
      - value 是属性值
      - target 是要修改的目标对象
      - property 是要修改或者添加的属性名

    - **示例代码**

      ```jsp
      <%--声明变量--%>
      <c:set scope="" var="" value=""></c:set>
      <%--修改属性值--%>
      <c:set target="" property="" value=""></c:set>
      ```

  - **\<c:out>标签**

    - **作用：**\<c:out>用于计算一个表达式并将结果输出到当前页面。功能类似于JSP表达式<%= %>和EL表达式${}

    - **属性**

      | 属性    | 作用                        | 参数类型 |
      | ------- | --------------------------- | -------- |
      | value   | 要输出的值                  | Object   |
      | default | 当value为null时显示的默认值 | Object   |
      | escaXml | 是否对特殊字符进行转义      | boolean  |

    - **示例代码**

      ```jsp
      <c:out value="${requestScope.stuName2}" default="stuName2 is null!"></c:out>
      
      <c:set value="<a href='#'>超链接</a>" var="temp"></c:set>
      <c:out value="${pageScope.temp}" escapeXml="false"></c:out>
      ```

  - **\<c:if>标签**

    - **作用：**\<c:if>标签 判断表达式的值，如果表达式的值为 true 则执行其主体内容。

    - **属性**

      | **属性** | **描述**               | **是否必要** | **默认值** |
      | -------- | ---------------------- | ------------ | ---------- |
      | test     | 条件                   | 是           | 无         |
      | var      | 用于存储条件结果的变量 | 否           | 无         |
      | scope    | var属性的作用域        | 否           | page       |

    - **示例代码**

      ```jsp
      <c:set var="yOn" value="true"></c:set>
      <c:if test="${pageScope.yOn == true}" var="bool" scope="request">
          正在听课！
      </c:if>
      ```

  - **\<c:choose>、\<c:when>、\<c:otherwise>标签(常用)**

    - **作用：**\<c:choose>标签与Java switch语句的功能一样，用于在众多选项中做出选择。

    - **属性：**

      test属性：条件判断，返回boolean类型

    - **示例代码**

      ```jsp
      <c:set var="age" value="88" scope="request"></c:set>
      <c:choose>
          <c:when test="${requestScope.age < 18}">
              未成年
          </c:when>
          <c:when test="${requestScope.age>=18 and requestScope.age<30}">
              青年
          </c:when>
          <c:when test="${requestScope.age>=30 and requestScope.age<60}">
              中年
          </c:when>
          <c:when test="${requestScope.age>=60 and requestScope.age<80}">
              中老年
          </c:when>
          <c:otherwise>
              老年
          </c:otherwise>
      </c:choose>
      ```

      

  - **\<c:forEach>标签**

    - **作用：**\<c:forEach>标签是迭代一个集合中的对象-可以是数组，也可以是list，也可以是map对象。

    - **属性：**

      | **属性**  | **描述**                                   | **是否必要** | **默认值**   |
      | --------- | ------------------------------------------ | ------------ | ------------ |
      | items     | 要被循环的数据集合-可以使用el表达式        | 否           | 无           |
      | begin     | 开始的索引（0=第一个元素，1=第二个元素）   | 否           | 0            |
      | end       | 最后一个索引（0=第一个元素，1=第二个元素） | 否           | Last element |
      | step      | 每一次迭代的步长                           | 否           | 1            |
      | var       | 代表当前条目的变量名称                     | 否           | 无           |
      | varStatus | 代表循环状态的变量名称                     | 否           | 无           |

    - **示例代码**

      ```jsp
      <%--普通循环--%>
      <c:forEach var="i" begin="1" end="10" step="2">
          ${i}&nbsp;
      </c:forEach>
      <%--增强循环--%>
      <%
          List<Student> list = new ArrayList<Student>();
          list.add(new Student(10001,"zijian"));
          list.add(new Student(10002,"yafei"));
          list.add(new Student(10003,"zhiyuan"));
          list.add(new Student(10004,"admin"));
          request.setAttribute("list",list);
      %>
      <c:forEach items="${requestScope.list}" var="stu">
          ${stu.id} &emsp; ${stu.name2}<br>
      </c:forEach>
      ```

  

  **3.2 其他标签库or函数库**

  - **格式化标签库之fmt**

    - **作用：**格式化数据，如日期、数字等

    - **属性：**

      - value:格式化数据
      - pattern：格式化规则

    - **示例代码**

      ```jsp
      <%
          Date date = new Date();
          request.setAttribute("date",date);
      %>
      <fmt:formatDate value="${requestScope.date}" pattern="yyyy-MM-dd hh:mm:ss"></fmt:formatDate>
      ```

  - **函数库**

    - **作用：**类似java中String作用

    - **示例代码**

      ```jsp
      ${fn:contains("HEllo", "he")}
      结果:false
      ```

      

  













