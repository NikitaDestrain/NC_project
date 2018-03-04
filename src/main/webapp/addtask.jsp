<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ page import="auxiliaryclasses.ConstantsClass" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %><%--
  Created by IntelliJ IDEA.
  User: ывв
  Date: 18.02.2018
  Time: 16:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add task</title>
    <style type="text/css" media="screen">
        body {
            font-weight: bold;
            font-family: "Yu Gothic Light"
        }

        table {
            width: auto;
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

        form {
            display: inline-block;
            text-align: center;
        }

        .align-right {
            text-align: right;
        }
    </style>
    <script type="text/javascript">
        function buttonClick(x) {
            switch (x.id) {
                case "add":
                    document.getElementById("hid").value = "Add";
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
    <form method="post" action=<%=ConstantsClass.SERVLET_ADDRESS%>>

        <input type="hidden" name="<%=ConstantsClass.ACTION%>" value="<%=ConstantsClass.DO_ADD_TASK%>">
        <input type="hidden" id="hid" name=<%=ConstantsClass.USERACTION%>>

        <table>
            <caption>Add task</caption>
        <%
            if (request.getSession().getAttribute(ConstantsClass.CURRENT_TASK) != null) {
        %>
        <x:parse xml="${sessionScope.task}" var="task"/>
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
                    <input type="text" name="<%=ConstantsClass.PLANNED_DATE%>"
                           value="<x:out select="$task/task/planned"/>">
                </td>
            </tr>
            <tr>
                <td class="align-right">Notification date</td>
                <td class="align-right">
                    <input type="text" name="<%=ConstantsClass.NOTIFICATION_DATE%>"
                           value="<x:out select="$task/task/notification"/>">
                </td>
            </tr>
            <tr>
                <td class="align-right">Upload date</td>
                <td class="align-right">
                    <input type="text" name="<%=ConstantsClass.UPLOAD_DATE%>"
                           value="<x:out select="$task/task/upload"/>"
                    >
                </td>
            </tr>
            <tr>
                <td class="align-right">Change date</td>
                <td class="align-right">
                    <input type="text" name="<%=ConstantsClass.CHANGE_DATE%>"
                           value="<x:out select="$task/task/change"/>">
                </td>
            </tr>
            <%
                } else {
            %>
                <caption>Add task</caption>
                <tr>
                    <td class="align-right">Name</td>
                    <td class="align-right">
                        <input type="text" name="<%=ConstantsClass.NAME%>"
                               value="">
                    </td>
                </tr>
                <tr>
                    <td class="align-right">Description</td>
                    <td class="align-right">
                        <input type="text" name="<%=ConstantsClass.DESCRIPTION%>"
                               value="">
                    </td>
                </tr>
                <tr>
                    <td class="align-right">Planned date</td>
                    <td class="align-right">
                        <input type="text" name="<%=ConstantsClass.PLANNED_DATE%>"
                               value="">
                    </td>
                </tr>
                <tr>
                    <td class="align-right">Notification date</td>
                    <td class="align-right">
                        <input type="text" name="<%=ConstantsClass.NOTIFICATION_DATE%>"
                               value="">
                    </td>
                </tr>
                <tr>
                    <td class="align-right">Upload date</td>
                    <td class="align-right">
                        <input type="text" name="<%=ConstantsClass.UPLOAD_DATE%>"
                               value=""
                        >
                    </td>
                </tr>
                <tr>
                    <td class="align-right">Change date</td>
                    <td class="align-right">
                        <input type="text" name="<%=ConstantsClass.CHANGE_DATE%>"
                               value="">
                    </td>
                </tr>
            <%
                }
            %>
            <tr>
                <td class="align-right">Journal name</td>
                <td class="align-right">
                    <select name=<%=ConstantsClass.JOURNAL_NAME%>>
                        <x:parse xml="${sessionScope.journalNames}" var="names"/>
                        <x:forEach select="$names/journalNames/name" var="name">
                            <option value="<x:out select="$name"/>"><x:out select="$name"/></option>
                        </x:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td colspan="2"><input type="button" id="add" value="Add" onclick="buttonClick(this)"></td>
            </tr>
        </table>
        <div class="center">
            <input type="button" id="backtotasks" value="Back to tasks page" onclick="buttonClick(this)">
        </div>
        <div class="center">
            <input type="button" id="backtomain" value="Back to main page" onclick="buttonClick(this)">
        </div>
        <div class="center">
            <%=request.getAttribute(ConstantsClass.MESSAGE_ATTRIBUTE)==null?"":request.getAttribute(ConstantsClass.MESSAGE_ATTRIBUTE)%>
        </div>
    </form>
</div>
</body>
</html>
