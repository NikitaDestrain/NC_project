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
    <title>Add task</title>
    <script language="Javascript" type="text/javascript" src="calendar&time/jquery.1.4.2.js"></script>
    <link type="text/css" href="calendar&time/latest.css" rel="Stylesheet"/>
    <script type="text/javascript" src="calendar&time/latest.js"></script>
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
</head>
<body>
<div align="center">
    <form>
        <table>
            <caption>Add user</caption>
            <tr>
                <td class="align-right">Name</td>
                <td class="align-right"><input type="text" name="name" value=""></td>
            </tr>
            <tr>
                <td class="align-right">Description</td>
                <td class="align-right"><input type="text" name="description" value=""></td>
            </tr>
            <tr>
                <td class="align-right">Planned date & time</td>
                <td class="align-right"><input type="text" name="description" class="datepickerTimeField" value=""></td>
            </tr>
            <tr>
                <td class="align-right">Notification date & time</td>
                <td class="align-right"><input type="text" name="description" class="datepickerTimeField" value=""></td>
            </tr>
            <tr>
                <td class="align-right">Journal name</td>
                <td class="align-right">
                    <select name="journalname" id="journalname">
                        <option value="1">1</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td><input type="submit" value="Add" name="Add"></td>
                <td><input type="submit" value="Cancel" name="Cancel"></td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
