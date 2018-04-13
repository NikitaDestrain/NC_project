<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ page import="auxiliaryclasses.ConstantsClass" %>
<%--
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

        function buttonClickRename(x) {
            switch (x.id) {
                case "rename":
                    if (document.getElementById("prefix").value.length == 0)
                        alert("Enter prefix!");
                    else {
                        document.getElementById("hid").value = "Rename";
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
    Boolean isRename = request.getSession().getAttribute(ConstantsClass.IS_RENAME) != null && (Boolean) request.getSession().getAttribute(ConstantsClass.IS_RENAME);
    Boolean isEdit = request.getSession().getAttribute(ConstantsClass.IS_EDIT) != null && (Boolean) request.getSession().getAttribute(ConstantsClass.IS_EDIT);
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
                <x:parse xml="${requestScope.taskxml}" var="task"/>
                <tr>
                    <td>Name</td>
                    <td>
                        <input type="text" class="form-control" id="addname" name="<%=ConstantsClass.NAME%>"
                               value="<x:out select="$task/task/name"/>">
                    </td>
                </tr>
                <tr>
                    <td>Description</td>
                    <td>
                        <input type="text" class="form-control" name="<%=ConstantsClass.DESCRIPTION%>"
                               value="<x:out select="$task/task/description"/>">
                    </td>
                </tr>
                <tr>
                    <td>Planned date</td>
                    <td>
                        <input type="text" class="form-control" name="<%=ConstantsClass.PLANNED_DATE%>"
                               value="<x:out select="$task/task/planned"/>">
                    </td>
                </tr>
                <tr>
                    <td>Notification date</td>
                    <td>
                        <input type="text" class="form-control" name="<%=ConstantsClass.NOTIFICATION_DATE%>"
                               value="<x:out select="$task/task/notification"/>">
                    </td>
                </tr>
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
                <%
                    }
                %>
                <tr>
                    <td>Journal:</td>
                    <td>
                        <select class="form-control" name=<%=ConstantsClass.JOURNAL_NAME%>>
                            <option value=""/>
                            <x:parse xml="${sessionScope.journalNames}" var="names"/>
                            <x:forEach select="$names/journalNames/name" var="name">
                                <option value="<x:out select="$name"/>"><x:out select="$name"/></option>
                            </x:forEach>
                        </select>
                    </td>
                </tr>
            </table>
            <div align="center">
                <input type="button" class="btn btn-outline-success" id="add" value="Add"
                       onclick="buttonClickAdd(this)">
            </div>
            <%
                    request.getSession().setAttribute(ConstantsClass.IS_ADD, Boolean.FALSE);
                }
            %>

            <%
                if (isEdit) {
            %>
            <div align="center">
                <strong>
                    Current journal
                    name: <%=request.getSession().getAttribute(ConstantsClass.CURRENT_JOURNAL_NAME) == null ? "" : request.getSession().getAttribute(ConstantsClass.CURRENT_JOURNAL_NAME)%>
                </strong>
            </div>
            <input type="hidden" name="<%=ConstantsClass.ACTION%>" value="<%=ConstantsClass.DO_EDIT_TASK%>">
            <x:parse xml="${requestScope.taskxml}" var="task"/>
            <div align="center">
                Task status: <x:out select="$task/task/status"/>
            </div>
            <div align="center">
                Upload date: <x:out select="$task/task/upload"/>
            </div>
            <div align="center">
                Change date: <x:out select="$task/task/change"/>
            </div>
            <table class="table">
                <tr>
                    <td>Name</td>
                    <td>
                        <input type="text" id="editname" class="form-control" name="<%=ConstantsClass.NAME%>"
                               value="<x:out select="$task/task/name"/>">
                    </td>
                </tr>
                <tr>
                    <td>Description</td>
                    <td>
                        <input type="text" class="form-control" name="<%=ConstantsClass.DESCRIPTION%>"
                               value="<x:out select="$task/task/description"/>">
                    </td>
                </tr>
                <tr>
                    <td>Planned date</td>
                    <td>
                        <input type="text" class="form-control" name="<%=ConstantsClass.PLANNED_DATE%>" id="planned"
                               value="<x:out select="$task/task/planned"/>">
                    </td>
                </tr>
                <tr>
                    <td>Notification date</td>
                    <td>
                        <input type="text" class="form-control" name="<%=ConstantsClass.NOTIFICATION_DATE%>"
                               id="notification"
                               value="<x:out select="$task/task/notification"/>">
                    </td>
                </tr>
                <tr>
                    <td>Change journal</td>
                    <td>
                        <select class="form-control" name=<%=ConstantsClass.JOURNAL_NAME%>>
                            <option value=""/>
                            <x:parse xml="${sessionScope.journalNames}" var="names"/>
                            <x:forEach select="$names/journalNames/name" var="name">
                                <option value="<x:out select="$name"/>"><x:out select="$name"/></option>
                            </x:forEach>
                        </select>
                    </td>
                </tr>
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
                    request.getSession().setAttribute(ConstantsClass.IS_EDIT, Boolean.FALSE);
                }
            %>

            <%
                if (isRename) {
            %>
            <div align="center">
                <strong>
                    Rename tasks
                </strong>
                <input type="hidden" name="<%=ConstantsClass.ACTION%>" value="<%=ConstantsClass.DO_RENAME_TASKS%>">
                <br>
                <i>
                    Result example: Prefix.TaskName
                    <br>
                    (Note: If the length of the new name is more than 50 characters, the old name will be left!)
                    <br>
                </i>
                <table>
                    <tr>
                        <td>Prefix</td>
                        Current journal name:
                        <%=request.getSession().getAttribute(ConstantsClass.CURRENT_JOURNAL_NAME) == null ? "" : request.getSession().getAttribute(ConstantsClass.CURRENT_JOURNAL_NAME)%>
                        <td>
                            <input type="text" class="form-control" name="<%=ConstantsClass.PREFIX%>"
                                   id="prefix"
                                   value=<%=request.getSession().getAttribute(ConstantsClass.CURRENT_JOURNAL_NAME) == null ? "" : request.getSession().getAttribute(ConstantsClass.CURRENT_JOURNAL_NAME)%>>
                        </td>
                    </tr>
                </table>
                <br>
                <div class="center">
                    <input type="button" id="rename" class="btn btn-outline-primary" value="Rename"
                           onclick="buttonClickRename(this)">
                </div>
                <br>
            </div>
            <%
                    request.getSession().setAttribute(ConstantsClass.IS_RENAME, Boolean.FALSE);
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
