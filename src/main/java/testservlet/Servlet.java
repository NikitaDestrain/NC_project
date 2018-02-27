package testservlet;


import auxiliaryclasses.ConstantsClass;
import client.commandprocessor.PasswordEncoder;
import server.controller.UserAuthorizer;
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
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.LinkedList;

@WebServlet(ConstantsClass.SERVLET_ADDRESS)
public class Servlet extends HttpServlet {
    private Connection connection;
    private DataSource dataSource;
    private LinkedList<User> users = new LinkedList<>();
    //private UserAuthorizer authorizer = UserAuthorizer.getInstance();
    //private PasswordEncoder encoder = PasswordEncoder.getInstance();

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
    private static String UPDATE = "UPDATE users SET email=?, password=? WHERE id=?";

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
        String usernumber;
        String login;
        String password;
        String encryptedPassword = null;
        switch (action) {
            case ConstantsClass.SIGN_IN_ACTION:
                useraction = req.getParameter(ConstantsClass.USERACTION);
                switch (useraction) {
                    case ConstantsClass.DO_SIGN_IN:
                        login = req.getParameter(ConstantsClass.LOGIN_PARAMETER);
                        password = req.getParameter(ConstantsClass.PASSWORD_PARAMETER);
//                        try {
//                            encryptedPassword = encoder.encode(password);
//                        } catch (NoSuchAlgorithmException e) {
//                            req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.UNSUCCESSFUL_ACTION);
//                            req.getRequestDispatcher(ConstantsClass.SIGN_IN_ADDRESS).forward(req, resp);
//                        }
//
//                        if (authorizer.isUserDataCorrect(new server.commandproccessor.User(login, encryptedPassword, -1))) {
//                            req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
//                        } else {
//                            req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.UNSUCCESSFUL_SIGN_IN);
//                            req.getRequestDispatcher(ConstantsClass.SIGN_IN_ADDRESS).forward(req, resp);
//                        }

                        if (login.equals("1") && password.equals("1")) {
                            req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                        } else {
                            req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.UNSUCCESSFUL_SIGN_IN);
                            req.getRequestDispatcher(ConstantsClass.SIGN_IN_ADDRESS).forward(req, resp);
                        }
                        break;
                    case ConstantsClass.DO_SIGN_UP:
                        req.getRequestDispatcher(ConstantsClass.SIGN_UP_ADDRESS).forward(req, resp);
                        break;
                }
                break;
            case ConstantsClass.DO_SELECT:
                doUpdateTasksPage(req, resp);
                break;
            case ConstantsClass.DO_SIGN_UP:
//                login = req.getParameter(ConstantsClass.LOGIN_PARAMETER);
//                password = req.getParameter(ConstantsClass.PASSWORD_PARAMETER);
//
//                if (authorizer.isSuchLoginExists(login)) {
//                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.EXIST_LOGIN);
//                    req.getRequestDispatcher(ConstantsClass.SIGN_UP_ADDRESS).forward(req, resp);
//                } else {
//                    try {
//                        encryptedPassword = encoder.encode(password);
//                    } catch (NoSuchAlgorithmException e) {
//                        req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.UNSUCCESSFUL_SIGN_UP);
//                        req.getRequestDispatcher(ConstantsClass.SIGN_UP_ADDRESS).forward(req, resp);
//                    }
//                    authorizer.addUser(new server.commandproccessor.User(login, password, -1));
//                    req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
//                }
                break;
            case ConstantsClass.DO_ADD_TASK:
                useraction = req.getParameter(ConstantsClass.USERACTION);
                switch (useraction) {
                    case ConstantsClass.ADD:
                        resp.getWriter().print("Add");
                        break;
                    case ConstantsClass.CANCEL:
                        doUpdateTasksPage(req, resp);
                        break;
                    case ConstantsClass.DO_BACK_TO_MAIN:
                        req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                        break;
                }
                break;
            case ConstantsClass.DO_EDIT_TASK:
                useraction = req.getParameter(ConstantsClass.USERACTION);
                switch (useraction) {
                    case ConstantsClass.SAVE:
                        resp.getWriter().print("Save");
                        break;
                    case ConstantsClass.CANCEL:
                        doUpdateTasksPage(req, resp);
                        break;
                    case ConstantsClass.DO_BACK_TO_MAIN:
                        req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                        break;
                }
                break;
            case ConstantsClass.DO_CRUD_FROM_TASKS:
                useraction = req.getParameter(ConstantsClass.USERACTION);
                usernumber = req.getParameter(ConstantsClass.USERNUMBER);
                if (useraction.equals(ConstantsClass.ADD)) {
                    req.getRequestDispatcher(ConstantsClass.ADD_TASK_ADDRESS).forward(req, resp);
                } else if (useraction.equals(ConstantsClass.CHANGE_JOURNAL)) {
                    resp.getWriter().print("Change Journal ");
                    resp.getWriter().print(req.getParameter(ConstantsClass.JOURNAL_NAME));
                } else if (useraction.equals(ConstantsClass.DO_BACK_TO_MAIN)) {
                    req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
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
                                doUpdateTasksPage(req, resp);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        req.setAttribute("bean", new SelectResultBean(users));
                        req.setAttribute("message", "Choose task to perform an update/delete action");
                        req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
                    }
                }
                break;
            case ConstantsClass.DO_CRUD_FROM_MAIN:
                useraction = req.getParameter(ConstantsClass.USERACTION);
                usernumber = req.getParameter(ConstantsClass.USERNUMBER);
                if (useraction.equals(ConstantsClass.ADD)) {
                    req.getRequestDispatcher(ConstantsClass.ADD_JOURNAL_ADDRESS).forward(req, resp);
                } else if (useraction.equals(ConstantsClass.DO_BACK_TO_MAIN)) {
                    req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                } else {
                    if (usernumber != null) {
                        //User user = users.get(Integer.parseInt(usernumber));
                        switch (useraction) {
                            case ConstantsClass.UPDATE:
                                //req.setAttribute("user", user);
                                req.getRequestDispatcher(ConstantsClass.EDIT_JOURNAL_ADDRESS).forward(req, resp);
                                break;
                            case ConstantsClass.DELETE:
//                                int id = user.getId();
//                            try {
//                                PreparedStatement statement = connection.prepareStatement("DELETE from cracker.public.users WHERE id = " + id);
//                                statement.executeUpdate();
//                                doUpdateTasksPage(req, resp);
//                            } catch (SQLException e) {
//                                e.printStackTrace();
//                            }
                                resp.getWriter().print("delete");
                                break;
                            case ConstantsClass.CHOOSE:
                                doUpdateTasksPage(req, resp);
                                break;
                        }
                    } else {
                        req.setAttribute("bean", new SelectResultBean(users));
                        req.setAttribute("message", "Choose journal to perform an update/delete action");
                        req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                    }
                }
                break;
            case ConstantsClass.DO_ADD_JOURNAL:
                useraction = req.getParameter(ConstantsClass.USERACTION);
                switch (useraction) {
                    case ConstantsClass.ADD:
                        resp.getWriter().print("Add");
                        break;
                    case ConstantsClass.CANCEL:
                        resp.getWriter().print("Cancel");
                        break;
                    case ConstantsClass.DO_BACK_TO_MAIN:
                        req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                        break;
                }
                break;
            case ConstantsClass.DO_EDIT_JOURNAL:
                useraction = req.getParameter(ConstantsClass.USERACTION);
                switch (useraction) {
                    case ConstantsClass.SAVE:
                        resp.getWriter().print("Save");
                        break;
                    case ConstantsClass.CANCEL:
                        //doUpdateTasksPage(req, resp);
                        resp.getWriter().print("Cancel");
                        break;
                    case ConstantsClass.DO_BACK_TO_MAIN:
                        req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                        break;
                }
                break;
        }
    }

    private void doUpdateTasksPage(HttpServletRequest req, HttpServletResponse resp) {
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
            req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
        } catch (SQLException | IOException | ServletException e) {
            e.printStackTrace();
        }
    }
}
