<%--
  Created by IntelliJ IDEA.
  User: ывв
  Date: 07.02.2018
  Time: 11:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Successful log in</title>
<style  type="text/css">


    #message {
        position: relative;
        top: 100px;
        width: 300px;
        border: 1px solid gray;
        padding: 20px;
        background-color: #CCFFCC;
        text-align: center;
        font-weight: bold;
    }


</style>
</head>
<body>

<center>

    <div id="message">

        <p>You are logged in.</p>

        <p><%= request.getAttribute("email") %></p>

    </div>


</center>
</body>
</html>
