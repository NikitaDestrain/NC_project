package testservlet;


import auxiliaryclasses.ConstantsClass;
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

@WebServlet(ConstantsClass.SERVLET_ADDRESS)
public class Servlet extends HttpServlet {
    private Connection connection;
    private DataSource dataSource;
    private LinkedList<User> users = new LinkedList<>();

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

        String action = req.getParameter(ConstantsClass.ACTION);
        Account account = Account.getInstance(connection);
        if (action == null) {
            req.setAttribute("message", "email or password not recognized");
            req.getRequestDispatcher("/login").forward(req, resp);
        } else if (action.equals("toupdatepage")) {

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

                  //  users = fillList();
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
        String action = req.getParameter(ConstantsClass.ACTION);
        String useraction;
        switch (action) {
            case ConstantsClass.DO_SIGN_IN:
                String email = req.getParameter("email");
                String password = req.getParameter("password");

//                req.setAttribute("email", email);
//                req.setAttribute("password", password);

                // обращение к userAuthorizer для проверки данных

                if (email.equals("1") && password.equals("1"))
                    doUpdateMainPage(req, resp);
                break;
            case ConstantsClass.DO_SELECT :
                doUpdateMainPage(req, resp);
                break;
            case ConstantsClass.DO_SIGN_UP :
                String email1 = req.getParameter("email");
                String password1 = req.getParameter("password");
                String repeatPassword1 = req.getParameter("repeatpassword");

                // обращение к контроллеру
//                req.setAttribute("email", email);
//                req.setAttribute("password", "");
//                req.setAttribute("repeatpassword", "");
//                req.setAttribute("message", "");

//                if (!password1.equals(repeatPassword1)) {
//                    // Passwords don't match.
//                    req.setAttribute("message", "Passwords do not match.");
//                    req.getRequestDispatcher("").forward(req, resp);
//                } else {
//                    User user = new User(email, password);
//
////                    if (!user.isValidated()) {
////                        // Password or email address has wrong format.
////                        req.setAttribute("message", user.getMessage());
////                        req.getRequestDispatcher("/signup").forward(req, resp);
////                    } else {
////                        if (account.isSuchUserExists(email)) {
////                            // This email address already exists in the user database.
////                            req.setAttribute("message", "An account with this email address already exists");
////                            req.getRequestDispatcher("/signup").forward(req, resp);
////                        } else {
////                            // We create create the account.
////                            account.createUser(email, password);
////                            users = fillList();
////                            req.setAttribute("bean", new SelectResultBean(users));
////                            //req.setAttribute("message", "user created successfully. log in now");
////                            req.getRequestDispatcher("/mainpage").forward(req, resp);
////                        }
                break;
            case ConstantsClass.DO_ADD_TASK:
                useraction = req.getParameter(ConstantsClass.USERACTION);
                switch (useraction) {
                    case ConstantsClass.ADD :
                        resp.getWriter().print("Save");
                        break;
                    case ConstantsClass.CANCEL:
                        doUpdateMainPage(req, resp);
                        break;
                }
                break;
            case ConstantsClass.DO_EDIT_TASK:
                useraction = req.getParameter(ConstantsClass.USERACTION);
                switch (useraction) {
                    case ConstantsClass.SAVE :
                        resp.getWriter().print("Add");
                        break;
                    case ConstantsClass.CANCEL:
                        doUpdateMainPage(req, resp);
                        break;
                }
                break;
            case ConstantsClass.DO_CRUD_FROM_MAIN:
                useraction = req.getParameter(ConstantsClass.USERACTION);
                String usernumber = req.getParameter(ConstantsClass.USERNUMBER);
                if (useraction.equals(ConstantsClass.ADD)) {
                    req.getRequestDispatcher(ConstantsClass.ADD_TASK_ADDRESS).forward(req, resp);
                } else {
                    if (usernumber != null) {
                        User user = users.get(Integer.parseInt(usernumber));
                        if (useraction.equals(ConstantsClass.UPDATE)) {
                            req.setAttribute("user", user);
                            req.getRequestDispatcher(ConstantsClass.EDIT_TASK_ADDRESS).forward(req, resp);
                        } else if (useraction.equals(ConstantsClass.DELETE)) {
                            int id = user.getId();
                            try {
                                PreparedStatement statement = connection.prepareStatement("DELETE from cracker.public.users WHERE id = " + id);
                                statement.executeUpdate();
                                doUpdateMainPage(req, resp);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        req.setAttribute("bean", new SelectResultBean(users));
                        req.setAttribute("message", "Choose user to perform an action");
                        req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                    }
                }
            break;
            case ConstantsClass.DO_NOTIFICATION_ACTION :
                useraction = req.getParameter(ConstantsClass.USERACTION);
                switch (useraction) {
                    case ConstantsClass.CANCEL:
                        resp.getWriter().print("Cancel");
                        break;
                    case ConstantsClass.FINISH:
                        resp.getWriter().print("Finish");
                        break;
                    case ConstantsClass.OK:
                        resp.getWriter().println("OK");
                        String time = req.getParameter(ConstantsClass.RESCHEDULE_TASK);
                        resp.getWriter().print(time);
                        break;
                }
                break;
        }
    }

    private void doUpdateMainPage(HttpServletRequest req, HttpServletResponse resp) {
        LinkedList<User> users = new LinkedList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(SELECT);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                users.add(new User(set.getInt("id"), set.getString("email"),
                        set.getString("password")));
            }
            this.users = users;
            req.setAttribute("bean", new SelectResultBean(users));
            req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
        } catch (SQLException | IOException | ServletException e) {
            e.printStackTrace();
        }
    }
}
