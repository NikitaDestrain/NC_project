<%@ page import="auxiliaryclasses.ConstantsClass" %><%--
  Created by IntelliJ IDEA.
  User: ывв
  Date: 26.02.2018
  Time: 12:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit journal</title>
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

        .align-left {
            text-align: center;
            color: #FF0000;
        }

    </style>
    <script type="text/javascript">
        function buttonClick(x) {
            switch (x.id) {
                case "save":
                    document.getElementById("hid").value = "Save";
                    break;
                case "cancel":
                    document.getElementById("hid").value = "Cancel";
                    break;
            }
            document.forms[0].submit();
        }
    </script>
</head>
<body>
<div align="center">
    <form method="post" action=<%=ConstantsClass.SERVLET_ADDRESS%>>
        <input type="hidden" name="action" value="<%=ConstantsClass.DO_EDIT_JOURNAL%>">
        <table>
            <caption>Edit journal</caption>
            <tr>
                <td class="align-right">Name</td>
                <td class="align-right"><input type="text" name="name" value=""></td>
            </tr>
            <tr>
                <td class="align-right">Description</td>
                <td class="align-right"><input type="text" name="description" value=""></td>
            </tr>
            <tr>
                <td><input type="button" id="save" value="Save" onclick="buttonClick(this)"></td>
                <td><input type="button" id="cancel" value="Cancel" onclick="buttonClick(this)"></td>
            </tr>
        </table>
        <input type="hidden" id="hid" name=<%=ConstantsClass.USERACTION%>>
    </form>
</div>
</body>
</html>
