<%--
  Created by IntelliJ IDEA.
  User: rocka
  Date: 2017/11/23
  Time: 11:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>checkboxes</title>
</head>
<body>
<form action="checkboxesSubmit.jsp" method="POST">
    Select the fruits you like to eat:<br/>
    <input type="checkbox" name="fruit" value="Banana"/>Banana<br/>
    <input type="checkbox" name="fruit" value="Apple"/>Apple<br/>
    <input type="checkbox" name="fruit" value="Orange"/>Orange<br/>
    <input type="checkbox" name="fruit" value="Guava"/>Guava<br/>
    <input type="checkbox" name="fruit" value="Kiwi"/>Kiwi<br/>
    <input type="submit" value="Submit"/>
</form>
</body>
</html>
