<%@ page import="server.model.Task" %><%--
  Created by IntelliJ IDEA.
  User: ывв
  Date: 18.02.2018
  Time: 18:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Notification page</title>
    <script language="Javascript" type="text/javascript" src="calendar&time/jquery.1.4.2.js"></script>
    <link type="text/css" href="calendar&time/latest.css" rel="Stylesheet"/>
    <script type="text/javascript" src="calendar&time/latest.js"></script>
    <style type="text/css">
        body {
            margin-bottom: 10px;
            font-weight: bold;
            font-family: "Yu Gothic Light"
        }

        div {
            width: auto;
            height: auto;
            background-color: khaki;
        }
    </style>
</head>
<body>
<%
    Task task = (Task) request.getAttribute("task");
%>
<div align="center">
    Notification!<br/>
    <%=task==null?"":task.getName()%><br/>
    <%=task==null?"":task.getDescription()%><br/>
</div>
<div align="center">
    <label>
        Change status
        <select name="statuschanged">
            <option value="Cancel">Cancel</option>
            <option value="Finish">Finish</option>
        </select>
    </label>
</div>
<div align="center">
    <label>
        Defer task on
        <input type="text" name="defertask" class="datepickerTimeField" value="">
    </label>
</div>
<div align="center">
    <input type="submit" name="OK" value="OK">
</div>
</body>
</html>
