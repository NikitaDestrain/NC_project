<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>

<%@ page import="auxiliaryclasses.ConstantsClass" %>
<%@ page import="server.model.Journal" %>
<%@ page import="java.util.LinkedList" %><%--
  Created by IntelliJ IDEA.
  User: ывв
  Date: 26.02.2018
  Time: 11:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Journals</title>
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
            text-align: center;
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

        div {
            align: center;
        }

        .center {
            text-align: center;
            font-weight: bold;
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
                case "choose" :
                    document.getElementById("hid").value = "Choose";
                    break;
            }
            document.forms[0].submit();
        }
    </script>
</head>
<body>
<%
    Journal j1 = new Journal("journal1", "j1");
    Journal j2 = new Journal("journal2", "j2");
    LinkedList<Journal> list = new LinkedList<>();
    list.add(j1);
    list.add(j2);
    int count = 0;
%>
<form method="post" action=<%=ConstantsClass.SERVLET_ADDRESS%>>
    <table class="main-table">
        <caption>Journals</caption>
        <tr>
            <th class="count">№</th>
            <th class="main-th">Name</th>
            <th class="main-th">Description</th>
        </tr>
        <%
            int id;
            String email;
            String password;
            for (Journal j : list) {
        %>
        <%--<x:parse xml="$[sessionScope.journals]" var="journals"/>--%>
        <%--<x:forEach select="$journals/"--%>
        <tr>
            <td class="count">
                <label>
                    <%=count%>
                    <input type="radio" name="<%=ConstantsClass.USERNUMBER%>" value="<%=count++%>"/>
                </label>
            </td>
            <td class="main-td"><%=j.getName()%>
            </td>
            <td class="main-td"><%=j.getDescription()%>
            </td>
        </tr>
        <%
            }
        %>
    </table>
    <div align="center" class="button-div">
        <table class="button-table">
            <tr>
                <td class="button-table-td"><input type="button" id="add" value="Add journal" onclick="buttonClick(this)"></td>
                <td class="button-table-td"><input type="button" id="edit" value="Edit journal" onclick="buttonClick(this)"></td>
                <td class="button-table-td"><input type="button" id="delete" value="Delete journal" onclick="buttonClick(this)"></td>
                <td class="button-table-td"><input type="button" id="choose" value="Choose journal" onclick="buttonClick(this)"></td>
            </tr>
        </table>
        <input type="hidden" id="hid" name=<%=ConstantsClass.USERACTION%>>
    </div>
    <div class="center">
        <%=
        request.getParameter(ConstantsClass.MESSAGE_ATTRIBUTE) == null ? "" : request.getParameter(ConstantsClass.MESSAGE_ATTRIBUTE)
        %>
    </div>
    <input type="hidden" name="action" value=<%=ConstantsClass.DO_CRUD_FROM_MAIN%>>
</form>
</body>
</html>
