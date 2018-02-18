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
    <style type="text/css">
        table {
            width: auto;
            height: auto;
            /*text-align: center;*/
            /*vertical-align: center;*/
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
</head>
<body>
<%
//    User user = (User)request.getAttribute("user");
    String email =  null;//user.getEmail().replaceAll(" ", "");
    String password =  null;//user.getPassword().replaceAll(" ", "");
    String id =  null;//(user.getId() + "").replaceAll(" ", "");
    String message = null;//user.getMessage();
%>
<div align="center">
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
</div>
</body>
</html>
