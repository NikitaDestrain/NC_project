package testservlet;


import testservlet.beans.SelectResultBean;
import testservlet.beans.User;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@WebServlet("/testoracle")
public class TestServlet extends HttpServlet {

    private Connection connection;
    private DataSource dataSource;
    private LinkedList<User> users = new LinkedList<User>();

    private static String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static String LOGIN = "postgres";
    private static String PASSWORD = "root";
    private static String CREATE = "CREATE TABLE users(\n" +
            "  id INTEGER  PRIMARY KEY,\n" +
            "  email CHAR(20) NOT NULL,\n" +
            "  password CHAR(20) NOT NULL\n" +
            ");";
    private static String INSERT = "insert into users(id, email, password) values(?,?,?)";
    private static String SELECT = "SELECT * FROM users";
    private static String UPDATE = "update users set email=?, password=? where id=?";

    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            Context context = new InitialContext();
            Context env = (Context) context.lookup("java:comp/env");
            this.dataSource = (DataSource) env.lookup("jdbc/cracker");
            this.connection = dataSource.getConnection();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");
        Account account = Account.getInstance(connection);
        if (action == null) {
            req.setAttribute("message", "email or password not recognized");
            req.getRequestDispatcher("/login").forward(req, resp);
        } else if (action.equals("dologin")) {
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            req.setAttribute("email", email);
            req.setAttribute("password", password);

            if (account.login(email, password)) {
                req.setAttribute("email", email);
                req.getRequestDispatcher("/loginsuccess").forward(req, resp);
            } else {
                req.setAttribute("message", "email or password are not recognized");
                req.getRequestDispatcher("/login").forward(req, resp);
            }
        } else if (action.equals("createtable")) {
            try {
                PreparedStatement statement = connection.prepareStatement(CREATE);
                statement.execute();
                //statement = connection.prepareStatement(AUTO_INCREMENT);
                statement.execute();
                resp.getWriter().print("table created");
                req.getRequestDispatcher("/index").forward(req, resp);
            } catch (SQLException e) {
                e.printStackTrace();
                resp.getWriter().print("problem");
            }
        } else if (action.equals("select")) {
            users = fillList();
            req.setAttribute("bean", new SelectResultBean(users));
            req.getRequestDispatcher("/select").forward(req, resp);
        } else if (action.equals("createaccount")) {
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            String repeatPassword = req.getParameter("repeatpassword");

            req.setAttribute("email", email);
            req.setAttribute("password", "");
            req.setAttribute("repeatpassword", "");
            req.setAttribute("message", "");

            if (!password.equals(repeatPassword)) {
                // Passwords don't match.
                req.setAttribute("message", "Passwords do not match.");
                req.getRequestDispatcher("/createuser").forward(req, resp);
            } else {
                User user = new User(email, password);

                if (!user.isValidated()) {
                    // Password or email address has wrong format.
                    req.setAttribute("message", user.getMessage());
                    req.getRequestDispatcher("/createuser").forward(req, resp);
                } else {
                    if (account.isSuchUserExists(email)) {
                        // This email address already exists in the user database.
                        req.setAttribute("message", "An account with this email address already exists");
                        req.getRequestDispatcher("/createuser").forward(req, resp);
                    } else {
                        // We create create the account.
                        account.createUser(email, password);
                        users = fillList();
                        req.setAttribute("bean", new SelectResultBean(users));
                        //req.setAttribute("message", "user created successfully. log in now");
                        req.getRequestDispatcher("/select").forward(req, resp);
                    }
                }
            }
        } else if (action.equals("toupdatepage")) {
            String useraction = req.getParameter("useraction");
            String usernumber = req.getParameter("usernumber");
            if (useraction.equals("Add")) {
                req.getRequestDispatcher("/createuser").forward(req, resp);
            } else {
                if (usernumber != null) {
                    User user = users.get(Integer.parseInt(usernumber));
                    if (useraction.equals("Update")) {
                        req.setAttribute("user", user);
                        req.getRequestDispatcher("/updatepage").forward(req, resp);
                    } else if (useraction.equals("Delete")) {
                        int id = user.getId();
                        try {
                            PreparedStatement statement = connection.prepareStatement("DELETE from cracker.public.users WHERE id = " + id);
                            statement.executeUpdate();
                            users = fillList();
                            req.setAttribute("bean", new SelectResultBean(users));
                            req.getRequestDispatcher("/select").forward(req, resp);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    req.setAttribute("bean", new SelectResultBean(users));
                    req.setAttribute("message", "Choose user to perform an action");
                    req.getRequestDispatcher("/select").forward(req, resp);
                }
            }
        } else if (action.equals("doupdate")) {
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            String id = req.getParameter("id");
            int userId = Integer.parseInt(id);
            User user = new User(userId, email, password);
            if (user.isValidated()) {
                try {
                    PreparedStatement statement = connection.prepareStatement(UPDATE);
                    statement.setString(1, user.getEmail());
                    statement.setString(2, user.getPassword());
                    statement.setInt(3, user.getId());
                    statement.executeUpdate();

                    users = fillList();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                req.setAttribute("bean", new SelectResultBean(users));
                req.getRequestDispatcher("/select").forward(req, resp);
            } else {
                req.setAttribute("user", user);
                req.getRequestDispatcher("/updatepage").forward(req, resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print("Do post");
    }

    private LinkedList<User> fillList() {
        LinkedList<User> users = new LinkedList<User>();
        try {
            PreparedStatement statement = connection.prepareStatement(SELECT);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                users.add(new User(set.getInt("id"), set.getString("email"),
                        set.getString("password")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return users;
    }
}
