<%@ page import="auxiliaryclasses.ConstantsClass" %><%--
  Created by IntelliJ IDEA.
  User: ывв
  Date: 07.02.2018
  Time: 13:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sign up</title>
    <style type="text/css">

        #content {
            position: relative;
        }

        .align-right {
            text-align: right;
        }

        table {
            border: 1px solid gray;
            padding: 20px;
            background-color: khaki;
        }

        .message {
            font-size: 16px;
            font-weight: bold;
            font-color: red;
        }
    </style>
    <script type="text/javascript">
        function buttonClick() {
            var login = document.getElementById("login").value;
            var password = document.getElementById("password").value;
            var repeat = document.getElementById("repeat").value;
            if (login.length != 0 && password.length != 0 && repeat.length != 0) {
                if (password.localeCompare(repeat) == 0)
                    document.forms[0].submit();
                else
                    alert("Passwords have to be equal!")
            }
            else
                alert("Fill in all the fields!");
        }
    </script>
</head>
<body>
<div align="center">

    <h3>New Account</h3>

    <form method="post" action=<%=ConstantsClass.SERVLET_ADDRESS%>>

        <input type="hidden" name="<%=ConstantsClass.ACTION%>" value=<%=ConstantsClass.DO_SIGN_UP%>/>

        <table>
            <tr>
                <td class="align-right">Username:</td>
                <td><input type="text" name="<%=ConstantsClass.LOGIN_PARAMETER%>" id="login"
                           value="<%= request.getAttribute(ConstantsClass.LOGIN_PARAMETER)!=null?
                           request.getAttribute(ConstantsClass.LOGIN_PARAMETER):"" %>"/>
                </td>
            </tr>
            <tr>
                <td class="align-right">Password:</td>
                <td><input type="password" name="<%=ConstantsClass.PASSWORD_PARAMETER%>" value="" id="password"/></td>
            </tr>
            <tr>

                <td class="align-right">Repeat password:</td>
                <td><input type="password" name="<%=ConstantsClass.REPEAT_PASSWORD_PARAMETER%>" value="" id="repeat"/></td>
            </tr>
            <tr>
                <td class="align-right" colspan="2"><input type="button" value="Sign up" onclick="buttonClick()"/></td>
            </tr>

        </table>

        <p class="message"><%= request.getAttribute(ConstantsClass.MESSAGE_ATTRIBUTE) != null ?
                request.getAttribute(ConstantsClass.MESSAGE_ATTRIBUTE) : "" %>
        </p>

    </form>

</div>
</body>
</html>
