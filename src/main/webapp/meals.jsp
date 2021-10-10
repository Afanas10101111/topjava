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
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach var="meal" items="${mealsTo}">
        <c:set var="mealColor" value="${meal.excess ? 'red' : 'green'}"/>
        <tr style="color:${mealColor}">
            <th><t:format value="${meal.dateTime}" pattern="yyyy-MM-dd hh:mm"/></th>
            <th>${meal.description}</th>
            <th>${meal.calories}</th>
        </tr>
    </c:forEach>
</table>
</body>
</html>