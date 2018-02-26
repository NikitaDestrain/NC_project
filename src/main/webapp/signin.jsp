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

    #login {
        position: relative;
        top: 80px;
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
                document.getElementById("hid").value = "signin";
                break;
            case "signup":
                document.getElementById("hid").value = "signup";
                break;
        }
        document.forms[0].submit();
    }
</script>
</head>
<body>

<center>

    <div id="login">

        <h3>Sign In</h3>

        <form method="post" action=<%=ConstantsClass.SERVLET_ADDRESS%>>

            <input type="hidden" name="<%=ConstantsClass.USERACTION%>" id="hid"/>
            <input type="hidden" name="action" value="<%=ConstantsClass.SIGN_IN_ACTION%>">

            <table>

                <tr>
                    <label>
                        <td class="align-right">Username:</td>
                        <td><input type="text" name="email"
                                   value="<%= request.getAttribute("email")!=null?request.getAttribute("email"):"" %>"/>
                        </td>
                    </label>
                </tr>
                <tr>
                    <label>
                        <td class="align-right">Password:</td>
                        <td><input type="password" name="password"
                                   value="<%= request.getAttribute("password")!=null?request.getAttribute("password"):"" %>"/>
                        </td>
                    </label>
                </tr>
                <tr>
                    <td class="align-left"><input type="button" value="Sign in" id="signin"
                                                  onclick="buttonClick(this)"/></td>
                    <td class="align-right" colspan="2"><input type="button" value="Sign up" id="signup"
                                                               onclick="buttonClick(this)"/></td>
                </tr>

            </table>

            <p class="login-error"><%= request.getAttribute("message") != null ? request.getAttribute("message") : "" %>
            </p>

        </form>

    </div>


</center>


</body>
</html>
