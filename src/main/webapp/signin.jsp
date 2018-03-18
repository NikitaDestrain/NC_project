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
    <%@include file="/bootstrap/css/bootstrap.min.css"%>
</style>
<script type="text/javascript">
    function buttonClick(x) {
        switch (x.id) {
            case "signin":
                if (document.getElementById("login").value.length != 0 &&
                    document.getElementById("password").value.length != 0) {
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
<h4>Sign In</h4>

<form method="post" action="<%=ConstantsClass.AUTH_SERVLET_ADDRESS%>" role="form">
    <div class="form-group">
        <input type="hidden" name="<%=ConstantsClass.USERACTION%>" id="hid"/>
        <input type="hidden" name="<%=ConstantsClass.ACTION%>" value="<%=ConstantsClass.DO_SIGN_IN%>">

        <table>
            <tr>
                <td>Username:</td>
                <td><input type="text" class="form-control" name="login" id="login"
                           value="<%= request.getAttribute(ConstantsClass.LOGIN_PARAMETER)!=null?
                           request.getAttribute(ConstantsClass.LOGIN_PARAMETER):"" %>"/>
                </td>
            </tr>
            <tr>
                <td>Password:</td>
                <td><input type="password" class="form-control" name="password" id="password"
                           value="<%= request.getAttribute(ConstantsClass.PASSWORD_PARAMETER)!=null?
                           request.getAttribute(ConstantsClass.PASSWORD_PARAMETER):"" %>"/>
                </td>
            </tr>
        </table>
        <div align="center">
            <input type="button" class="btn btn-outline-success" value="Sign in" id="signin"
                   onclick="buttonClick(this)"/>
            <input type="button" class="btn btn-outline-primary" value="Sign up"
                   id="signup"
                   onclick="buttonClick(this)"/>
        </div>
    </div>
    <div align="center"><%= request.getAttribute(ConstantsClass.MESSAGE_ATTRIBUTE) != null ?
            request.getAttribute(ConstantsClass.MESSAGE_ATTRIBUTE) : "" %>
    </div>
</form>
</div>
</body>
</html>
