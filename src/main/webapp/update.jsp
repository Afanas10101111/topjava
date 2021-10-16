<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Edit/Add Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit/Add Meal</h2>
<form method="post" action="extended-meals">
    <p>DateTime: <input type="datetime-local" name="dateTime" value="${meal.dateTime}" required/></p>
    <p>Description: <input type="text" name="description" value="${meal.description}" required/></p>
    <p>Calories: <input type="number" name="calories" value="${meal.calories}" required/></p>
    <p>
        <button type="submit">Save</button>
        <button type="reset">Reset</button>
        <button type="button" onclick="window.location.href = 'extended-meals';">Cancel</button>
    </p>
    <input type="hidden" name="id" value="${meal.id}"/>
</form>
</body>
</html>