#  Typora的使用

[Typora的使用-GitHub](https://github.com/younghz/Markdown)

[Typora的使用-CSDN](https://blog.csdn.net/weixin_39751195/article/details/109971095)

[中文文案排版指南](https://github.com/sparanoid/chinese-copywriting-guidelines/blob/master/README.zh-CN.md)

[程序员鱼皮](https://www.code-nav.cn/resources)     [Java学习路线by-yupi](https://doc.code-nav.cn/roadmap/java)

[Markdown表情包](https://www.webfx.com/tools/emoji-cheat-sheet/)

- 段落的前后要有空行，所谓的空行是指没有文字内容。若想在段内强制换行的方式是使用**两个以上**空格加上回车（引用中换行省略回车）。
- 在段落的每行或者只在第一行使用符号`>`,还可使用多个嵌套引用，如：\> 区块引用 \>> 嵌套引用
- 在强调内容两侧分别加上`*`或者`_`，如：*斜体*   **粗体**  ~~删除线的内容~~ 或按快捷键Alt+Shift+5
- **分割线**最常使用就是三个或以上`*`，还可以使用`-`和`_`和+。**【回车】**
- 符号`: 起到标记作用。如： :`` ctrl+a``
- []() 行内式链接  ；
- **使用表情** : + 表情单词(| 注意，英文冒号之后紧跟一个单词，支持智能提示)
- **插入代码块**\``` + 对应编程语种

>常用文本编辑快捷键：

- home键：光标回到行首；end：光标回到行尾
- ctrl + home键：光标回到文章开始；ctrl + end键：光标回到文章末尾
- shift + home 或 end 键：选中一行
- 鼠标双击：选中一个单词 / 鼠标连续击3次：选中一行
- ctrl+ shift + 右箭头或左箭头：选中一个单词
- Ctrl + F 搜索 ；Ctrl + H 替换

---

#  SQL基本命令行操作

##  1、链接数据库

命令行链接：(ctrl+shift+c，注释：--)

```sql
mysql -u root -phrj -- 链接数据库
update mysql.user set authentication_string=password('hrj') where user='root' and Host = 'localhost'; -- 修改用户密码
flush privileges; -- 刷新权限

mysql version(); -- 查看mysql版本号：
----------------------------------
-- 所有的语句都用;结尾
show databases; -- 查看所有的数据库

mysql> use school -- 切换数据库 use 数据库名
Database changed  -- 有这行代码提示：表示切换成功

select database();  -- 查看当前使用的是哪个数据库

show tables; -- 查看数据库中所有的表 
describe student; -- 显示数据库中所有的表的信息

create database if not exists goods; -- 创建一个名为goods的数据库
drop database if exists goods; -- 删除数据库指令

exit; -- 退出链接
```

## 2、创建数据库表

2-1、创建数据库表：

```sql
-- 目标：创建一个school数据库
-- 创建学生表(列，字段) 使用SQL创建
-- 学号int、登录密码varchar(20) 姓名、性别varchar(2)、出生日期(datatime)、家庭住址 email 
CREATE TABLE `student` (
  `id` int(4) NOT NULL AUTO_INCREMENT COMMENT '学号',
  `name` varchar(30) NOT NULL DEFAULT '匿名' COMMENT '姓名',
  `password` varchar(20) NOT NULL DEFAULT '123' COMMENT '密码',
  `gender` varchar(2) NOT NULL DEFAULT '女' COMMENT '性别',
  `birthday` datetime DEFAULT NULL COMMENT '出生日期',
  `address` varchar(100) DEFAULT NULL COMMENT '家庭住址',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
```

2-2、常用命令：

```sql
SHOW CREATE DATABASE school -- 查看创建数据库的语句
SHOW CREATE TABLE student -- 查看student数据表的定义语句
DESC student -- 显示表的结构

mysql> select database(); -- 查看当前使用的是哪个数据库
mysql> select version(); -- 查看mysql的版本号。
\c   -- 命令，结束一条语句。
```

2-3、修改&删除表：

```sql
-- 修改表名 ALTER TABLE 旧表面 AS 新表名
ALTER TABLE student RENAME  AS student1
-- 增加表的字段 ALTER TABLE 表名 ADD 字段名 列属性
ALTER TABLE student1 ADD age INT(11)
-- 修改表的字段（重命名，修改约束）
ALTER TABLE student1 MODIFY age VARCHAR(11)  -- 修改约束
-- ALTER TABLE 表名 CHANGE 旧名字 新名字 列属性[]
ALTER TABLE student1 CHANGE age age1 INT(1)  -- 字段重命名

-- 删除表的字段
ALTER TABLE student1 DROP age1

-- 删除表
DROP TABLE IF EXISTS student1
```

## 3、备份数据库

3-1、备份数据库(注意在DOS下执行)

```sql
-- 练习：database01.sql 备份 student1 和 student2 库中的数据，并恢复
-- 备份，要在 DOS 下执行 mysqldump 指令
-- mysqldump -u 用户名 -p密码 -B 数据库1 数据库2 数据库n > d文件名.sql
mysqldump -u root -phrj -B student1 student2 > d:\\database01.sql
-- 恢复数据库(注意：进入 Mysql 命令行再执行)先将 student1 和 student2 删除再恢复
-- source 文件名.sql
source d:\\database01.sql
```

3-2、备份恢复数据库的表

```sql
-- 备份库的表
-- mysqldump -u 用户名 -p密码 数据库 表1 表2 表n > d:\\文件名.sql

-- 初始化数据
mysql> source D:\course\05-MySQL\resources\bjpowernode.sql
```

```sql
-- 将数据库当中的数据导出,在windows的dos命令窗口中执行：（导出整个库）
mysqldump bjpowernode>D:\bjpowernode.sql -uroot -p333
-- 在windows的dos命令窗口中执行：（导出指定数据库当中的指定表）
mysqldump bjpowernode emp>D:\bjpowernode.sql -uroot –p123
-- 导入数据
create database bjpowernode;
use bjpowernode;
source D:\bjpowernode.sql
```

##  4、DQL语言

>>:pushpin: 做加法&去重
>
>- **distinct只能出现在所有字段的最前面。**
>
>```sql
>1、 SELECT 数值+数值; 直接运算
>2、 SELECT 字符+数值; 首先先将字符转换为整数，如果转换成功，则继续运算，如果转换失败，则默认为0，然后继续运算
>3、 SELECT NULL+数值; NULL和任何数值参与运算结果都是NULL
>
>SELECT DISTINCT 字段名 FROM 表名;
>```
>
>>:pushpin: ifnull函数：判断某字段或表达式是否为null，如果为null，返回指定的值，否则返回原本的值
>
>- SELECT IFNULL(字段名, 指定值) FROM 表名;
>
>> :pushpin:isnull函数：判断某字段或表达式是否为null，如果是null，则返回1，否则返回0
>
>- SELECT ISNULL(字段名) FROM 表名;
>
>>:pushpin:模糊运算符
>
>- like：%任意多个字符、_任意单个字符，如果有特殊字符，需要使用escape转义
>- between and
>- not between and
>- in
>- is null
>- is not null
>
>>:pushpin:排序查询
>
>1. 排序列表可以是单个字段、多个字段、别名、函数、表达式
>2. order by的位置一般放在查询语句的最后（除limit语句之外）
>3. **select-5、from-1、where-2、group by-3、having-4、order by-6、limit-7**
>
>```sql
>-- 按别名排序查询：查询员工信息，要求按员工年薪升序
>SELECT *,salary * 12 * (1+ IFNULL(commission_pct, 0)) 年薪 
>FROM employees 
>ORDER BY 年薪 ASC ;
>```
>
>>:pushpin:多行处理函数(分组函数)：输入多行，最终输出的结果是1行【分组函数自动忽略NULL】
>
>- count(comm): 表示统计comm字段中不为``NULL``的数据总数量。、sum 、avg 、max 、min
>- 任何一个分组函数（count sum avg max min）都是在``group by``语句执行结束之后才会执行的。当一条sql语句没有group by的话，整张表的数据会自成一组。
>
>```sql
>select ename,sal from emp where sal > avg(sal); -- ERROR 1111 (HY000): Invalid use of group function
>-- 原因：SQL语句当中有一个语法规则，**分组函数不可直接使用在where子句当中**。why????
>-- 因为：group by是在where执行之后才会执行的。
>select ename,sal from emp where sal > (select avg(sal) from emp);
>```
>
>>:x:**select ename,max(sal),job from emp group by job;**
>>
>>- 以上在mysql当中，查询结果是有的，但是结果没有意义，在Oracle数据库当中会报错。语法错误。Oracle的语法规则比MySQL语法规则严谨。
>>- 记住一个规则：**当一条语句中有group by的话，select后面只能跟分组函数和参与分组的字段**。
>
>>:pushpin:*建议能够使用where过滤的尽量使用where。*
>
>- select max(sal),deptno from emp group by deptno having max(sal) > 2900; *// 这种方式效率低。*
>- select max(sal),deptno from emp where sal > 2900 group by deptno; 效率高
>- **where后面不能使用分组函数**：
>  - :x:select deptno,avg(sal) from emp where avg(sal) > 2000 group by deptno;	*// 错误了。*这种情况只能使用having过滤。
>    - select deptno,avg(sal) from emp group by deptno having avg(sal) > 2000;	

##  5、连接查询

>>📌连接查询的分类：
>
>- 内连接：等值连接、非等值连接、自连接
>  - 假设A和B表进行连接，使用内连接的话，凡是A表和B表能够匹配上的记录都能查询出来，这就是内连接。 AB两张表没有主副之分，两张表是平等的。
>- 外连接：左外连接（左连接：表示左边的这张表是主表。）、右外连接（右连接）
>  - 主要查询主表中 	的数据，捎带着查询副表，当副表中的数据没有和主表中的数据匹配上，副表自动模拟出NULL与之匹配。
>
>>:pushpin:等值连接：
>
>```sql
>-- 案例：查询每个员工的部门名称，要求显示员工名和部门名。
>SQL99：（常用的）
>select e.ename,d.dname
>from emp e
>join dept d
>on e.deptno = d.deptno;
>```
>
>>:pushpin:非等值连接
>
>```sql
>-- 案例：找出每个员工的工资等级，要求显示员工名、工资、工资等级。
>select e.ename,e.sal,s.grade
>from emp e
>join salgrade s
>on e.sal between s.lowsal and s.highsal;
>```
>
>>:pushpin:自连接：一张表看做两张表。自己连接自己。
>
>```sql
>-- 案例：找出每个员工的上级领导，要求显示员工名和对应的领导名。
>-- (员工的领导编号 = 领导的员工编号)
>select a.ename as '员工名',b.ename as '领导名'
>from emp a
>inner join emp b
>on a.supno = b.empno;
>```
>
>>:pushpin:外连接：
>
>```sql
>-- 案例：找出每个员工的上级领导？（所有员工必须全部查询出来。）
>-- 使用左
>select a.ename '员工', b.ename '领导'
>from emp a
>left join emp b
>on a.mgr = b.empno;
>```
>
>>:pushpin:三张表连接查询： 
>
>```sql
>-- 案例：找出每一个员工的部门名称以及工资等级。
>select e.ename,d.dname,s.grade
>from emp e
>join dept d
>on e.deptno = d.deptno
>join salgrade s
>on e.sal between s.losal and s.hisal;
>
>-- 案例：找出每一个员工的部门名称、工资等级、以及上级领导。
>select e.ename '员工',d.dname,s.grade,e1.ename '领导'
>from emp e
>join dept d
>on e.deptno = d.deptno
>join salgrade s
>on e.sal between s.losal and s.hisal
>left join emp e1
>on e.mgr = e1.empno;
>```

##  6、子查询

>>:pushpin:from后面嵌套子查询
>
>```sql
>-- 案例：找出每个部门平均薪水的等级。
>select 
>	t.*,s.grade
>from
>	(select deptno,avg(sal) as avgsal from emp group by deptno) t
>join
>	salgrade s
>on
>	t.avgsal between s.losal and s.hisal;
>	
>-- 案例：找出每个部门平均的薪水等级。
>```

##  7、limit的使用

>>:pushpin:limit startIndex, length
>
>- startIndex表示起始位置，从0开始，0表示第一条数据。length表示取几个
>
>```sql
>-- 案例：找出工资排名在第4到第9名的员工？
>select ename,sal from emp order by sal desc limit 3,6;
>```
>
>>:pushpin:每页显示pageSize条记录：
>
>-  第pageNo页：(pageNo - 1) * pageSize, pageSize

##  8、创建表

>>:pushpin:关于MySQL当中字段的数据类型？以下只说常见的
>
>- `int`	整数型(java中的int)
>- `bigint`	长整型(java中的**long**)
>- `float`	浮点型(java中的float double)	
>- `char`	定长字符串(String)
>- `varchar`可变长字符串(StringBuffer/StringBuilder)	
>- `date`	日期类型 （对应Java中的java.sql.Date类型）
>- `BLOB	`	二进制大对象（存储图片、视频等流媒体信息） Binary Large OBject （对应java中的Object）	
>- `CLOB`	字符大对象（存储较大文本，比如，可以存储4G的字符串。） Character Large Object（对应java中的Object）
>
>>:pushpin:约束（Constraint）:
>
>- 检查约束(check)：注意Oracle数据库有check约束，但是mysql没有，目前mysql不支持该约束。
>- 唯一约束(unique)修饰的字段具有唯一性，不能重复。但可以为NULL。
>- Oracle当中也提供了一个自增机制，叫做：序列（sequence）对象。
>- 外键约束：
> - **foreign key(classno) references t_class(cno)**
> - 外键字段引用其他表的某个字段的时候，被引用的字段不一定是主键，但至少具有unique约束。

```sql
CREATE TABLE employee(
	id INT PRIMARY KEY AUTO_INCREMENT,
	`name` VARCHAR(32) DEFAULT NULL
)ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE department(
	id INT(11) PRIMARY KEY AUTO_INCREMENT,
	dept_name VARCHAR(255)	
);
ALTER TABLE employee ADD COLUMN d_id INT(11);
ALTER TABLE employee ADD CONSTRAINT fk 
FOREIGN KEY(d_id) REFERENCES department(id);
update employee set `d_id` = 1 where `id` = 1;
```

##  9、表的操作

>>:pushpin:表的操作：
>
>- 表名在数据库当中一般建议以：`t_` 或者 `tbl_` 开始。
>  - 注意：在MySQL当中，凡是标识符是可以使用飘号括起来的。最好别用，不通用。
>- 表的复制：**create table 表名 as select语句;**  将查询结果当做表创建出来。
>  - create table emp_bak as select * from emp;
>- 将查询结果插入一张表中：**insert into dept1 select * from dept;**
>- 更新数据：update 表名 set 字段名1=值1,字段名2=值2... where 条件;
>- 删除数据：delete from 表名 where 条件;
>- 怎么删除大表中的数据？（重点）
>  - **truncate table 表名;** *// 表被截断，不可回滚。永久丢失。*
>- 删除表：
>  - **drop table 表名;**  // 这个通用。
>- 出现在java代码当中的sql包括：insert delete update select（这些都是表中的数据操作。）
>- CRUD操作：Create（增） Retrieve（检索） Update（修改） Delete（删除）

##  10、存储引擎

>>:pushpin:MyISAM
>
>- 缺点：不支持事务。
>- 优点：可被压缩，节省存储空间。并且可以转换为只读表，提高检索效率。
>  - MyISAM采用三个文件组织一张表：①xxx.frm（存储格式的文件）、②xxx.MYD（存储表中数据的文件）、③xxx.MYI（存储表中索引的文件）
>
>>:pushpin:InnoDB
>
>- 优点：支持事务、行级锁、外键等。这种存储引擎数据的安全得到保障。
>  - 表的结构存储在xxx.frm文件中
>  - 数据存储在tablespace这样的表空间中（逻辑概念），无法被压缩，无法转换成只读。
>  - 这种InnoDB存储引擎在MySQL数据库崩溃之后提供自动恢复机制。InnoDB支持级联删除和级联更新。
>
>>:pushpin:MEMORY
>
>- 缺点：不支持事务。数据容易丢失。因为所有数据和索引都是存储在内存当中的。
>- 优点：查询速度最快。以前叫做HEPA引擎。

##  11、事务



##  12、索引

>>:pushpin:创建，删除索引对象:
>
>- create index 索引名称 on 表名(字段名);
>  - create index emp_sal_index on emp(sal);
>- drop index 索引名称 on 表名;
>- 主键和具有unique约束的字段自动会添加索引。

##  13、视图

>>:pushpin:创建，删除视图:
>
>- **create view myview as select empno,ename from emp;**
>- drop view myview;
>
>```sql
>create view myview1 as select empno,ename,sal from emp_bak;
>update myview1 set ename='hehe',sal=1 where empno = 7369; -- 通过视图修改原表数据。
>delete from myview1 where empno = 7369; -- 通过视图删除原表数据。
>```
>
>
