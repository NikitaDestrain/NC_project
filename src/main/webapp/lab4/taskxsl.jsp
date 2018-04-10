<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ page import="auxiliaryclasses.ConstantsClass" %><%--
  Created by IntelliJ IDEA.
  User: ывв
  Date: 04.04.2018
  Time: 16:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Task XSL</title>
    <style type="text/css">
        <%@include file="../bootstrap/css/bootstrap.min.css"%>
    </style>
    <script type="text/javascript">
        function importSubmit(x) {
            var imp = x.value;
            if (imp.localeCompare("") != 0) {
                if (confirm("Do you really want to import data from " + imp + "?")) {
                    document.getElementById("imp").disabled = false;
                }
            }
        }

        function clearImport() {
            document.getElementById("import").value = "";
            document.getElementById("imp").disabled = true;
        }

        function buttonClick(x) {
            switch (x.id) {
                case "imp":
                    document.getElementById("hid").value = "imp";
                    document.getElementById("form").submit();
                    break;
                case "submit" :
                    document.getElementById("hid").value = "choosestrategy";
                    document.getElementById("form").submit();
                    break;
            }
        }
    </script>
</head>
<body>
<div align="center"><strong>TASKS</strong></div>
<form id="form" method="post" action="<%=ConstantsClass.IMPORT_SERVLET_ADDRESS%>" role="form" enctype="multipart/form-data">
    <input type="hidden" id="hid" name="<%=ConstantsClass.ACTION%>" value="<%=ConstantsClass.CHOOSE_STRATEGY%>">
    <div class="form-group">
        <table class="table">
            <tr>
                <th>ID</th>
                <th>Status</th>
                <th>Name</th>
                <th>Description</th>
                <th>Planned date</th>
                <th>Notification date</th>
                <th>Upload date</th>
                <th>Change date</th>
            </tr>
            <x:transform xml="${sessionScope.journal}" xslt="${requestScope.xslJournal}"/>
        </table>
        <div align="center">
            Choose a strategy for journals: <br/>
            <table>
                <tr>
                    <td>
                        <select name="<%=ConstantsClass.JOURNAL_STRATEGY%>">
                            <option value=""></option>
                            <option value="<%=ConstantsClass.REPLACE%>">
                                Replace
                            </option>
                            <option value="<%=ConstantsClass.IGNORE%>">
                                Ignore
                            </option>
                            <option value="<%=ConstantsClass.EXCEPTION%>">
                                Throw an exception
                            </option>
                        </select>
                    </td>
                </tr>
            </table>
        </div>
        <%--<div align="center">--%>
            <%--Choose a strategy for tasks: <br/>--%>
            <%--<table>--%>
                <%--<tr>--%>
                    <%--<td>--%>
                        <%--<select name="<%=ConstantsClass.TASK_STRATEGY%>">--%>
                            <%--<option value=""></option>--%>
                            <%--<option value="<%=ConstantsClass.REPLACE%>">--%>
                                <%--Replace--%>
                            <%--</option>--%>
                            <%--<option value="<%=ConstantsClass.IGNORE%>">--%>
                                <%--Ignore--%>
                            <%--</option>--%>
                            <%--<option value="<%=ConstantsClass.EXCEPTION%>">--%>
                                <%--Throw an exception--%>
                            <%--</option>--%>
                        <%--</select>--%>
                    <%--</td>--%>
                <%--</tr>--%>
            <%--</table>--%>
        <%--</div>--%>
        <div align="center">
            <input type="submit" id="submit" class="btn btn-outline-primary" value="OK"/>
        </div>
    </div>
</form>
</body>
</html>
