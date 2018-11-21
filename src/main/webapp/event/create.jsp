<%--
  Created by IntelliJ IDEA.
  User: phuhi
  Date: 11/19/2018
  Time: 11:17 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Create Event</title>

</head>
<body>
<h1>Create Event</h1>
<form action="/events/create" method="post">
    Name: <input type="text" name="name"/><br/>
    <c:if test="${messages.containsKey('name')}">
        <c:out value="${messages['name']}"/><br/>
    </c:if>
    Description: <input type="text" name="description"/><br/>
    <c:if test="${messages.containsKey('description')}">
        <c:out value="${messages['description']}"/><br/>
    </c:if>
    Location: <input type="text" name="location"/><br/>
    <c:if test="${messages.containsKey('location')}">
        <c:out value="${messages['location']}"/><br/>
    </c:if>
    Date: <input type="text" name="date"/><br/>
    <c:if test="${messages.containsKey('date')}">
        <c:out value="${messages['date']}"/><br/>
    </c:if>
    <button type="submit">Register</button>
</form>
</body>
</html>
