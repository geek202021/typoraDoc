# day02_css_javascript(js)

## CSS

### 1. CSS简介

**1.1 CSS全称：Cascading Style Sheets,层叠样式表**

**1.2 主要用于定义HTML样式表，如文本颜色、大小、边框等。**

### 2. CSS语法

**2.1 基本格式**

- 选择器（选择符）{

  ​	属性：属性值；

  ​	属性2：属性值2；

  ​	...

  }

**2.2 基本语法**

- CSS对大小写不敏感，但建议使用小写。不过存在一个特例：class和id属性值对大小写是敏感的。
- 每行代码最好只描述一个属性。

### 3. CSS嵌入方式（书写位置）

**3.1 内联样式表（书写在style属性中）**

- 结构（html）与表现(css)相耦合，不建议使用。

**3.2 内部样式表（书写在style标签中）**

- style标签位置一般在\<head>标签内，\<titile>标签下面。
- 结构与表现简单解耦，可以使用。

**3.3 外部样式表（书写在外部xxx.css文件中）**

- 使用\<link rel="stylesheet"  href="外部css文件路径" type="text/css">标签引入外部样式表
- 结构与表现完全解耦，推荐使用。

> 注意：样式表优先级（就近原则）
>
> ​		内联样式表	>	内部样式表	>	外部样式表



### 4. CSS基本选择器

**4.1 标签选择器**

- 语法：标签名{}

- 代码：

  ```css
  p{
      color: green;
  }
  div{
      color: red;
  }
  ```

**4.2 类选择器**

- 语法：

  - 定义：.类名{}
  - 使用：class=“类名”

- 代码：

  ```css
  .c_r{
      color: red;
  }
  <div class="c_r">第1个div</div>
  ```

**4.3 ID选择器**

- 语法：

  - 定义：#id名{}
  - 使用：id=“id名”

- 代码:

  ```css
  #div3{
      color: green;
      font-size:36px;
  }
  <div id="div3" >第3个div</div>
  ```

> 注意：选择器的优先级
>
> ​	ID选择器	 >	类选择器	>	标签选择器

### 5. CSS中颜色值

**5.1 颜色关键字**

- red    green	blue

**5.2 十六进制**【0-ff】

- 红色：#ff0000或#f00      绿色：#00ff00或#0f0	蓝色：#0000ff或#00f
- 具体颜色值，可参考【颜色对照表】。

**5.3 rgb()【0-255】**

- 红色：rgb(255,0,0)      绿色：rgb(0,255,0)	蓝色：rgb(0,0,255)



## JavaScript(JS)

### 1. JS简介

**1.1 JavaScript前身叫LiveScript**

**1.2 Js是一门基于对象和事件驱动的客户端脚本语言，其主要作用数据校验、为网页添加特效等。**

**1.3 JS特点**

- JS是面向对象，有类似Java的语法结构
- JS是一门弱类型语言，也是一门动态类型语言。
- JS可以跨平台且安全性较强。
- 边解释边执行

**1.4 JS的构成**

- ECMAScript:是一个标准，各浏览器厂商都需要实现此标准。
- DOM:Document  Object  Model,文档对象模型，Js通过DOM对HTML文档进行操作。
- BOM:Browser Object Model,浏览器对象模型，BOM提供一些对象，通过修改对象属性或方法，可以控制浏览器行为。

**1.5 JS—Helloworld**

```JavaScript
<head>
    <!-- JavaScript代码写在Script标签中 -->
    <script type="text/javascript">
        window.onload = function(){//main
            //获取#btnId对应的元素节点
            var btn = document.getElementById("btnId");
            //为#btnId绑定单击响应函数
            btn.onclick = function(){
            //弹出警告框，显示字符串Hello
                   alert("Hello");
            }
        }
    </script>
</head>
<body>
    <button id="btnId">SayHello</button>
</body>
```



### 2. JS基本语法

**2.1 JS嵌入方式（书写位置）**

- 内联JS:

  - 书写在事件属性中，结构与行为相耦合，不建议使用。

  - 代码

    ```html
    <button onclick="alert('hello js2');">点我！！！</button>
    ```

- 内部JS

  - 书写在\<script>标签中,可以使用

  - 代码

    ```javascript
    <script type="text/javascript">
        //main
        window.onload = function () {
            //通过id获取元素
            var btnEle = document.getElementById("btnOne");
            //为元素关联单击事件
            btnEle.onclick = function () {
                //触发指定函数
                alert("Hello JS！！！");
            }
        }
    </script>
    
    <button id="btnOne">点我！！！</button>
    ```

- 外部JS

  - 书写在xxx.js文件中，推荐使用。

  - 需要使用\<script src="demo.js" type="text/javascript">\</script>引入外部js文件

    > 注意：一旦\<script src="">\</script>标签中书写src属性时，此时不能在该\<script>标签中书写js代码。如书写不会报错，但无效。如需继续在当前文件中书写js代码，需要新建一个\<script>标签

    

**2.2 JS变量**

- 使用var关键字，声明变量。(JS是一种弱类型语言，JS也是一种动态类型语言。) 

- 代码

  ```javascript
  var name = "zs";
  var i = 100;
  i = 2.5;
  i = true;
  ```

  > 注意：在JS中可以为变量任意赋值。（包括数据类型）

**2.3 JS中数据类型**

- 基本数据类型
  - 数值型：number
  - 字符型：string
  - 布尔型：boolean
  - null
  - undefined：只声明，未赋值
- 对象类型
  - 普通对象：var obj = new Object();
  - json对象：var jsonObj = {"key":value,"key2":value2,...}
  - 函数对象：function(){}
- 可以使用typeof()验证变量的数据类型

**2.4 JS运算符**

- 算数运算符

- 赋值运算符

- 逻辑运算符

- 三元运算符   模块1?模块2:模块3

- 关系运算符

  - == ：判断数值是否相等
  - ===：判断是否全等（数值&数据类型）

  > 面试题： == 与 === 区别？

**2.5 流程控制**：与java完全一致。

**2.6 数组**

- java中定义数组:   数据类型[]   数组名 = new 数据类型[length];

- js中定义数组:

  - var 数组名 = [初始值];
  - var 数组名 = new Array(初始值);

  > 注意：在JS中，数组中的数据可以是任意数据类型，数组可以自动扩容。



### 3. JS中函数

> 函数与方法关系：当函数定义在类中时，我将函数称之为方法。

**3.1 JS定义函数**

- java中定义方法： public  void methodName([参数列表]){}

- JS中使用function关键字定义函数

  - 普通函数：function  函数名([参数列表]){}

    ```javascript
    function fun1(){
        alert("fun1");
    }
    ```

  - 匿名函数：function([参数列表]){}

    ```javascript
    定义匿名函数
    var f = function(){
        alert("匿名函数");
    }
    f();
    ```

**3.2 JS调用函数**

- JS中调用函数时，不检查形参与实参的匹配情况。

- 实参>形参【以add()函数为例】，忽略多余参数。

- 实参<形参

  - 判断实参的数据类型
    - number:NaN
      - NaN：not a number(不是一个数值)，当我们计算结果不是一个数值型时，返回NaN结果。
    - string:实参+undefined

- JS中函数的重载和重写问题

  - JS中不存在函数的重载与重写

- 在JS中存在一个arguments隐式参数

  - 可以用该参数，获取函数中的任意参数。

  - 代码

    ```javascript
    function add(i,j){
            alert("多余参数："+arguments[2]);
            return (i+j);
        }
        alert(add(2,3,1));      //自动忽略多余参数
    ```

### 4. JS中对象

1. 普通对象：var obj = new Object();

   ```javascript
   var stu = new Object();
   stu.name = "yongqiang22";
   stu.age = 18;
   stu.study = function(){
       alert(this.name+" 正在学习ing...");
   }
   // alert(stu.name+","+stu.age);
   stu.study();
   ```

2. json对象：var jsonObj = {"key":value,"key2":value2,...}

   ```javascript
   //定义json对象
   var stu = {
       "name":"yongqiang",
       "age":19,
       study:function(){
           alert(this.name+"is studing!!!")
       }
   }
   // alert(stu.name+","+stu.age);
   stu.study();
   ```

3. 函数对象：function(){}

   ```javascript
   function fun(){}
   fun.age = 20;
   alert(fun.name+","+fun.age);
   ```

   > this关键字：
   >
   > ​	在Java语言中：当前类的对象
   >
   > ​	在JS语言中：当前（触发）函数的对象

### 5. JS中事件

**5.1 常用事件**

- onload:加载事件
- onclick:单击事件
- onchange:文本改变且失去焦点事件
- onsubmit:提交事件
- onblur:失去焦点事件

**5.2 调用函数注意事项**

- 函数名与函数名()区别

  - 函数名：调用函数的“引用”
  - 函数名():直接触发函数

  ```javascript
  //函数名与函数名()区别
  function fun(){
     alert("sayHello!!!");
  }
  // var fun = function(){
  //     alert("sayHello!!!");
  // }
  var btnEle = document.getElementById("btnId");
  btnEle.onclick = fun;
  ```



### 6. window.onload作用

**6.1 window对象是BOM的核心对象**

**6.2 window.onload=fn:当前文档完全(文本、图片、视频等)加载后执行fn。**



### 7. DOM简介

**7.1 DOM(Document Object Model):文档对象模型-主要将文档封装成对象模型，目的便于js|jQuery操作当前文档。**

**7.2 HTML DOM将HTML文档封装一个“树”结构，这种结构称之为节点树**

**7.3 document对象的本质调用问题：window.document ，而window可以省略**

- window.alert() -> alert()



### 8. DOM查询

**8.1 基于整个文档查询**

- document.getElementById():基于整个文档，通过id获取元素节点。返回单个对象
- document.getElementsByTagName():基于整个文档，通过标签名获取元素节点。返回数组。
- document.getElementsByName()基于整个文档，通过name属性值获取元素节点。返回数组。

**8.2 基于元素节点查询**

- 查询子节点
  - ele.childNodes:基于元素节点，获取所有子节点。(IE版本<=8,不获取空元素)
  - ele.firstChild:基于元素节点，获取第一个子节点。(IE版本<=8,不获取空元素)
  - ele.lastChild:基于元素节点，获取最后一个子节点。(IE版本<=8,不获取空元素)
  - ele.getElementsByTagName():基于元素节点，通过标签名获取子节点。
- 查询父节点
  - ele.parentNode:基于元素节点，获取父节点。
- 查询兄弟节点
  - ele.previousSibling:基于元素节点，获取前一个兄弟元素节点。
  - ele.nextSibling：基于元素节点，获取下一个兄弟元素节点。

**8.3 DOM操作其他API**

- 获取元素节点内部文本值：innerText或innerHTML
  - innerText只识别元素文本
  - innerHTML识别元素HTML
- 获取表单项value值：.value



### 9. 扩展：取消默认行为&常用提示框&location

**9.1 取消控件默认行为：return false**

**9.2 常用提示框**

- alert():普通提示框，只有确定按钮
- **confirm()**:带确定取消按钮的提示框。
- prompt():提示用户输入数据，提示框。

**9.3 JS中的现有对象**

- location

  - href:获取或设置浏览器URL(地址栏)

  - 代码

    ```javascript
    //单击普通按钮，发送请求【跳转01.dom查询.html】
    var btnOneEle = document.getElementById("btnOne");
    btnOneEle.onclick = function(){
       // location.href = "01.dom查询.html";
       location = "http://www.baidu.com/";
    }
    ```



- JavaScript内置对象**
  - Array 、Date、Math、……
- **浏览器对象**
  - window、**location**
- **dom对象**
  - document、body、button……

