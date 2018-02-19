<%@ page import="server.model.Task" %>
<%@ page import="auxiliaryclasses.ConstantsClass" %><%--
  Created by IntelliJ IDEA.
  User: ывв
  Date: 18.02.2018
  Time: 18:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Notification page</title>
    <script language="Javascript" type="text/javascript" src="calendar&time/jquery.1.4.2.js"></script>
    <link type="text/css" href="calendar&time/latest.css" rel="Stylesheet"/>
    <script type="text/javascript" src="calendar&time/latest.js"></script>
    <style type="text/css">
        body {
            margin-bottom: 10px;
            font-weight: bold;
            font-family: "Yu Gothic Light"
        }

        div {
            width: auto;
            height: auto;
            background-color: khaki;
        }

        .align-right {
            text-align: right;
        }
    </style>
    <script type="text/javascript">
        function buttonClick(x) {
            switch (x.id) {
                case "finish":
                    document.getElementById("hid").value = "Finish";
                    break;
                case "cancel":
                    document.getElementById("hid").value = "Cancel";
                    break;
                case "OK" :
                    document.getElementById("hid").value = "OK";
                    break;
            }
            document.forms[0].submit();
        }
    </script>
</head>
<body>
<%
    Task task = (Task) request.getAttribute("task");
%>
<div align="center">
    Notification!<br/>
    <%=task==null?"":task.getName()%><br/>
    <%=task==null?"":task.getDescription()%><br/>
</div>
<div align="center">
    <form method="post" action=<%=ConstantsClass.SERVLET_ADDRESS%>>
        <table>
            <tr>
                <td><input type="button" id="cancel" value="Cancel task" onclick="buttonClick(this)"></td>
                <td><input type="button" id="finish" value="Finish task" onclick="buttonClick(this)"></td>
            </tr>
            <tr>
                <label>
                    <td><strong>Reschedule task on</strong></td>
                    <td><input type="text" name=<%=ConstantsClass.RESCHEDULE_TASK%> class="datepickerTimeField" value=""></td>
                </label>
            </tr>
            <tr>
                <td class="align-right">
                    <input type="button" id="OK" value="OK" onclick="buttonClick(this)">
                </td>
            </tr>
        </table>
        <input type="hidden" value= <%=ConstantsClass.DO_NOTIFICATION_ACTION%> name=<%=ConstantsClass.ACTION%>>
        <input type="hidden" id="hid" name=<%=ConstantsClass.USERACTION%>>
    </form>
</div>
</body>
</html>
