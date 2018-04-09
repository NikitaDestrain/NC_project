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
                case "imp":
                    document.getElementById("hid").value = "imp";
                    document.forms[0].submit();
                    break;
                case "submit" :
                    document.getElementById("hid").value = "choosestrategy";
                    document.forms[0].submit();
            }
        }
    </script>
</head>
<body>
<form method="post" action="<%=ConstantsClass.IMPORT_SERVLET_ADDRESS%>" role="form" enctype="multipart/form-data">
    <input type="hidden" id="hid" name="<%=ConstantsClass.ACTION%>" value=<%=ConstantsClass.CHOOSE_STRATEGY%>>
    <div align="center">
        <table>
            <tr>
                <td>
                    Import <input type="file" name="<%=ConstantsClass.IMPORT_PARAMETER%>"
                                  class="btn btn-outline-primary"
                                  id="import" accept="text/xml" onchange="importSubmit(this)"/>
                </td>
                <td>
                    <input type="button" class="btn btn-outline-primary" value="Clear"
                           onclick="clearImport()"/>
                </td>
                <td>
                    <input type="button" class="btn btn-outline-primary" id="imp" value="Import data"
                           onclick="buttonClick(this)" disabled/>
                </td>
            </tr>
        </table>
    </div>
    <div class="form-group">
        <div align="center"><strong>JOURNALS</strong></div>
        <table class="table">
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Description</th>
            </tr>
            <x:transform xml="${sessionScope.journalContainer}" xslt="${requestScope.xslJournals}"/>
        </table>
        <div align="center">
            Choose strategy: <br/>
            <table>
                <tr>
                    <td>
                        <select name="<%=ConstantsClass.USERNUMBER%>">
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
            <input type="button" id="submit" class="btn btn-outline-primary" value="OK" onclick="buttonClick(this)"/>
        </div>
    </div>
</form>
</body>
</html>
