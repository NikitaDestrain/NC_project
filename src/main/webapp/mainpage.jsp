<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>

<%@ page import="auxiliaryclasses.ConstantsClass" %>
<%@ page import="server.model.Journal" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="server.model.JournalContainer" %>
<%@ page import="java.io.File" %>
<%@ page import="javax.xml.bind.JAXBContext" %>
<%@ page import="javax.xml.bind.Unmarshaller" %>
<%@ page import="javax.xml.bind.JAXBException" %><%--
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
        function filterType() {
            var select = document.getElementById("type");
            var type = select.options[select.selectedIndex].value;
            switch (type) {
                case "" :
                    document.getElementById("liketype").value = "";
                    document.getElementById("equalstype").value = "";
                    document.getElementById("liketype").disabled = true;
                    document.getElementById("equalstype").disabled = true;
                    break;
                case "like":
                    document.getElementById("liketype").disabled = false;
                    document.getElementById("equalstype").value = "";
                    document.getElementById("equalstype").disabled = true;
                    break;
                case "equals":
                    document.getElementById("equalstype").disabled = false;
                    document.getElementById("liketype").value = "";
                    document.getElementById("liketype").disabled = true;
                    break;
            }
        }
        function buttonClick(x) {
            switch (x.id) {
                case "add":
                    document.getElementById("hid").value = "Add";
                    document.forms[0].submit();
                    break;
                case "edit":
                    document.getElementById("hid").value = "Update";
                    document.forms[0].submit();
                    break;
                case "delete":
                    var radios = document.getElementsByName("usernumber");
                    var checked = false;
                    for (var i = 0; i < radios.length; i++) {
                        if (radios[i].checked) {
                            checked = true;
                            break;
                        }
                    }
                    if (checked) {
                        if (confirm("Are you sure want to delete this journal?")) {
                            document.getElementById("hid").value = "Delete";
                            document.forms[0].submit();
                        }
                    }
                    else {
                        alert("Select a journal to perform an action!")
                    }
                    break;
                case "choose" :
                    document.getElementById("hid").value = "Choose";
                    document.forms[0].submit();
                    break;
                case "sort":
                    if (document.getElementById("sortcolumn").value.localeCompare("") == 0 ||
                    document.getElementById("sortcriteria").value.localeCompare("") == 0)
                        alert("Choose column and criterion to perform a sorting!");
                    else {
                        document.getElementById("hid").value = "Sort";
                        document.forms[0].submit();
                    }
                    break;
            }
        }
    </script>
</head>
<body>
<%
    int count = 0;
%>
<div align="center"><strong>TASK SCHEDULER</strong></div>
<div align="center">
    <form method="post" action=<%=ConstantsClass.SERVLET_ADDRESS%>>

        <input type="hidden" id="hid" name=<%=ConstantsClass.USERACTION%>>
        <input type="hidden" name="<%=ConstantsClass.ACTION%>" value=<%=ConstantsClass.DO_CRUD_FROM_MAIN%>>

        <table class="main-table">
            <caption>Journals</caption>
            <tr>
                <th class="count">№</th>
                <th class="main-th">Name</th>
                <th class="main-th">Description</th>
            </tr>
            <x:parse xml="${sessionScope.journalContainer}" var="container"/>
            <x:forEach select="$container/journalContainer/journals/entry" var="journal">
                <tr>
                    <td class="count">
                        <label>
                            <%=count++%>
                            <input type="radio" name="<%=ConstantsClass.USERNUMBER%>"
                                   value="<x:out select="$journal/value/id"/>"/>
                        </label>
                    </td>
                    <td class="main-td">
                        <x:out select="$journal/value/name"/>
                    </td>
                    <td class="main-td">
                        <x:out select="$journal/value/description"/>
                    </td>
                </tr>
            </x:forEach>
        </table>
        <div align="center" class="button-div">
            <table class="button-table">
                <tr>
                    <td class="button-table-td"><input type="button" id="choose" value="Choose journal"
                                                       onclick="buttonClick(this)"></td>
                    <td class="button-table-td"><input type="button" id="add" value="Add journal"
                                                       onclick="buttonClick(this)"></td>
                    <td class="button-table-td"><input type="button" id="edit" value="Edit journal"
                                                       onclick="buttonClick(this)"></td>
                    <td class="button-table-td"><input type="button" id="delete" value="Delete journal"
                                                       onclick="buttonClick(this)"></td>
                </tr>
            </table>
        </div>
        <div align="center">
            <table class="button-table">
                <tr>
                    <td>Sort by:</td>
                    <td>
                        <select name="<%=ConstantsClass.SORT_COLUMN%>" id="<%=ConstantsClass.SORT_COLUMN%>">
                            <option value=""></option>
                            <option value="<%=ConstantsClass.NAME%>">
                                Name
                            </option>
                            <option value="<%=ConstantsClass.DESCRIPTION%>">
                                Description
                            </option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        Criterion:
                    </td>
                    <td>
                        <select name="<%=ConstantsClass.SORT_CRITERIA%>" id="<%=ConstantsClass.SORT_CRITERIA%>">
                            <option value=""></option>
                            <option value="<%=ConstantsClass.SORT_ASC%>">
                                Ascending
                            </option>
                            <option value="<%=ConstantsClass.SORT_DESC%>">
                                Descending
                            </option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        Choose filter:
                    </td>
                    <td>
                        <select id="type" onchange="filterType()">
                            <option value=""></option>
                            <option value="like">
                                Like
                            </option>
                            <option value="equals">
                                Equals
                            </option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>
                        Like:
                    </td>
                    <td>
                        <input type="text" id="liketype" name="<%=ConstantsClass.FILTER_LIKE%>"
                        value="<%=request.getAttribute(ConstantsClass.FILTER_LIKE)==null?"":request.getAttribute(ConstantsClass.FILTER_LIKE)%>"
                               disabled>
                    </td>
                </tr>
                <tr>
                    <td>
                        Equals:
                    </td>
                    <td>
                        <input type="text" id="equalstype" name="<%=ConstantsClass.FILTER_EQUALS%>"
                               value="<%=request.getAttribute(ConstantsClass.FILTER_EQUALS)==null?"":request.getAttribute(ConstantsClass.FILTER_EQUALS)%>"
                               disabled>
                    </td>
                </tr>
                <tr>
                    <td class="button-table-td" colspan="3">
                        <input type="button" id="sort" value="Sort" onclick="buttonClick(this)">
                    </td>
                </tr>
            </table>
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
