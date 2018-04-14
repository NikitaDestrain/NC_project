<%@ page import="auxiliaryclasses.ConstantsClass" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%--
  Created by IntelliJ IDEA.
  User: ывв
  Date: 04.04.2018
  Time: 16:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Journal XML</title>
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
                case "imp" :
                    document.getElementById("hid").value = "imp";
                    document.forms[0].submit();
                    break;
                case "submit":
                    document.getElementById("hid").value = "choosestrategy";
                    document.forms[0].submit();
                    break;
            }
        }
    </script>
</head>
<body>
<form id="form" method="post" action="<%=ConstantsClass.IMPORT_SERVLET_ADDRESS%>" role="form"
      enctype="multipart/form-data">
    <div class="form-group">
        <input type="hidden" id="hid" name="<%=ConstantsClass.ACTION%>" value="<%=ConstantsClass.CHOOSE_STRATEGY%>">
        <div align="center"><strong>Data from xml</strong></div>
        <%
            if (request.getSession().getAttribute(ConstantsClass.JOURNAL_CONTAINER_PARAMETER) != null &&
                    request.getAttribute(ConstantsClass.XSL_JOURNAL_CONTAINER_PARAMETER) != null) {
        %>
        <x:transform xml="${sessionScope.journalContainer}" xslt="${requestScope.xslJournals}"/>
        <div align="center">
            Choose a strategy for journals: <br/>
            <table>
                <tr>
                    <td>
                        <select name="<%=ConstantsClass.JOURNAL_STRATEGY%>">
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
        <div align="center">
            Choose a strategy for tasks: <br/>
            <table>
                <tr>
                    <td>
                        <select name="<%=ConstantsClass.TASK_STRATEGY%>">
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
        <div align="center">
            <input type="submit" id="submit" class="btn btn-outline-primary" value="OK"/>
        </div>
        <%
            } else {
        %>
        <div align="center">
            <strong>
                Could not display the XML you've chosen :(
            </strong>
        </div>
        <%
            }
        %>
    </div>
</form>
</body>
</html>
