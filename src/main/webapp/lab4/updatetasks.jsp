<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="auxiliaryclasses.ConstantsClass" %>
<%@ page import="server.model.Journal" %><%--
  Created by IntelliJ IDEA.
  User: ывв
  Date: 18.02.2018
  Time: 16:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update tasks</title>
    <style type="text/css">
        <%@include file="../bootstrap/css/bootstrap.min.css"%>
    </style>

    <script type="text/javascript">
        function buttonClickAdd(x) {
            switch (x.id) {
                case "add":
                    if (document.getElementById("addname") != null) {
                        if (document.getElementById("addname").value.length == 0) {
                            alert("Enter name!");
                        } else {
                            document.getElementById("hid").value = "Add";
                            document.forms[0].submit();
                        }
                    } else if (document.getElementById("addname1") != null) {
                        if (document.getElementById("addname1").value.length == 0) {
                            alert("Enter name!");
                        } else {
                            document.getElementById("hid").value = "Add";
                            document.forms[0].submit();
                        }
                    }
                    break;

                case "backtomain":
                    document.getElementById("hid").value = "backtomain";
                    document.forms[0].submit();
                    break;
                case "backtotasks":
                    document.getElementById("hid").value = "backtotasks";
                    document.forms[0].submit();
                    break;
            }
        }

        function buttonClickEdit(x) {
            switch (x.id) {
                case "save":
                    if (document.getElementById("editname").value.length == 0)
                        alert("Enter name!");
                    else {
                        document.getElementById("hid").value = "Save";
                        document.forms[0].submit();
                    }
                    break;
                case "backtomain":
                    document.getElementById("hid").value = "backtomain";
                    document.forms[0].submit();
                    break;
                case "backtotasks":
                    document.getElementById("hid").value = "backtotasks";
                    document.forms[0].submit();
                    break;
            }
        }
    </script>
</head>
<body>
<%
    Boolean isAdd = request.getSession().getAttribute(ConstantsClass.IS_ADD) != null && (Boolean) request.getSession().getAttribute(ConstantsClass.IS_ADD);
%>
<div align="center">
    <form method="post" action="<%=ConstantsClass.TASK_UPDATE_SERVLET_ADDRESS%>" role="form">
        <input type="hidden" id="hid" name=<%=ConstantsClass.USERACTION%>>
        <div class="form-group">
            <%
                if (isAdd) {
            %>
            <input type="hidden" name="<%=ConstantsClass.ACTION%>" value="<%=ConstantsClass.DO_ADD_TASK%>">
            <div align="center"><strong>Add task</strong></div>
            <div align="center">
                <strong>
                    Current journal
                    name: <%=request.getSession().getAttribute(ConstantsClass.CURRENT_JOURNAL_NAME) == null ? "" : request.getSession().getAttribute(ConstantsClass.CURRENT_JOURNAL_NAME)%>
                </strong>
            </div>
            <table class="table">
            <%
                if (request.getAttribute(ConstantsClass.CURRENT_TASK_XML) != null) {
            %>
            <x:transform xml="${requestScope.taskxml}" xslt="${requestScope.xslTask}"/>
            <x:transform xml="${sessionScope.journalNames}" xslt="${sessionScope.xslNames}"/>
            <%
                } else {
            %>
            <tr>
                <td>Name</td>
                <td>
                    <input type="text" class="form-control" id="addname1" name="<%=ConstantsClass.NAME%>"
                           value="">
                </td>
            </tr>
            <tr>
                <td>Description</td>
                <td>
                    <input type="text" class="form-control" name="<%=ConstantsClass.DESCRIPTION%>"
                           value="">
                </td>
            </tr>
            <tr>
                <td>Planned date</td>
                <td>
                    <input type="text" class="form-control" name="<%=ConstantsClass.PLANNED_DATE%>"
                           value="">
                </td>
            </tr>
            <tr>
                <td>Notification date</td>
                <td>
                    <input type="text" class="form-control" name="<%=ConstantsClass.NOTIFICATION_DATE%>"
                           value="">
                </td>
            </tr>
                <x:transform xml="${sessionScope.journalNames}" xslt="${sessionScope.xslNames}"/>
            <%
                }
            %>
            </table>
            <div align="center">
                <input type="button" class="btn btn-outline-success" id="add" value="Add"
                       onclick="buttonClickAdd(this)">
            </div>
            <%
            } else {
            %>
            <div align="center">
                <strong>
                    Current journal
                    name: <%=request.getSession().getAttribute(ConstantsClass.CURRENT_JOURNAL_NAME) == null ? "" : request.getSession().getAttribute(ConstantsClass.CURRENT_JOURNAL_NAME)%>
                </strong>
            </div>
            <input type="hidden" name="<%=ConstantsClass.ACTION%>" value="<%=ConstantsClass.DO_EDIT_TASK%>">
            <table class="table">
                <x:transform xml="${requestScope.taskxml}" xslt="${requestScope.xslTask}"/>
                <x:transform xml="${sessionScope.journalNames}" xslt="${sessionScope.xslNames}"/>
                <tr>
                    <td>Change task status</td>
                    <td>
                        <select class="form-control" name="<%=ConstantsClass.STATUS%>">
                            <option value=""></option>
                            <option value="<%=ConstantsClass.CANCELLED_STATUS%>">Cancelled</option>
                            <option value="<%=ConstantsClass.COMPLETED_STATUS%>">Completed</option>
                        </select>
                    </td>
                </tr>
            </table>
            <div align="center">
                <input type="button" class="btn btn-outline-success" id="save" value="Save"
                       onclick="buttonClickEdit(this)">
            </div>
            <%
                }
            %>
            <div class="center">
                <input type="button" id="backtotasks" class="btn btn-outline-primary" value="Back to tasks page"
                       onclick="buttonClickAdd(this)">
            </div>
            <div class="center">
                <input type="button" id="backtomain" class="btn btn-outline-primary" value="Back to main page"
                       onclick="buttonClickAdd(this)">
            </div>
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
