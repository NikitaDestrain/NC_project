<%--
  Created by IntelliJ IDEA.
  User: ывв
  Date: 07.02.2018
  Time: 13:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create user</title>
    <style  type="text/css">

        #content { position: relative;}

        #login {
            position: relative;
            top: 80px;
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

</head>
<body>

<center>

    <div id="login">

        <h3>New Account</h3>

        <form method="get" action="<%= response.encodeURL(request.getContextPath() + "/testoracle?action=createaccount") %>">

            <input type="hidden" name="action" value="createaccount" />

            <table>

                <tr><td class="align-right">Email address: </td><td><input type="text" name="email" value="<%= request.getAttribute("email")!=null?request.getAttribute("email"):"" %>"/></td></tr>
                <tr><td class="align-right">Password: </td><td><input type="password" name="password" value=""/></td></tr>
                <tr><td class="align-right">Repeat password: </td><td><input type="password" name="repeatpassword" value=""/></td></tr>
                <tr><td class="align-right" colspan="2"><input type="submit" value="Log in"/></td></tr>

            </table>

            <p class="message"><%= request.getAttribute("message")!= null? request.getAttribute("message"):"" %></p>

        </form>

    </div>


</center>
</body>
</html>
