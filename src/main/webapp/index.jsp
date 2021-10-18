<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Java Enterprise (Topjava)</title>
</head>
<body>
<h3>Проект <a href="https://github.com/JavaWebinar/topjava" target="_blank">Java Enterprise (Topjava)</a></h3>
<form name="SelectUser" method="post" action="users">
    <p>Select user: </p>
    <table>
        <tr>
            <th><button type="submit" name="userId" value="1">User</button></th>
            <th><button type="submit" name="userId" value="2">Admin</button></th>
        </tr>
    </table>
</form>
<hr>
<p>Current user: ${user eq null ? 'Unknown' : user}</p>
<ul style="font-size: large">
    <li><a href="users">Users</a></li>
    <li><a href="meals">Meals</a></li>
</ul>
</body>
</html>
