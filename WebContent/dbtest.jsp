<%@ page language="java" import="java.sql.*,java.io.*,java.util.*,org.dom4j.*,org.dom4j.io.*" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <title>Database Connection Test</title>
        <style>
            body {
                font-family: sans-serif;
                padding: 20px;
            }

            .success {
                color: green;
                font-weight: bold;
            }

            .error {
                color: red;
                font-weight: bold;
            }

            .info {
                color: gray;
            }
        </style>
    </head>

    <body>
        <h2>Database Connection Diagnostics</h2>
        <hr />
        <% Connection conn=null; try { // 1. Locate Config File String path=request.getRealPath("/")
            + "WEB-INF/classes/config.xml" ; out.println("<p class='info'>Checking config at: " + path + "</p>");

            File f = new File(path);
            if(!f.exists()) {
            out.println("<p class='error'>Config file NOT FOUND!</p>");
            } else {
            // 2. Parse Config
            SAXReader reader = new SAXReader();
            Document document = reader.read(f);
            Element root = document.getRootElement();
            Element db = root.element("databaseconnect");

            String url = db.elementText("url");
            String user = db.elementText("user");
            String pass = db.elementText("password"); // Be careful displaying this in prod
            String driver = db.elementText("driverclass");

            out.println("<ul>");
                out.println("<li>Driver: " + driver + "</li>");
                out.println("<li>URL: " + url + "</li>");
                out.println("<li>User: " + user + "</li>");
                out.println("</ul>");

            // 3. Test Connection
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            if(conn != null) {
            out.println("<p class='success'>Connection Successful!</p>");
            out.println("<p>Database Product: " + conn.getMetaData().getDatabaseProductName() + "</p>");

            // 4. Check Tables
            out.println("<h3>Checking Tables:</h3>
            <ul>");
                Statement stmt = conn.createStatement();
                try {
                ResultSet rs = stmt.executeQuery("SELECT count(*) FROM admin_users");
                if(rs.next()) {
                out.println("<li class='success'>admin_users table exists. Row count: " + rs.getInt(1) + "</li>");
                }
                } catch(Exception e) {
                out.println("<li class='error'>Error querying admin_users: " + e.getMessage() + "</li>");
                }

                try {
                ResultSet rs = stmt.executeQuery("SELECT count(*) FROM auth_device");
                if(rs.next()) {
                out.println("<li class='success'>auth_device table exists. Row count: " + rs.getInt(1) + "</li>");
                }
                } catch(Exception e) {
                out.println("<li class='error'>Error querying auth_device: " + e.getMessage() + "</li>");
                }
                out.println("</ul>");

            } else {
            out.println("<p class='error'>Connection Failed (Result is null)</p>");
            }
            }
            } catch (Exception e) {
            out.println("<div class='error'>");
                out.println("<h3>Exception Occurred:</h3>");
                out.println("
                <pre>");
                e.printStackTrace(new java.io.PrintWriter(out));
                out.println("</pre>");
                out.println("
            </div>");
            } finally {
            if(conn != null) try { conn.close(); } catch(Exception e) {}
            }
            %>
            <br />
            <a href="login.jsp">Back to Login</a>
    </body>

    </html>