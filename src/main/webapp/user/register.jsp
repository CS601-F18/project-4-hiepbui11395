<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register</title>
</head>
<body>
<h1>Register</h1>
<form action="/register" method="post">
    Username: <input type="text" name="username"/><br/>
    <c:if test="${messages.containsKey('username')}">
        <c:out value="${messages['username']}"/><br/>
    </c:if>
    Password: <input type="password" name="password"/><br/>
    Re-type Password: <input type="password" name="reTypePassword"/><br/>
    Email: <input type="text" name="email"/><br/>
    <c:if test="${messages.containsKey('email')}">
        <c:out value="${messages['email']}"/><br/>
    </c:if>
    Phone Number: <input type="text" name="phoneNumber"/><br/>

    <button type="submit">Login</button>
</form>
</body>
</html>