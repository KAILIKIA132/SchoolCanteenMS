<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="s" uri="/struts-tags" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Login - Device Manager</title>
            <link rel="stylesheet" type="text/css" href="css/login.css">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
        </head>

        <body>
            <div class="login-container">
                <img src="images/crawford-logo.jpg" alt="Logo" style="max-width: 150px; margin-bottom: 20px;">
                <h2>Administrator Login</h2>

                <s:if test="hasActionErrors()">
                    <div class="error-message visible">
                        <s:actionerror />
                    </div>
                </s:if>

                <form action="auth.action" method="post">
                    <div class="form-group">
                        <label for="username">Username</label>
                        <input type="text" id="username" name="username" placeholder="Enter username" required
                            autofocus>
                    </div>

                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" placeholder="Enter password" required>
                    </div>

                    <button type="submit" class="btn-login">Login</button>
                    <div class="form-group" style="text-align: center; margin-top: 15px;">
                        <a href="dbtest.jsp" target="_blank"
                            style="color: #666; font-size: 12px; text-decoration: none;">
                            <i class="fa fa-wrench"></i> Check Database Connection
                        </a>
                    </div>
                </form>

                <div class="footer">
                    &copy; 2026 Device Manager System
                </div>
            </div>
        </body>

        </html>