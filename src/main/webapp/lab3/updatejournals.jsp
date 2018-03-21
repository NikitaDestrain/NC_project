<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="auxiliaryclasses.ConstantsClass" %><%--
  Created by IntelliJ IDEA.
  User: ывв
  Date: 26.02.2018
  Time: 12:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update journals</title>
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
                case
                "back"
                :
                    document.getElementById("hid").value = "backtomain";
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
                case "back":
                    document.getElementById("hid").value = "backtomain";
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
    <form method="post" action="<%=ConstantsClass.JOURNAL_UPDATE_SERVLET_ADDRESS%>" role="form">
        <input type="hidden" id="hid" name=<%=ConstantsClass.USERACTION%>>
        <div class="form-group">
            <%
                if (isAdd) {
            %>
            <input type="hidden" name="<%=ConstantsClass.ACTION%>" value="<%=ConstantsClass.DO_ADD_JOURNAL%>">
            <div align="center"><strong>Add journal</strong></div>

            <table class="table">
                <%
                    if (request.getAttribute(ConstantsClass.JOURNAL_PARAMETER) != null) {
                %>
                <x:parse xml="${requestScope.journal}" var="journal"/>
                <tr>
                    <td class="align-right">Name</td>
                    <td class="align-right">
                        <input type="text" id="addname" class="form-control" name="<%=ConstantsClass.NAME%>"
                               value="<x:out select="$journal/journal/name"/>">
                    </td>
                </tr>
                <tr>
                    <td class="align-right">Description</td>
                    <td class="align-right">
                        <input type="text" class="form-control" name="<%=ConstantsClass.DESCRIPTION%>"
                               value="<x:out select="$journal/journal/description"/>">
                    </td>
                </tr>
                <%
                } else {
                %>
                <tr>
                    <td class="align-right">Name</td>
                    <td class="align-right">
                        <input type="text" id="addname1" class="form-control" name="<%=ConstantsClass.NAME%>"
                               value="">
                    </td>
                </tr>
                <tr>
                    <td class="align-right">Description</td>
                    <td class="align-right">
                        <input type="text" class="form-control" name="<%=ConstantsClass.DESCRIPTION%>"
                               value="">
                    </td>
                </tr>
                <%
                    }
                %>
            </table>
            <div align="center">
                <input type="button" class="btn btn-outline-success"
                       id="add" value="Add" onclick="buttonClickAdd(this)">
            </div>
            <%
            } else {
            %>
            <input type="hidden" name="<%=ConstantsClass.ACTION%>" value="<%=ConstantsClass.DO_EDIT_JOURNAL%>">
            <div align="center"><strong>Edit journal</strong></div>

            <table class="table">

                <x:parse xml="${requestScope.journal}" var="journal"/>
                <tr>
                    <td class="align-right">Name</td>
                    <td class="align-right">
                        <input type="text" class="form-control" id="editname"
                               name="<%=ConstantsClass.NAME%>"
                               value="<x:out select="$journal/journal/name"/>">
                    </td>
                </tr>
                <tr>
                    <td class="align-right">Description</td>
                    <td class="align-right">
                        <input type="text" class="form-control"
                               name="<%=ConstantsClass.DESCRIPTION%>"
                               value="<x:out select="$journal/journal/description"/>">
                    </td>
                </tr>
            </table>
            <div align="center">
                <input type="button" class="btn btn-outline-success" id="save" value="Save" onclick="buttonClickEdit(this)">
            </div>
            <%
                }
            %>
            <div class="center">
                <input type="button" id="back" class="btn btn-outline-primary"
                       value="Back to main page" onclick="buttonClickAdd(this)">
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
