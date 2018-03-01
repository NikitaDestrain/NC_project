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
                case "change":
                    document.getElementById("hid").value = "changejournal";
                    break;
                case "back":
                    document.getElementById("hid").value = "backtomain";
                    break;
                case "sort":
                    document.forms[1].submit();
                    break;
            }
            document.forms[0].submit();
        }
    </script>
</head>
<body>
<%
    String message = null;
    SelectResultBean bean = (SelectResultBean) request.getAttribute("bean");
    Calendar calendar = Calendar.getInstance();
    Task task1 = TaskFactory.createTask("name1", TaskStatus.Planned, "description1", calendar.getTime(), calendar.getTime(), calendar.getTime(), calendar.getTime(), 0);
    Task task2 = TaskFactory.createTask("name2", TaskStatus.Overdue, "description2", calendar.getTime(), calendar.getTime(),calendar.getTime(), calendar.getTime(), 0);
    LinkedList<Task> tasks = new LinkedList<>();
    tasks.add(task1);
    tasks.add(task2);
    int count = 0;
%>
<div class="center">TASK SCHEDULER</div>
<div align="center">
    <form method="post" id="mainform" action=<%=ConstantsClass.SERVLET_ADDRESS%>>
        <table class="main-table">
            <caption>Tasks</caption>
            <tr>
                <th class="count">№</th>
                <th class="main-th">Status</th>
                <th class="main-th">Name</th>
                <th class="main-th">Description</th>
                <th class="main-th">Planned date</th>
                <th class="main-th">Notification date</th>
            </tr>
            <%
                java.io.File f = (File) session.getAttribute("tasks");
                Journal journal = null;
                try {
                    JAXBContext context = JAXBContext.newInstance(Journal.class);
                    Unmarshaller unmarshaller = context.createUnmarshaller();
                    journal = (Journal) unmarshaller.unmarshal(f);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }
            %>
            <%
                Calendar planned = Calendar.getInstance();
                Calendar notif = Calendar.getInstance();
                String minutesPlanned;
                String minutesNotif;
                for (Task u : journal.getTasks()) {
                    planned.setTime(u.getPlannedDate());
                    notif.setTime(u.getNotificationDate());
                    minutesPlanned = planned.get(Calendar.MINUTE) + "";
                    minutesPlanned = minutesPlanned.length() == 1 ? "0" + minutesPlanned : minutesPlanned;
                    minutesNotif = notif.get(Calendar.MINUTE) + "";
                    minutesNotif = minutesNotif.length() == 1 ? "0" + minutesNotif : minutesNotif;
            %>
            <tr>
                <td class="count">
                    <label>
                        <%=count%>
                        <input type="radio" name="usernumber" value="<%=count++%>"/>
                    </label>
                </td>
                <td class="main-td">
                    <%=u.getStatus()%>
                    <%--<x:out select="$task/status"/>--%>
                </td>
                <td class="main-td">
                    <%=u.getName()%>
                        <%--<x:out select="$task/name"/>--%>
                </td>
                <td class="main-td">
                    <%=u.getDescription()%>
                    <%--<x:out select="$task/description"/>--%>
                </td>
                <td class="main-td">
                    <%=planned.get(Calendar.DAY_OF_MONTH) + "." + (planned.get(Calendar.MONTH) + 1) +
                        "." + planned.get(Calendar.YEAR) + " " + planned.get(Calendar.HOUR_OF_DAY) + ":" + minutesPlanned%>
                        <%--<x:out select="$task/plannedDate"/>--%>
                </td>
                <td class="main-td">
                    <%=notif.get(Calendar.DAY_OF_MONTH) + "." + (notif.get(Calendar.MONTH) + 1) +
                        "." + notif.get(Calendar.YEAR) + " " + notif.get(Calendar.HOUR_OF_DAY) + ":" + minutesNotif%>
                        <%--<x:out select="$task/notificationDate"/>--%>
                </td>
            </tr>
            <%
                }
            %>
        </table>
        <div align="center" class="button-div">
            <table class="button-table">
                <tr>
                    <label class="label">
                        <td class="button-table-td">Journal #</td>
                        <td class="button-table-td"><select name="<%=ConstantsClass.JOURNAL_NAME%>">
                            <option value="Journal 1">Journal 1</option>
                            <option value="Journal 2">Journal 2</option>
                        </select></td>
                    </label>
                    <td>
                        <input type="button" id="change" value="Change journal" onclick="buttonClick(this)">
                    </td>
                </tr>
                <tr>
                    <td class="button-table-td"><input type="button" id="add" value="Add task"
                                                       onclick="buttonClick(this)"></td>
                    <td class="button-table-td"><input type="button" id="edit" value="Edit task"
                                                       onclick="buttonClick(this)"></td>
                    <td class="button-table-td"><input type="button" id="delete" value="Delete task"
                                                       onclick="buttonClick(this)"></td>
                </tr>
            </table>
            <input type="hidden" id="hid" name=<%=ConstantsClass.USERACTION%>>
        </div>
        <div class="center">
            <%=
            request.getParameter("message") == null ? "" : request.getParameter("message")
            %>
        </div>
        <div align="center">
            <form method="get" action="<%=ConstantsClass.SERVLET_ADDRESS%>" id="sortform">
                <input type="hidden" name="action" value="sortaction">
                <table class="button-table">
                    <tr>
                        <td>Sort by: </td>
                        <td>
                            <select name="sortcolumn">
                                <option value="status">
                                    Status
                                </option>
                                <option value="name">
                                    Name
                                </option>
                                <option value="description">
                                    Description
                                </option>
                                <option name="planneddate">
                                    Planned date
                                </option>
                                <option value="notifdate">
                                    Notification date
                                </option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Criteria:
                        </td>
                        <td>
                            <select name="criteria">
                                <option value="asc">
                                    Ascending
                                </option>
                                <option value="desc">
                                    Descending
                                </option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="button-table-td" colspan="2">
                            <input type="submit" id="sort" value="Sort">
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div class="center">
            <input type="button" id="back" value="Back to main page" onclick="buttonClick(this)">
        </div>
        <input type="hidden" name="action" value=<%=ConstantsClass.DO_CRUD_FROM_TASKS%>>
    </form>
</div>
</body>
</html>