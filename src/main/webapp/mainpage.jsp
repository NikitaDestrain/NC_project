<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="com.sun.xml.internal.ws.server.ServerRtException" %>
<%@ page import="testservlet.beans.SelectResultBean" %>
<%@ page import="testservlet.beans.User" %><%--
  Created by IntelliJ IDEA.
  User: ывв
  Date: 10.02.2018
  Time: 17:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Select results</title>
</head>
<style type="text/css">
    table {
        width: 100%;
        height: auto;
        padding: 10px;
        border-collapse: collapse;
        background-color: khaki;
    }

    caption {
        margin-bottom: 10px;
        font-weight: bold;
        font-family: "Yu Gothic Light";
    }

    th {
        padding: 5px;
        border: 1px solid black;
        text-align: center;
    }

    td {
        border: 1px solid black;
        padding: 5px;
        text-align: left;
        padding-right: 20px;
    }

    .count {
        background-color: darkkhaki;
        font-weight: bold;
        text-align: center;
    }

    input[type="submit"] {
        display: block;
        align-self: center;
    }

    .label {
        margin-bottom: 10px;
        font-weight: bold;
        font-family: "Yu Gothic Light"
    }

    div {
        align: center;
    }

    .center {
        text-align: center;
    }

    .namelabel {
        text-align: right;
    }

    .left {
        text-align: left;
    }
</style>
<body>
<%
    String message = null;
    SelectResultBean bean = (SelectResultBean) request.getAttribute("bean");
    LinkedList<User> users = bean.getUsers();
    int count = 0;
%>
<div class="center"><strong>TASK SCHEDULER</strong></div>
<div class="namelabel"><strong>You logged as: <%=request.getAttribute("username")==null?"":request.getAttribute("username") %></strong></div>
<form action="/testoracle" method="get">
    <table>
        <caption>Result of "select" request</caption>
        <tr>
            <th class="count">№</th>
            <th>Status</th>
            <th>Name</th>
            <th>Description</th>
            <th>Planned date</th>
            <th>Planned time</th>
            <th>Notification date</th>
            <th>Notification time</th>
        </tr>
        <%
            int id;
            String email;
            String password;
            for (User u : users) {
                id = u.getId();
                email = u.getEmail();
                password = u.getPassword();
        %>
        <tr>
            <td class="count">
                <label>
                    <%=count%>
                    <input type="radio" name="usernumber" value="<%=count++%>"/>
                </label>
            </td>
            <td><%=id%>
            </td>
            <td><%=email%>
            </td>
            <td><%=password%>
            </td>
        </tr>
        <%
            }
        %>
    </table>
    <div class="left">
        <label class="label">
            Journal #
            <select name="journalnum" id="journalnum">
                <option value="1">1</option>
            </select>
        </label>
    </div>
    <div class="left">
        <p class="label"><%=request.getAttribute("message") != null ? request.getAttribute("message") : ""%>
        </p> <br/>
        <label class="label">
            Choose action to perform <br/>
            <select name="useraction" id="useraction">
                <option value="Add">
                    Add user
                </option>
                <option value="Update">
                    Update
                </option>
                <option value="Delete">
                    Delete
                </option>
            </select>
        </label>
    </div>
    <input type="submit" value="Do">
    <input type="hidden" name="action" value="toupdatepage">
</form>
</body>
</html>