<%--
  Created by IntelliJ IDEA.
  User: phuhi
  Date: 11/20/2018
  Time: 10:09 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Event</title>
    <%--<%@ include file="../_header.jsp"%>--%>
    <c:import url="../_header.jsp"/>
</head>
<body>
<c:import url="../_menu.jsp"/>
<div class="row">
    <div class="col-2"></div>
    
    <div class="col-4 game-container">
        <div class="page-title"><h1>${event.name}</h1></div>
        <ul>
            <li>Description: ${event.description}</li>
            <li>Location: ${event.location}</li>
            <li>Date: ${event.date}</li>
        </ul>
    </div>
    <div class="col-4 following-container">
        <h2>Tickets</h2>
    </div>

    <div class="col-2"></div>
</div>

</body>
</html>
