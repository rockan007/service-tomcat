<%--
  Created by IntelliJ IDEA.
  User: rocka
  Date: 2017/11/23
  Time: 11:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>context-parameters</title>
</head>
<body>
SettingOne:<%= application.getInitParameter("settingOne") %><br/>
SettingTwo:<%= application.getInitParameter("settingTwo")%>
</body>
</html>
