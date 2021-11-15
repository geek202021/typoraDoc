#  Typora的使用

[Typora的使用-GitHub](https://github.com/younghz/Markdown)

[Typora的使用-CSDN](https://blog.csdn.net/weixin_39751195/article/details/109971095)

[中文文案排版指南](https://github.com/sparanoid/chinese-copywriting-guidelines/blob/master/README.zh-CN.md)

[程序员鱼皮](https://www.code-nav.cn/resources)     [Java学习路线by-yupi](https://doc.code-nav.cn/roadmap/java)

[Markdown表情包](https://www.webfx.com/tools/emoji-cheat-sheet/)

##  文本编辑快捷键

- home键：光标回到行首；end：光标回到行尾
- ctrl + home键：光标回到文章开始；ctrl + end键：光标回到文章末尾
- **shift + home 或 end 键：选中一行**
- 鼠标双击：选中一个单词 / 鼠标连续击3次：选中一行
- **ctrl+ shift + 右箭头或左箭头：选中一个单词**
- Ctrl + F 搜索 ；Ctrl + H 替换

#   正则表达式

##  1、元字符说明

1. `*`：0次或n次匹配前面的字符或子表达式。例如，zo* 匹配"z"和"zoo"。*** 等效于 {0,}**。
2. `+`：1次或n次匹配前面的字符或子表达式。例如，"zo+"与"zo"和"zoo"匹配，但与"z"不匹配。+ 等效于 **{1,}**。
3. `?`：0次或1次匹配前面的字符或子表达式。例如，"do(es)?"匹配"do"或"does"中的"do"。? 等效于 **{0,1}**。
   1. 当此字符紧随任何其他限定符（*、+、?、{n}、{n,}、{n,m}）之后时，匹配模式是**"非贪心的"**。"非贪心的"模式匹配搜索到的、尽可能短的字符串，而默认的"贪心的"模式匹配搜索到的、尽可能长的字符串。例如，**在字符串"oooo"中，"o+?"只匹配单个"o"，而"o+"匹配所有"o"。**
4. `\d`：数字字符匹配。等效于 [0-9]。  `\D`：非数字字符匹配。等效于 [\^0-9]。
5. `\w`：**匹配任何字类字符，包括下划线。与"[A-Za-z0-9_]"等效。**

