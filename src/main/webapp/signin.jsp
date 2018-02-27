<%@ page import="auxiliaryclasses.ConstantsClass" %><%--
  Created by IntelliJ IDEA.
  User: ывв
  Date: 07.02.2018
  Time: 11:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sign in</title>
</head>
<style type="text/css">

    #content {
        position: relative;
    }

    .align-right {
        text-align: right;
    }

    .align-left {
        text-align: left;
    }

    table {
        border: 1px solid gray;
        padding: 20px;
        background-color: khaki;
    }

    .login-error {
        font-size: 16px;
        font-weight: bold;
        color: red;
    }
</style>
<script type="text/javascript">
    function buttonClick(x) {
        switch (x.id) {
            case "signin":
                if (document.getElementById("login").value.length != 0 && document.getElementById("password").value.length != 0) {
                    document.getElementById("hid").value = "signin";
                    document.forms[0].submit();
                }
                else
                    alert("Enter login and password!");
                break;
            case "signup":
                document.getElementById("hid").value = "signup";
                document.forms[0].submit();
                break;
        }
    }
</script>
</head>
<body>
<div align="center">

    <h3>Sign In</h3>

    <form method="post" action=<%=ConstantsClass.SERVLET_ADDRESS%>>

        <input type="hidden" name="<%=ConstantsClass.USERACTION%>" id="hid"/>
        <input type="hidden" name="action" value="<%=ConstantsClass.SIGN_IN_ACTION%>">

        <table>
            <tr>
                <td class="align-right">Username:</td>
                <td><input type="text" name="login" id="login"
                           value="<%= request.getAttribute(ConstantsClass.LOGIN_PARAMETER)!=null?
                           request.getAttribute(ConstantsClass.LOGIN_PARAMETER):"" %>"/>
                </td>
            </tr>
            <tr>
                <td class="align-right">Password:</td>
                <td><input type="password" name="password" id="password"
                           value="<%= request.getAttribute(ConstantsClass.PASSWORD_PARAMETER)!=null?
                           request.getAttribute(ConstantsClass.PASSWORD_PARAMETER):"" %>"/>
                </td>
            </tr>
            <tr>
                <td class="align-left"><input type="button" value="Sign in" id="signin"
                                              onclick="buttonClick(this)"/></td>
                <td class="align-right" colspan="2"><input type="button" value="Sign up" id="signup"
                                                           onclick="buttonClick(this)"/></td>
            </tr>
        </table>

        <p class="login-error"><%= request.getAttribute(ConstantsClass.MESSAGE_ATTRIBUTE) != null ?
                request.getAttribute(ConstantsClass.MESSAGE_ATTRIBUTE) : "" %>
        </p>
    </form>
</div>
</body>
</html>
