<html>
    <head>
        <title>demo</title>
        <meta charset="UTF-8">
    </head>
    <body>
    <#--引用模板，嵌套-->
    <#include "head.ftl">
    <#--我是一个注释，我不会输出-->

    ${name},你好。${message}

    <#--定义变量-->
    <#assign linkman ="周先生">
    ${linkman}

    <#--if判断-->
    <#if success = true>
    你已通过实名认证
    <#else>
    你未通过实名认证
    </#if>
    <br>

    <h4>---商品列表---</h4>
    <#list goodsList as goods>
     ${goods_index+1} 商品名称：${goods.name} 商品价格：${goods.price} <br>
    </#list>


    <#--內建函数-->
    一共${goodsList?size}条记录 <br>
    <#assign text="{'attributeName':'屏幕尺寸','attributeValue':'6寸'}">
    <#assign data=text?eval>
    屏幕：${data.attributeName} 尺寸：${data.attributeValue} <br>

    当前日期：${today?date} <br>
    当前时间：${today?time} <br>
    当前日期+时间：${today?datetime} <br>

    日期格式化：${today?string("yyyy年MM月")} <br>

    <#--数字超过3位会自动加逗号，用函数 c 会自动转为字符串-->
    当前积分：${point?c} <br>

    <#--空值处理运算符 用if 或者 ！-->
    <#if aaa??>
      aaa存在
      <#else>
      aaa不存在
      </#if>
      <br>
      ${bbb!'没有'}




    </body>

</html>

