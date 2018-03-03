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
    <title>Add journal</title>
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
                case "back":
                    document.getElementById("hid").value = "backtomain";
                    break;
            }
            document.forms[0].submit();
        }
    </script>
</head>
<body>
<div align="center">
    <form method="post" action=<%=ConstantsClass.SERVLET_ADDRESS%>>
        <input type="hidden" name="action" value="<%=ConstantsClass.DO_ADD_JOURNAL%>">
        <table>
            <caption>Add journal</caption>
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
                           value="<%=request.getAttribute(ConstantsClass.DESCRIPTION)==null?"":
                           request.getAttribute(ConstantsClass.DESCRIPTION)%>">
                </td>
            </tr>
            <tr>
                <td colspan="2"><input type="button" id="add" value="Add" onclick="buttonClick(this)"></td>
            </tr>
        </table>
        <input type="hidden" id="hid" name=<%=ConstantsClass.USERACTION%>>
        <div class="center">
            <input type="button" id="back" value="Back to main page" onclick="buttonClick(this)">
        </div>
        <div align="center">
            <%=
            request.getAttribute(ConstantsClass.MESSAGE_ATTRIBUTE)==null?"":request.getAttribute(ConstantsClass.MESSAGE_ATTRIBUTE)
            %>
        </div>
    </form>
</div>
</body>
</html>
