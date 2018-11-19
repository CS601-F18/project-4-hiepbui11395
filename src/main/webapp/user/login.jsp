<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>Login</h1>
    <form action="/login" method="post">
        Username <input type="text" name="username"/><br/>
        <c:if test="${messages.containsKey('username')}">
            <c:out value="${messages['username']}"/><br/>
        </c:if>
        Password <input type="text" name="password"/><br/>
        <c:if test="${messages.containsKey('password')}">
            <c:out value="${messages['password']}"/><br/>
        </c:if>

        <c:if test="${messages.containsKey('login')}">
            <c:out value="${messages['login']}"/><br/>
        </c:if>
        <button type="submit">Login</button>
    </form>
</body>
</html>