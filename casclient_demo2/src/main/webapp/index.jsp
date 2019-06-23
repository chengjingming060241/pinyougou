<%--
  Created by IntelliJ IDEA.
  User: chengjingming
  Date: 2019/3/31
  Time: 3:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>cas client demo1</title>
</head>
<body>
    <p>欢迎来到品优购 <%=request.getRemoteUser()%></p>

    <a href="http://localhost:9100/cas/logout?service=http://www.baidu.com">退出</a>
</body>
</html>
