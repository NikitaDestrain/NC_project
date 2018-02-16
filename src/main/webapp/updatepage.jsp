<%@ page import="testservlet.beans.User" %><%--
  Created by IntelliJ IDEA.
  User: ывв
  Date: 11.02.2018
  Time: 12:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update user</title>
</head>
<style type="text/css">
    table {
        width: 100%;
        height: auto;
        padding: 10px;
        border: 1px solid gray;
        border-collapse: collapse;
        background-color: khaki;
    }

    caption {
        margin-bottom: 10px;
        font-weight: bold;
        font-family: "Yu Gothic Light";
    }

    td {
        text-align: center;
    }
</style>
<body>
<%
    User user = (User)request.getAttribute("user");
    String email = user.getEmail().replaceAll(" ", "");
    String password = user.getPassword().replaceAll(" ", "");
    String id = (user.getId() + "").replaceAll(" ", "");
    String message = user.getMessage();
%>
<form>
    <table>
        <caption>
            User with id: <%=id%>
        </caption>
        <tr>
            <td><input type="text" name="email" value="<%=email%>"></td>
        </tr>
        <tr>
            <td><input type="text" name="password" value="<%=password%>"></td>
        </tr>
        <tr>
            <td>
                <input type="submit" value="Update">
                <input type="hidden" name="action" value="doupdate">
                <input type="hidden" name="id" value="<%=id%>">
            </td>
        </tr>
        <tr>
            <td>
                <%
                    if (message != null && !message.equals("")) {
                %>
                <%=message%>
                <%
                    }
                %>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
