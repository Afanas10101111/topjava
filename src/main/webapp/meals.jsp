<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://sargue.net/jsptags/time" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <style>
        table {
            border: 1px solid grey;
        }

        th {
            border: 1px solid grey;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<h4><a href="meals?action=add">Add Meal</a></h4>
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach var="meal" items="${mealsTo}">
        <tr style="color:${meal.excess ? 'red' : 'green'}">
            <th><t:format value="${meal.dateTime}" pattern="yyyy-MM-dd HH:mm"/></th>
            <th>${meal.description}</th>
            <th>${meal.calories}</th>
            <th><a href="meals?action=update&id=${meal.id}">Update</a></th>
            <th><a href="meals?action=delete&id=${meal.id}">Delete</a></th>
        </tr>
    </c:forEach>
</table>
</body>
</html>