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
                    break;
                case "edit":
                    document.getElementById("hid").value = "Update";
                    break;
                case "delete":
                    if (confirm("Are you sure want to delete this task?")) {
                        document.getElementById("hid").value = "Delete";
                    }
                    break;
                case "back":
                    document.getElementById("hid").value = "backtomain";
                    break;
                case "sort":
                    document.getElementById("hid").value = "Sort";
                    break;
            }
            document.forms[0].submit();
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
                Journal journal = (Journal) session.getAttribute(ConstantsClass.JOURNAL_PARAMETER);
                int count = 0;
                Calendar planned = Calendar.getInstance();
                Calendar notif = Calendar.getInstance();
                Calendar upload = Calendar.getInstance();
                Calendar change = Calendar.getInstance();
                String minutesPlanned;
                String minutesNotif;
                String minutesUpload;
                String minutesChange;
                if (journal != null && journal.getTasks() != null) {
                    for (Task u : journal.getTasks()) {
                        planned.setTime(u.getPlannedDate());
                        notif.setTime(u.getNotificationDate());
                        upload.setTime(u.getUploadDate());
                        change.setTime(u.getChangeDate());
                        minutesPlanned = planned.get(Calendar.MINUTE) + "";
                        minutesPlanned = minutesPlanned.length() == 1 ? "0" + minutesPlanned : minutesPlanned;
                        minutesNotif = notif.get(Calendar.MINUTE) + "";
                        minutesNotif = minutesNotif.length() == 1 ? "0" + minutesNotif : minutesNotif;
                        minutesUpload = planned.get(Calendar.MINUTE) + "";
                        minutesUpload = minutesUpload.length() == 1 ? "0" + minutesUpload : minutesUpload;
                        minutesChange = planned.get(Calendar.MINUTE) + "";
                        minutesChange = minutesChange.length() == 1 ? "0" + minutesChange : minutesChange;
            %>
            <tr>
                <td class="count">
                    <label>
                        <%=count%>
                        <input type="radio" name="<%=ConstantsClass.USERNUMBER%>" value="<%=count++%>"/>
                    </label>
                </td>
                <td class="main-td">
                    <%=u.getStatus()%>
                </td>
                <td class="main-td">
                    <%=u.getName()%>
                </td>
                <td class="main-td">
                    <%=u.getDescription() == null ? "" : u.getDescription()%>
                </td>
                <td class="main-td">
                    <%=planned.get(Calendar.DAY_OF_MONTH) + "." + (planned.get(Calendar.MONTH) + 1) +
                            "." + planned.get(Calendar.YEAR) + " " + planned.get(Calendar.HOUR_OF_DAY) + ":" + minutesPlanned%>
                </td>
                <td class="main-td">
                    <%=notif.get(Calendar.DAY_OF_MONTH) + "." + (notif.get(Calendar.MONTH) + 1) +
                            "." + notif.get(Calendar.YEAR) + " " + notif.get(Calendar.HOUR_OF_DAY) + ":" + minutesNotif%>
                </td>
                <td class="main-td">
                    <%=upload.get(Calendar.DAY_OF_MONTH) + "." + (upload.get(Calendar.MONTH) + 1) +
                            "." + upload.get(Calendar.YEAR) + " " + upload.get(Calendar.HOUR_OF_DAY) + ":" + minutesUpload%>
                </td>
                <td class="main-td">
                    <%=change.get(Calendar.DAY_OF_MONTH) + "." + (change.get(Calendar.MONTH) + 1) +
                            "." + change.get(Calendar.YEAR) + " " + change.get(Calendar.HOUR_OF_DAY) + ":" + minutesChange%>
                </td>
            </tr>
            <%
                }
            } else {
            %>
            <div align="center">
                <strong>Incorrect journal!</strong>
            </div>
            <%
                }
            %>
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
            request.getAttribute(ConstantsClass.MESSAGE_ATTRIBUTE) == null ? "" : request.getAttribute(ConstantsClass.MESSAGE_ATTRIBUTE)
            %>
        </div>
    </form>
</div>
</body>
</html>