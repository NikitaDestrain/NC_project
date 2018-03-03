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
    <script language="Javascript" type="text/javascript" src="calendar&time/jquery.1.4.2.js"></script>
    <link type="text/css" href="calendar&time/latest.css" rel="Stylesheet"/>
    <script type="text/javascript" src="calendar&time/latest.js"></script>
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
    <%
        Date p = (Date) request.getAttribute(ConstantsClass.PLANNED_DATE);
        Date n = (Date) request.getAttribute(ConstantsClass.NOTIFICATION_DATE);
        Date u = (Date) request.getAttribute(ConstantsClass.UPLOAD_DATE);
        Date c = (Date) request.getAttribute(ConstantsClass.CHANGE_DATE);

        String minutesPlanned = null;
        String daysPlanned = null;
        String monthsPlanned = null;

        String minutesNotif = null;
        String daysNotif = null;
        String monthsNotif = null;

        String minutesUpload = null;
        String daysUpload = null;
        String monthsUpload = null;

        String minutesChange = null;
        String daysChange = null;
        String monthsChange = null;

        Calendar planned = Calendar.getInstance();
        Calendar notif = Calendar.getInstance();
        Calendar upload = Calendar.getInstance();
        Calendar change = Calendar.getInstance();
        if (p != null && n != null && u != null && c != null) {

            planned.setTime(p);
            notif.setTime(n);
            upload.setTime(u);
            change.setTime(c);

            minutesPlanned = planned.get(Calendar.MINUTE) + "";
            minutesPlanned = minutesPlanned.length() == 1 ? "0" + minutesPlanned : minutesPlanned;
            daysPlanned = planned.get(Calendar.DAY_OF_MONTH) + "";
            daysPlanned = daysPlanned.length() == 1 ? "0" + daysPlanned : daysPlanned;
            monthsPlanned = (planned.get(Calendar.MONTH) + 1) + "";
            monthsPlanned = monthsPlanned.length() == 1 ? "0" + monthsPlanned : monthsPlanned;

            minutesNotif = notif.get(Calendar.MINUTE) + "";
            minutesNotif = minutesNotif.length() == 1 ? "0" + minutesNotif : minutesNotif;
            daysNotif = planned.get(Calendar.DAY_OF_MONTH) + "";
            daysNotif = daysNotif.length() == 1 ? "0" + daysNotif : daysNotif;
            monthsNotif = (planned.get(Calendar.MONTH) + 1) + "";
            monthsNotif = monthsNotif.length() == 1 ? "0" + monthsNotif : monthsNotif;

            minutesUpload = planned.get(Calendar.MINUTE) + "";
            minutesUpload = minutesUpload.length() == 1 ? "0" + minutesUpload : minutesUpload;
            daysUpload = planned.get(Calendar.DAY_OF_MONTH) + "";
            daysUpload = daysUpload.length() == 1 ? "0" + daysUpload : daysUpload;
            monthsUpload = (planned.get(Calendar.MONTH) + 1) + "";
            monthsUpload = monthsUpload.length() == 1 ? "0" + monthsUpload : monthsUpload;

            minutesChange = planned.get(Calendar.MINUTE) + "";
            minutesChange = minutesChange.length() == 1 ? "0" + minutesChange : minutesChange;
            daysChange = planned.get(Calendar.DAY_OF_MONTH) + "";
            daysChange = daysChange.length() == 1 ? "0" + daysChange : daysChange;
            monthsChange = (planned.get(Calendar.MONTH) + 1) + "";
            monthsChange = monthsChange.length() == 1 ? "0" + monthsChange : monthsChange;
        }
    %>
    <form method="post" action=<%=ConstantsClass.SERVLET_ADDRESS%>>
        <input type="hidden" name="action" value="<%=ConstantsClass.DO_ADD_TASK%>">
        <table>
            <caption>Add user</caption>
            <tr>
                <td class="align-right">Name</td>
                <td class="align-right">
                    <input type="text" name="<%=ConstantsClass.NAME%>"
                           value="<%=request.getAttribute(ConstantsClass.NAME)==null?"":request.getAttribute(ConstantsClass.NAME)%>">
                </td>
            </tr>
            <tr>
                <td class="align-right">Description</td>
                <td class="align-right">
                    <input type="text" name="<%=ConstantsClass.DESCRIPTION%>"
                           value="<%=request.getAttribute(ConstantsClass.DESCRIPTION)==null?"":request.getAttribute(ConstantsClass.DESCRIPTION)%>">
                </td>
            </tr>
            <tr>
                <td class="align-right">Planned date & time</td>
                <td class="align-right">
                    <%--<input type="text" name="<%=ConstantsClass.PLANNED_DATE%>" id="planned"--%>
                           <%--value="<%=daysPlanned + ":" + monthsPlanned +--%>
                            <%--":" + planned.get(Calendar.YEAR) + " " + planned.get(Calendar.HOUR_OF_DAY) + ":" + minutesPlanned%>">--%>
                        <input type="text" name="<%=ConstantsClass.PLANNED_DATE%>" id="planned"
                               value="<%=request.getAttribute(ConstantsClass.PLANNED_DATE)==null?"":request.getAttribute(ConstantsClass.PLANNED_DATE)%>">
                </td>
            </tr>
            <tr>
                <td class="align-right">Notification date & time</td>
                <td class="align-right">
                    <input type="text" name="<%=ConstantsClass.NOTIFICATION_DATE%>" id="notification"
                           value="<%=daysNotif + ":" + monthsNotif +
                            ":" + notif.get(Calendar.YEAR) + " " + notif.get(Calendar.HOUR_OF_DAY) + ":" + minutesNotif%>">
                </td>
            </tr>
            <tr>
                <td class="align-right">Upload date & time</td>
                <td class="align-right">
                    <input type="text" name="<%=ConstantsClass.UPLOAD_DATE%>" id="upload"
                           value="<%=daysUpload + ":" + monthsUpload +
                            ":" + upload.get(Calendar.YEAR) + " " + upload.get(Calendar.HOUR_OF_DAY) + ":" + minutesUpload%>"
                    >
                </td>
            </tr>
            <tr>
                <td class="align-right">Change date & time</td>
                <td class="align-right">
                    <input type="text" name="<%=ConstantsClass.CHANGE_DATE%>" id="change"
                           value="<%=daysChange + ":" + monthsChange +
                            ":" + change.get(Calendar.YEAR) + " " + change.get(Calendar.HOUR_OF_DAY) + ":" + minutesChange%>">
                </td>
            </tr>
            <tr>
                <td class="align-right">Journal name</td>
                <td class="align-right">
                    <select name=<%=ConstantsClass.JOURNAL_NAME%>>
                        <%
                            LinkedList<String> names = (LinkedList<String>) request.getAttribute(ConstantsClass.JOURNAL_NAMES);
                            for (String s : names) {
                        %>
                        <option value="<%=s%>"><%=s%></option>
                        <%
                            }
                        %>
                    </select>
                </td>
            </tr>
            <tr>
                <td colspan="2"><input type="button" id="add" value="Add" onclick="buttonClick(this)"></td>
            </tr>
        </table>
        <input type="hidden" id="hid" name=<%=ConstantsClass.USERACTION%>>
        <div class="center">
            <input type="button" id="backtotasks" value="Back to tasks page" onclick="buttonClick(this)">
        </div>
        <div class="center">
            <input type="button" id="backtomain" value="Back to main page" onclick="buttonClick(this)">
        </div>
    </form>
</div>
</body>
</html>
