# day03_jQuery

### 1. jQuery简介

**1.1 jQuery是一个轻量级的且兼容多浏览器的JavaScript库。**

**1.2 jQuery宗旨是“Write Less , Do More”**
**1.3 jQuery特点：免费开源、出色的DOM封装及强大选择器等。**

### 2. $与$()

**2.1 $ = jQuery，jQuery是函数引用（函数名）**

**2.2 $():触发当前jQuery核心函数**

**2.3 核心函数【四大作用】**

- $(fn):传入参数为匿名函数时，主要作用与window.onload类似。但有区别？

  - $(fn) 与 window.onload区别

    1. 执行时机不同（前者>后者）：前者在当前文档【绘制】成功后执行，后者在当前文档【完全加载】后执行。

    2. 编写次数不同：在当前文档中，前者可以编写多次，后者只可以编写一次（如书写多次，不会报错，后面window.onload会覆盖前面的window.onload）

    3. 是否有简写语法:前者有简写语法，后者无。

       ```javascript
       //简介语法
       $(function(){
          alert("jQuery2");
       })
       //标准语法
       $(document).ready(function(){
          alert("jQuery3");
       });
       ```

- $("选择器")：传入参数为选择器字符串，作用：选择器。

- $("HTML"):传入参数为HTML字符串是，作用：创建元素节点对象。

- $(DOM对象)：传入参数为DOM对象时，作用：将DOM对象转换为jQuery对象。



### 3. DOM对象与jQuery对象转换

**3.1 为什么DOM对象与jQuery对象需要相互转换？**

- 答：因为DOM对象与jQuery对象无法自动调用对方的属性和方法，如需要调用对方属性或方法时，需要类型转换。

**3.2 DOM对象转换jQuery对象方法**

- $(DOM对象)

**3.3 jQuery对象转换DOM对象方法**

- $jqueryObj[0]
- $jqueryObj.get(0)

### 4. jQuery选择器（重点）

**4.1 基本选择器**

- $("#id")：id选择器
- $(".class")：类选择器
- $("标签名")：标签选择器
- $("*")：全局选择器
- $("span,div,.class")：组合选择器

**4.2 层级（层次）选择器**

- $("E F")：后代选择器
- $("E>F")：子代选择器
- $("E+F")：相邻（紧邻）选择器（后面邻居）
- $("E~F")：兄弟（同辈）选择器（后面兄弟）

**4.3 过滤选择器**

- 基本过滤选择器
  - $(":first")：匹配第一个元素节点
  - $(":last")：匹配最后一个元素节点
  - $(":even")：匹配下标为偶数的元素节点（下标从0开始）
  - $(":odd")：匹配下标为基数的元素节点（下标从0开始）
  - $(":eq()")：匹配指定下标的元素节点
  - $(":gt()")：匹配大于指定下标的元素节点
  - $(":lt()")：匹配小于指定下标的元素节点
  - $(":not()")：匹配指定选择器的【取反】元素节点
  - $(":header")：匹配所有标题元素节点（h1,h2,...h6）
  - $(":animated")：匹配所有正在执行动画的元素节点
  - $(":focus")：匹配所有获取焦点的元素节点
- 内容过滤选择器
  - $(":contains()")：匹配包含指定文本的元素
  - $(":has()")：匹配包含指定选择器的元素
  - $(":empty")：匹配空元素（不包含子元素及文本）
  - $(":parent")：匹配非空元素
- 可见性过滤选择器
  - $(":hidden")：匹配所有不可见元素（样式表display:none，type="hidden"）
  - $(":visible")：匹配所有可见元素
- 属性过滤选择器
  - $("selector[属性]")
  - $("[属性=‘值’]")
  - $("[属性!=‘值’]")：匹配属性不等于指定值【如没有当前属性，也属于属性不等指定值范畴】的元素节点
  - $("[属性^=‘值’]")
  - $("[属性$=‘值’]")
  - $("[属性*=‘值’]")
  - $("\[属性=‘值’]\[属性2]\[属性3]")
- 子元素过滤选择器
  - $(":nth-child")
  - $(":first-child")：与【:first】区别？【:first】只能匹配第一个元素节点，【:first-child】为每个父元素匹配第一个元素节点。
  - $(":last-child")
  - $(":only-child")
- 表单过滤选择器
  - $(":input")：匹配所有表单项，所有表单项包括四项（input\select\button\textarea）
  - $(":text")
  - $(":password")
  - $(":radio")
  - $(":checkbox")
  - $(":submit")
  - $(":reset")
  - $(":button")
  - $(":image")
  - $(":file")
  - $(":hidden")
- 表单对象属性过滤选择器
  - $(":enabled")：匹配可用元素
  - $(":disabled")：匹配禁用元素
  - $(":checked")：匹配单选框&复选框选中的元素
  - $(":selected")：匹配下拉列表选中的元素

### 5. jQuery操作DOM

> DOM包含哪些内容？
>
> html、属性、文本、样式

**5.1 jQuery操作文本**

- html()：类似js中的innerHTML
- text()：类似js中的innerText
- val()：类似js中的value

**5.2 jQuery操作属性**

- attr(""):获取属性
- attr("",""):为指定属性赋值

- prop("")：获取指定属性值
- prop("","")：为指定属性赋值

**5.3 jQuery操作节点元素（html）**

- 增
  - 内后
    - append():  A.append(B),将B追加到A元素的内部（后置）
    - appendTo()：A.appendTo(B),将A追加到B元素的内部（后置）
  - 内前
    - prepend():A.prepend(B),将B追加到A元素的内部（前置）
    - prependTo():
  - 外后
    - after():A.after(B),将B追加到A的外部（后置）
    - insertAfter()
  - 外前
    - before()：A.before(B),将B追加到A的外部（前置）
    - insertBefore()
- 删
  - empty()：删除匹配元素的所有子元素（掏空）
  - remove()：删除匹配元素（当前元素节点）
- 改
  - replaceWith()：A.replaceWith(B)，用B元素替换A元素
  - replaceAll()：A.replaceAll(B)，用A元素替换B元素
- 查
  - eq()
  - first()
  - last()
  - filter()
  - is()
  - has()：匹配包含指定子元素的父元素节点
  - add():相当于并集选择器
  - children()：查询子代元素
  - find()：查询后代元素
  - parent():查询父一代元素
  - parents():查询父n代元素
  - next():查询后面第一个兄弟元素
  - nextAll():查询后面所有兄弟元素
  - prev():查询前一个兄弟元素
  - prevAll()：查询前面所有兄弟元素
  - siblings():查询前后所有兄弟元素

**5.4 jQuery操作样式（css）**

- css("")：获取指定样式属性值
- css("","")：为指定样式赋值
- addClass()：添加类样式【前提，必须在使用之前，准备一个类样式表】
- removeClass()：移除类样式

### 6. jQuery常用事件

**6.1 ready(fn):当前文档绘制成功后触发函数，简写语法：$(fn)**

**6.2 click(fn):单击事件**

**6.3 change(fn):文本改变且失去焦点时触发。**

**6.4 live(“事件名”，fn):为指定元素绑定相应事件【即使元素是后面新添加进来的，也有效】**

**6.5 delegate("选择器"，“事件名”，fn):可以理解为live的加强版**

**6.6 mouseover(fn):鼠标移入（悬停）事件**

**6.7 mouseout(fn):鼠标移出事件**



### 7. 正则表达式

**7.1 为什么使用正则表达式？**

- 使用传统方式验证非空数据还可以，但如对数据验证较严格时，推荐使用【正则表达式】

**7.2 概述：正则表达式就是一组验证数据的规则。**

**7.3 正则表达式语法**

- var reg = /验证规则/；  

- var reg = /^[验证字符规则]{n,m}$/;   //{n,m}：验证字符数量

  ```javascript
   var unReg = /a/;                    //包含a字母即可
   var unReg = /^a/;                   //以a字母开头即可
   var unReg = /a$/;                   //以a字母结尾即可
   var unReg = /^[a-zA-Z0-9_]{6,12}$/;           //标准语法
  var unReg = /^[a-zA-Z0-9_]{6}$/; 	//匹配合法字符，6次
  var unReg = /^[a-zA-Z0-9_]{6,}$/; 	//匹配合法字符，大于等于6次
  var unReg = /^[a-zA-Z0-9_]{6,12}$/; 	//匹配合法字符，6次<=次数<=12次
  ```

  

**7.4 正则表达式用法**

- 使用test()：验证数据是否合法，如返回true时，表示数据合法。如返回false时，表示数据不合法。

- 特殊匹配规则

  - 匹配次数

    - *：匹配合法字符任意次，等价于{0，}
    - +：至少匹配合法字符一次，等价于{1，}
    - ?：匹配合法字符零次或一次，等价于{0,1}

  - 匹配规则

    - .    ：代表除了"\n"之外的任意字符，任意字符需要指定：[.|\n]

      > **注意：**如规则中需要指定（.）这个符号时，使用:[\\.]

    - \d：等价于[0-9]

    - \w：等价于[a-zA-Z0-9_]












