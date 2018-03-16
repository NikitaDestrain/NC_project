<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ page import="auxiliaryclasses.ConstantsClass" %><%--
  Created by IntelliJ IDEA.
  User: ывв
  Date: 18.02.2018
  Time: 17:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit task</title>
    <style type="text/css" media="screen">
        <%@include file="css/cu-task.css"%>
    </style>
    <script type="text/javascript">
        function buttonClick(x) {
            switch (x.id) {
                case "save":
                    document.getElementById("hid").value = "Save";
                    break;
                case "backtomain":
                    document.getElementById("hid").value = "backtomain";
                    break;
                case "backtotasks":
                    document.getElementById("hid").value = "backtotasks";
                    break;
            }
            document.forms[0].submit();
        }
    </script>
</head>
<body>
<div align="center">
    <form method="post" action=<%=ConstantsClass.TASK_SERVLET_ADDRESS%>>

        <input type="hidden" name="<%=ConstantsClass.ACTION%>" value="<%=ConstantsClass.DO_EDIT_TASK%>">
        <input type="hidden" id="hid" name=<%=ConstantsClass.USERACTION%>>

        <table>
            <caption>Edit task</caption>
            <x:parse xml="${requestScope.task}" var="task"/>
            <tr>
                <td class="align-center" colspan="2">
                    Journal name: <%=request.getAttribute(ConstantsClass.CURRENT_JOURNAL_NAME)==null?"":request.getAttribute(ConstantsClass.CURRENT_JOURNAL_NAME)%></td>
            </tr>
            <tr>
                <td class="align-status" colspan="2">
                    Task status: <x:out select="$task/task/status"/>
                </td>
            </tr>
            <tr>
                <td class="align-center" colspan="2">
                    Upload date: <x:out select="$task/task/upload"/>
                </td>
            </tr>
            <tr>
                <td class="align-center" colspan="2">
                    Change date: <x:out select="$task/task/change"/>
                </td>
            </tr>
            <tr>
                <td class="align-right">Name</td>
                <td class="align-right">
                    <input type="text" name="<%=ConstantsClass.NAME%>"
                           value="<x:out select="$task/task/name"/>">
                </td>
            </tr>
            <tr>
                <td class="align-right">Description</td>
                <td class="align-right">
                    <input type="text" name="<%=ConstantsClass.DESCRIPTION%>"
                           value="<x:out select="$task/task/description"/>">
                </td>
            </tr>
            <tr>
                <td class="align-right">Planned date</td>
                <td class="align-right">
                    <input type="text" name="<%=ConstantsClass.PLANNED_DATE%>" id="planned"
                           value="<x:out select="$task/task/planned"/>">
                </td>
            </tr>
            <tr>
                <td class="align-right">Notification date</td>
                <td class="align-right">
                    <input type="text" name="<%=ConstantsClass.NOTIFICATION_DATE%>" id="notification"
                           value="<x:out select="$task/task/notification"/>">
                </td>
            </tr>
            <tr>
                <td class="align-right">Change journal</td>
                <td class="align-left">
                    <select name=<%=ConstantsClass.JOURNAL_NAME%>>
                        <option value=""></option>
                        <x:parse xml="${sessionScope.journalNames}" var="names"/>
                        <x:forEach select="$names/journalNames/name" var="name">
                            <option value="<x:out select="$name"/>"><x:out select="$name"/></option>
                        </x:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="align-right">Change task status</td>
                <td class="align-left">
                    <select name="<%=ConstantsClass.STATUS%>">
                        <option value=""></option>
                        <option value="<%=ConstantsClass.CANCELLED_STATUS%>">Cancelled</option>
                        <option value="<%=ConstantsClass.COMPLETED_STATUS%>">Completed</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td colspan="2"><input type="button" id="save" value="Save" onclick="buttonClick(this)"></td>
            </tr>
        </table>
        <div class="center">
            <input type="button" id="backtotasks" value="Back to tasks page" onclick="buttonClick(this)">
        </div>
        <div class="center">
            <input type="button" id="backtomain" value="Back to main page" onclick="buttonClick(this)">
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
