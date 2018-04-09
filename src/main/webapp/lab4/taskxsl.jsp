<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ page import="auxiliaryclasses.ConstantsClass" %><%--
  Created by IntelliJ IDEA.
  User: ывв
  Date: 04.04.2018
  Time: 16:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Task XSL</title>
    <style type="text/css">
        <%@include file="../bootstrap/css/bootstrap.min.css"%>
    </style>
</head>
<body>
<div align="center"><strong>TASKS</strong></div>
<form method="post" action="<%=ConstantsClass.IMPORT_SERVLET_ADDRESS%>" role="form">
    <input type="hidden" name="<%=ConstantsClass.ACTION%>" value=<%=ConstantsClass.CHOOSE_STRATEGY%>>
    <div class="form-group">
        <table class="table">
            <tr>
                <th>ID</th>
                <th>Status</th>
                <th>Name</th>
                <th>Description</th>
                <th>Planned date</th>
                <th>Notification date</th>
                <th>Upload date</th>
                <th>Change date</th>
            </tr>
            <x:transform xml="${sessionScope.journal}" xslt="${requestScope.xslJournal}"/>
        </table>
        <div align="center">
            Choose strategy: <br/>
            <table>
                <tr>
                    <td>
                        <select name="<%=ConstantsClass.USERNUMBER%>">
                            <option value="<%=ConstantsClass.REPLACE%>">
                                Replace
                            </option>
                            <option value="<%=ConstantsClass.IGNORE%>">
                                Ignore
                            </option>
                            <option value="<%=ConstantsClass.EXCEPTION%>">
                                Throw an exception
                            </option>
                        </select>
                    </td>
                </tr>
            </table>
        </div>
        <div align="center">
            <input type="submit" id="submit" class="btn btn-outline-primary" value="OK" onclick="buttonClick(this)"/>
        </div>
    </div>
</form>
</body>
</html>
