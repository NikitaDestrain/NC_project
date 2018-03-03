<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="com.sun.xml.internal.ws.server.ServerRtException" %>
<%@ page import="testservlet.beans.SelectResultBean" %>
<%@ page import="testservlet.beans.User" %>
<%@ page import="auxiliaryclasses.ConstantsClass" %>
<%@ page import="server.model.Task" %>
<%@ page import="server.factories.TaskFactory" %>
<%@ page import="server.model.TaskStatus" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="javax.xml.bind.JAXBContext" %>
<%@ page import="server.model.Journal" %>
<%@ page import="javax.xml.bind.JAXBException" %>
<%@ page import="javax.xml.bind.Unmarshaller" %>
<%@ page import="java.io.File" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %><%--
  Created by IntelliJ IDEA.
  User: ывв
  Date: 10.02.2018
  Time: 17:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Task scheduler</title>
    <style type="text/css">
        .main-table {
            width: 100%;
            height: auto;
            padding: 10px;
            border-collapse: collapse;
            background-color: khaki;
        }

        .main-th {
            padding: 5px;
            border: 1px solid black;
            text-align: center;
        }

        .main-td {
            border: 1px solid black;
            padding: 5px;
            text-align: left;
            padding-right: 20px;
        }

        .button-table {
            width: auto;
            height: auto;
            padding: 10px;
            border-collapse: collapse;
            background-color: khaki;
        }

        .button-table-td {
            padding: 5px;
            text-align: center;
            padding-right: 20px;
        }

        caption {
            margin-bottom: 10px;
            font-weight: bold;
            font-family: "Yu Gothic Light";
        }

        .count {
            background-color: darkkhaki;
            font-weight: bold;
            border: 1px solid black;
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
            font-weight: bold;
        }

        .namelabel {
            text-align: right;
        }

        .button-div {
            background-color: khaki;
            width: auto;
            height: auto;
        }
    </style>
    <script type="text/javascript">
        function buttonClick(x) {
            switch (x.id) {
                case "add":
                    document.getElementById("hid").value = "Add";
                    document.forms[0].submit();
                    break;
                case "edit":
                    document.getElementById("hid").value = "Update";
                    document.forms[0].submit();
                    break;
                case "delete":
                    var radios = document.getElementsByName("usernumber");
                    var checked = false;
                    for (var i = 0; i < radios.length; i++) {
                        if (radios[i].checked) {
                            checked = true;
                            break;
                        }
                    }
                    if (checked) {
                        if (confirm("Are you sure want to delete this task?")) {
                            document.getElementById("hid").value = "Delete";
                            document.forms[0].submit();
                        }
                    }
                    else {
                        alert("Select a journal to perform an action!")
                    }
                    break;
                case "back":
                    document.getElementById("hid").value = "backtomain";
                    document.forms[0].submit();
                    break;
                case "sort":
                    document.getElementById("hid").value = "Sort";
                    document.forms[0].submit();
                    break;
            }
        }
    </script>
</head>
<body>
<div class="center">TASK SCHEDULER</div>
<div align="center">
    <form method="post" id="mainform" action=<%=ConstantsClass.SERVLET_ADDRESS%>>

        <input type="hidden" id="hid" name=<%=ConstantsClass.USERACTION%>>
        <input type="hidden" name="<%=ConstantsClass.ACTION%>" value=<%=ConstantsClass.DO_CRUD_FROM_TASKS%>>

        <table class="main-table">
            <caption>Tasks</caption>
            <tr>
                <th class="count">№</th>
                <th class="main-th">Status</th>
                <th class="main-th">Name</th>
                <th class="main-th">Description</th>
                <th class="main-th">Planned date</th>
                <th class="main-th">Notification date</th>
                <th class="main-th">Upload date</th>
                <th class="main-th">Change date</th>
            </tr>
            <%
                int count = 0;
                Calendar planned = Calendar.getInstance();
                Calendar notif = Calendar.getInstance();
                Calendar upload = Calendar.getInstance();
                Calendar change = Calendar.getInstance();

                String minutesPlanned;
                String daysPlanned;
                String monthsPlanned;

                String minutesNotif;
                String daysNotif;
                String monthsNotif;

                String daysUpload;
                String monthsUpload;

                String daysChange;
                String monthsChange;

            %>
            <x:parse xml="${sessionScope.journal}" var="container"/>
            <x:forEach select="$container/journal/tasks/entry" var="task">
            <%--<%--%>
                <%--planned.setTime(u.getPlannedDate());--%>
                <%--notif.setTime(u.getNotificationDate());--%>
                <%--upload.setTime(u.getUploadDate());--%>
                <%--change.setTime(u.getChangeDate());--%>

                <%--minutesPlanned = planned.get(Calendar.MINUTE) + "";--%>
                <%--minutesPlanned = minutesPlanned.length() == 1 ? "0" + minutesPlanned : minutesPlanned;--%>
                <%--daysPlanned = planned.get(Calendar.DAY_OF_MONTH) + "";--%>
                <%--daysPlanned = daysPlanned.length() == 1 ? "0" + daysPlanned : daysPlanned;--%>
                <%--monthsPlanned = (planned.get(Calendar.MONTH) + 1) + "";--%>
                <%--monthsPlanned = monthsPlanned.length() == 1 ? "0" + monthsPlanned : monthsPlanned;--%>

                <%--minutesNotif = notif.get(Calendar.MINUTE) + "";--%>
                <%--minutesNotif = minutesNotif.length() == 1 ? "0" + minutesNotif : minutesNotif;--%>
                <%--daysNotif = planned.get(Calendar.DAY_OF_MONTH) + "";--%>
                <%--daysNotif = daysNotif.length() == 1 ? "0" + daysNotif : daysNotif;--%>
                <%--monthsNotif = (planned.get(Calendar.MONTH) + 1) + "";--%>
                <%--monthsNotif = monthsNotif.length() == 1 ? "0" + monthsNotif : monthsNotif;--%>

                <%--daysUpload = planned.get(Calendar.DAY_OF_MONTH) + "";--%>
                <%--daysUpload = daysUpload.length() == 1 ? "0" + daysUpload : daysUpload;--%>
                <%--monthsUpload = (planned.get(Calendar.MONTH) + 1) + "";--%>
                <%--monthsUpload = monthsUpload.length() == 1 ? "0" + monthsUpload : monthsUpload;--%>

                <%--daysChange = planned.get(Calendar.DAY_OF_MONTH) + "";--%>
                <%--daysChange = daysChange.length() == 1 ? "0" + daysChange : daysChange;--%>
                <%--monthsChange = (planned.get(Calendar.MONTH) + 1) + "";--%>
                <%--monthsChange = monthsChange.length() == 1 ? "0" + monthsChange : monthsChange;--%>
            <%--%>--%>
            <tr>
                <td class="count">
                    <label>
                        <%=count%>
                        <input type="radio" name="<%=ConstantsClass.USERNUMBER%>" value="<%=count++%>"/>
                    </label>
                </td>
                <td class="main-td">
                    <x:out select="$task/value/status"/>
                </td>
                <td class="main-td">
                    <x:out select="$task/value/name"/>
                </td>
                <td class="main-td">
                    <x:out select="$task/value/description"/>
                </td>
                <td class="main-td">
                    <%--<%=daysPlanned + ":" + monthsPlanned +--%>
                            <%--":" + planned.get(Calendar.YEAR) + " " + planned.get(Calendar.HOUR_OF_DAY) + ":" + minutesPlanned%>--%>
                        <x:out select="$task/value/plannedDate"/>
                </td>
                <td class="main-td">
                    <%--<%=daysNotif + ":" + monthsNotif +--%>
                            <%--":" + notif.get(Calendar.YEAR) + " " + notif.get(Calendar.HOUR_OF_DAY) + ":" + minutesNotif%>--%>
                        <x:out select="$task/value/notificationDate"/>
                </td>
                <td class="main-td">
                    <%--<%=daysUpload + ":" + monthsUpload +--%>
                            <%--":" + upload.get(Calendar.YEAR)%>--%>
                        <x:out select="$task/value/uploadDate"/>
                </td>
                <td class="main-td">
                    <%--<%=daysChange + ":" + monthsChange +--%>
                            <%--":" + change.get(Calendar.YEAR)%>--%>
                        <x:out select="$task/value/changeDate"/>
                </td>
            </tr>
            </x:forEach>
        </table>
        <div align="center" class="button-div">
            <table class="button-table">
                <tr>
                    <td class="button-table-td"><input type="button" id="add" value="Add task"
                                                       onclick="buttonClick(this)"></td>
                    <td class="button-table-td"><input type="button" id="edit" value="Edit task"
                                                       onclick="buttonClick(this)"></td>
                    <td class="button-table-td"><input type="button" id="delete" value="Delete task"
                                                       onclick="buttonClick(this)"></td>
                </tr>
            </table>
        </div>
        <div align="center">
            <table class="button-table">
                <tr>
                    <td>Sort by:</td>
                    <td>
                        <select name="<%=ConstantsClass.SORT_COLUMN%>">
                            <option value="<%=ConstantsClass.STATUS%>">
                                Status
                            </option>
                            <option value="<%=ConstantsClass.NAME%>">
                                Name
                            </option>
                            <option value="<%=ConstantsClass.DESCRIPTION%>">
                                Description
                            </option>
                            <option name="<%=ConstantsClass.PLANNED_DATE%>">
                                Planned date
                            </option>
                            <option value="<%=ConstantsClass.NOTIFICATION_DATE%>">
                                Notification date
                            </option>
                            <option value="<%=ConstantsClass.UPLOAD_DATE%>">
                                Upload date
                            </option>
                            <option value="<%=ConstantsClass.CHANGE_DATE%>">
                                Change date
                            </option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        Criteria:
                    </td>
                    <td>
                        <select name="<%=ConstantsClass.SORT_CRITERIA%>">
                            <option value="<%=ConstantsClass.SORT_ASC%>">
                                Ascending
                            </option>
                            <option value="<%=ConstantsClass.SORT_DESC%>">
                                Descending
                            </option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td class="button-table-td" colspan="3">
                        <input type="button" id="sort" value="Sort" onclick="buttonClick(this)">
                    </td>
                </tr>
            </table>
        </div>
        <div class="center">
            <input type="button" id="back" value="Back to main page" onclick="buttonClick(this)">
        </div>
        <div class="center">
            <%=
                request
                .
                getAttribute
                (
                ConstantsClass
                .
                MESSAGE_ATTRIBUTE
                )
                ==
                null
                ?
                ""
                :
                request
                .
                getAttribute
                (
                ConstantsClass
                .
                MESSAGE_ATTRIBUTE
                )
            %>
        </div>
    </form>
</div>
</body>
</html>