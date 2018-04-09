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
        <%@include file="/bootstrap/css/bootstrap.min.css"%>
    </style>
    <script type="text/javascript">
        function buttonClick() {
            var login = document.getElementById("login").value;
            var password = document.getElementById("password").value;
            var repeat = document.getElementById("repeat").value;
            if (login.length != 0 && password.length != 0 && repeat.length != 0) {
                if (password.localeCompare(repeat) == 0) {
                    document.getElementById("hid").value = "signup";
                    document.forms[0].submit();
                }
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
    <h4>New Account</h4>

    <form method="post" action="<%=ConstantsClass.AUTH_SERVLET_ADDRESS%>" role="form">

        <div class="form-group">
            <input type="hidden" name="<%=ConstantsClass.ACTION%>" id="hid"/>

            <table>
                <tr>
                    <td>Username: </td>
                    <td>
                        <input type="text" class="form-control"
                               name="<%=ConstantsClass.LOGIN_PARAMETER%>" id="login"
                               value="<%= request.getAttribute(ConstantsClass.LOGIN_PARAMETER)!=null?
                           request.getAttribute(ConstantsClass.LOGIN_PARAMETER):"" %>"/>
                    </td>
                </tr>
                <tr>
                    <td>Password: </td>
                    <td>
                        <input type="password" class="form-control" name="<%=ConstantsClass.PASSWORD_PARAMETER%>"
                               value="" id="password"/>
                    </td>
                </tr>
                <tr>

                    <td>Repeat password: </td>
                    <td>
                        <input type="password" class="form-control" name="<%=ConstantsClass.REPEAT_PASSWORD_PARAMETER%>"
                               value="" id="repeat"/>
                    </td>
                </tr>
            </table>
            <div align="center">
                <input type="button" class="btn btn-outline-success" value="Sign up"
                       onclick="buttonClick()"/>
            </div>
        </div>
        <div align="center"><%= request.getAttribute(ConstantsClass.MESSAGE_ATTRIBUTE) != null ?
                request.getAttribute(ConstantsClass.MESSAGE_ATTRIBUTE) : "" %>
        </div>
    </form>
</div>
</body>
</html>
