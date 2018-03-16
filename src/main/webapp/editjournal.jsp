<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ page import="auxiliaryclasses.ConstantsClass" %><%--
  Created by IntelliJ IDEA.
  User: ывв
  Date: 26.02.2018
  Time: 12:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit journal</title>
    <style>
        <%@include file="css/cu-journal.css"%>
    </style>
    <script type="text/javascript">
        function buttonClick(x) {
            switch (x.id) {
                case "save":
                    document.getElementById("hid").value = "Save";
                    break;
                case "back":
                    document.getElementById("hid").value = "backtomain";
                    break;
            }
            document.forms[0].submit();
        }
    </script>
</head>
<body>
<div align="center">
    <form method="post" action="<%=ConstantsClass.JOURNAL_SERVLET_ADDRESS%>">

        <input type="hidden" name="<%=ConstantsClass.ACTION%>" value="<%=ConstantsClass.DO_EDIT_JOURNAL%>">
        <input type="hidden" id="hid" name=<%=ConstantsClass.USERACTION%>>

        <table>
            <caption>Edit journal</caption>
            <x:parse xml="${requestScope.journal}" var="journal"/>
            <tr>
                <td class="align-right">Name</td>
                <td class="align-right">
                    <input type="text" name="<%=ConstantsClass.NAME%>"
                           value="<x:out select="$journal/journal/name"/>">
                </td>
            </tr>
            <tr>
                <td class="align-right">Description</td>
                <td class="align-right">
                    <input type="text" name="<%=ConstantsClass.DESCRIPTION%>"
                           value="<x:out select="$journal/journal/description"/>">
                </td>
            </tr>
            <tr>
                <td colspan="2"><input type="button" id="save" value="Save" onclick="buttonClick(this)"></td>
            </tr>
        </table>
        <div class="center">
            <input type="button" id="back" value="Back to main page" onclick="buttonClick(this)">
        </div>
    </form>
</div>
<div align="center">
    <%=
    request.getAttribute(ConstantsClass.MESSAGE_ATTRIBUTE) == null ? "" : request.getAttribute(ConstantsClass.MESSAGE_ATTRIBUTE)
    %>
</div>
</body>
</html>
