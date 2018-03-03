package testservlet;


import auxiliaryclasses.ConstantsClass;
import client.commandprocessor.PasswordEncoder;
import server.model.Journal;
import server.model.JournalContainer;
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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@WebServlet(ConstantsClass.SERVLET_ADDRESS)
public class Servlet extends HttpServlet {
    private Connection connection;
    private DataSource dataSource;
    private LinkedList<User> users = new LinkedList<>();
    //private UserAuthorizer authorizer = UserAuthorizer.getInstance();
    private PasswordEncoder encoder = PasswordEncoder.getInstance();
    private JournalContainer container = parseJournalsXML();
    private List<Journal> journals = container.getJournals();

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
        resp.getWriter().print("doGet");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter(ConstantsClass.ACTION);
        switch (action) {
            case ConstantsClass.DO_SIGN_IN:
                doSignIn(req, resp);
                break;
            case ConstantsClass.DO_SIGN_UP:
                doSignUp(req, resp);
                break;
            case ConstantsClass.DO_ADD_TASK:
                doAddTask(req, resp);
                break;
            case ConstantsClass.DO_EDIT_TASK:
                doEditTask(req, resp);
                break;
            case ConstantsClass.DO_CRUD_FROM_TASKS:
                doActionFromTasks(req, resp);
                break;
            case ConstantsClass.DO_CRUD_FROM_MAIN:
                doActionFromMain(req, resp);
                break;
            case ConstantsClass.DO_ADD_JOURNAL:
                doAddJournal(req, resp);
                break;
            case ConstantsClass.DO_EDIT_JOURNAL:
                doEditJournal(req, resp);
                break;
        }
    }

    private JournalContainer parseJournalsXML() {
        JournalContainer container;
        try {
            JAXBContext context = JAXBContext.newInstance(JournalContainer.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            container = (JournalContainer) unmarshaller.unmarshal(new File("C:\\Apps\\weblab\\weblab\\xsd\\journals.xml"));
            journals = container.getJournals();
        } catch (JAXBException e) {
            return null;
        }

        return container;
    }

    private LinkedList<String> getJournalNames() {
        LinkedList<String> names = new LinkedList<>();
        for (Journal j : journals) {
            names.add(j.getName());
        }
        return names;
    }

    /**
     * (yyyy-[m]m-[d]d) hh:mm:ss
     */

    private void doActionFromTasks(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String useraction = req.getParameter(ConstantsClass.USERACTION);
        String usernumber;
        switch (useraction) {
            case ConstantsClass.ADD:
                req.setAttribute(ConstantsClass.JOURNAL_NAMES, getJournalNames());
                req.getRequestDispatcher(ConstantsClass.ADD_TASK_ADDRESS).forward(req, resp);
                break;
            case ConstantsClass.BACK_TO_MAIN:
                req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                break;
            case ConstantsClass.UPDATE:
                usernumber = req.getParameter(ConstantsClass.USERNUMBER);
                if (usernumber != null && !usernumber.equals("")) {
                    int num = Integer.parseInt(usernumber);
                    Journal journal = (Journal) req.getSession().getAttribute(ConstantsClass.JOURNAL_PARAMETER);

                    req.setAttribute(ConstantsClass.NAME, journal.getTask(num).getName());
                    req.setAttribute(ConstantsClass.DESCRIPTION, journal.getTask(num).getDescription());
                    req.setAttribute(ConstantsClass.PLANNED_DATE, journal.getTask(num).getPlannedDate());
                    req.setAttribute(ConstantsClass.NOTIFICATION_DATE, journal.getTask(num).getNotificationDate());
                    req.setAttribute(ConstantsClass.UPLOAD_DATE, journal.getTask(num).getUploadDate());
                    req.setAttribute(ConstantsClass.CHANGE_DATE, journal.getTask(num).getChangeDate());
                    req.setAttribute(ConstantsClass.STATUS, journal.getTask(num).getStatus());

                    req.setAttribute(ConstantsClass.JOURNAL_NAMES, getJournalNames());

                    req.getRequestDispatcher(ConstantsClass.EDIT_TASK_ADDRESS).forward(req, resp);
                } else {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_CHOOSE_JOURNAL);
                    req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                }
                break;
            case ConstantsClass.DELETE:
                usernumber = req.getParameter(ConstantsClass.USERNUMBER);
                if (usernumber != null && !usernumber.equals("")) {
                    int num = Integer.parseInt(usernumber);
                    // todo вызов метода удаления
                } else {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_CHOOSE_JOURNAL);
                    req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                }
        }
    }

    private void doAddTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String useraction = req.getParameter(ConstantsClass.USERACTION);
        switch (useraction) {
            case ConstantsClass.ADD:
                resp.getWriter().print("Add");
                break;
            case ConstantsClass.BACK_TO_MAIN:
                req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                break;
            case ConstantsClass.BACK_TO_TASKS:
                req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
                break;
        }
    }

    private void doEditTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String useraction = req.getParameter(ConstantsClass.USERACTION);
        switch (useraction) {
            case ConstantsClass.SAVE:
                resp.getWriter().print("Save");
                break;
            case ConstantsClass.BACK_TO_MAIN:
                req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                break;
            case ConstantsClass.BACK_TO_TASKS:
                req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
                break;
        }
    }

    private void doSignIn(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String useraction = req.getParameter(ConstantsClass.USERACTION);
        String login;
        String password;
        String encryptedPassword;
        switch (useraction) {
            case ConstantsClass.DO_SIGN_IN:
                login = req.getParameter(ConstantsClass.LOGIN_PARAMETER);
                password = req.getParameter(ConstantsClass.PASSWORD_PARAMETER);
//                        try {
//                            encryptedPassword = encoder.encode(password);
//                        } catch (NoSuchAlgorithmException e) {
//                            req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.UNSUCCESSFUL_ACTION);
//                            req.getRequestDispatcher(ConstantsClass.SIGN_IN_ADDRESS).forward(req, resp);
//                            break;
//                        }
//
//                        if (authorizer.isUserDataCorrect(login, encryptedPassword)) {
//                            req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
//                        } else {
//                            req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.UNSUCCESSFUL_SIGN_IN);
//                            req.getRequestDispatcher(ConstantsClass.SIGN_IN_ADDRESS).forward(req, resp);
//                        }

                if (login.equals("1") && password.equals("1")) {
                    File xmlFile = new File("C:\\Apps\\weblab\\weblab\\xsd\\journals.xml");
                    if (xmlFile != null)
                        req.getSession().setAttribute(ConstantsClass.JOURNAL_CONTAINER_PARAMETER, xmlFile);
                    else
                        req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_XML_READING);

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
    }

    private void doSignUp(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String login = req.getParameter(ConstantsClass.LOGIN_PARAMETER);
//        String password = req.getParameter(ConstantsClass.PASSWORD_PARAMETER);
//        String encryptedPassword;
//
//        if (authorizer.isSuchLoginExists(login)) {
//            req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.EXIST_LOGIN);
//            req.getRequestDispatcher(ConstantsClass.SIGN_UP_ADDRESS).forward(req, resp);
//        } else {
//            try {
//                encryptedPassword = encoder.encode(password);
//            } catch (NoSuchAlgorithmException e) {
//                req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.UNSUCCESSFUL_ACTION);
//                req.getRequestDispatcher(ConstantsClass.SIGN_UP_ADDRESS).forward(req, resp);
//            }
//            authorizer.addUser(login, password);
//
//            JournalContainer container = parseJournalsXML();
//            if (container != null)
//                req.getSession().setAttribute(ConstantsClass.JOURNAL_CONTAINER_PARAMETER, container);
//            else
//                req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_XML_READING);
//
//            req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
//        }
    }

    private void doAddJournal (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String useraction = req.getParameter(ConstantsClass.USERACTION);
        switch (useraction) {
            case ConstantsClass.ADD:
                String name = req.getParameter(ConstantsClass.NAME);
                String description = req.getParameter(ConstantsClass.DESCRIPTION);

                if (name.length() > 18) {
                    req.setAttribute(ConstantsClass.NAME, name);
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_JOURNAL_NAME_LENGTH);
                    req.getRequestDispatcher(ConstantsClass.ADD_JOURNAL_ADDRESS).forward(req, resp);
                } else if (description.length() > 80) {
                    req.setAttribute(ConstantsClass.DESCRIPTION, description);
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_JOURNAL_DESCRIPTION_LENGTH);
                    req.getRequestDispatcher(ConstantsClass.ADD_JOURNAL_ADDRESS).forward(req, resp);
                } else {
                    resp.getWriter().print("Add");
                    // todo вызов метода добавления
                }
                break;
            case ConstantsClass.BACK_TO_MAIN:
                req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                break;
        }
    }

    private void doEditJournal (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String useraction = req.getParameter(ConstantsClass.USERACTION);
        switch (useraction) {
            case ConstantsClass.SAVE:
                String name = req.getParameter(ConstantsClass.NAME);
                String description = req.getParameter(ConstantsClass.DESCRIPTION);

                if (name.length() > 18) {
                    req.setAttribute(ConstantsClass.NAME, name);
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_JOURNAL_NAME_LENGTH);
                    req.getRequestDispatcher(ConstantsClass.EDIT_JOURNAL_ADDRESS).forward(req, resp);
                } else if (description.length() > 80) {
                    req.setAttribute(ConstantsClass.DESCRIPTION, description);
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_JOURNAL_DESCRIPTION_LENGTH);
                    req.getRequestDispatcher(ConstantsClass.EDIT_JOURNAL_ADDRESS).forward(req, resp);
                } else {
                    resp.getWriter().print("Save");
                    // todo вызов метода изменения
                }
                break;
            case ConstantsClass.BACK_TO_MAIN:
                req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                break;
        }
    }

    private void doActionFromMain(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String useraction = req.getParameter(ConstantsClass.USERACTION);
        String usernumber;
        switch (useraction) {
            case ConstantsClass.ADD:
                req.getRequestDispatcher(ConstantsClass.ADD_JOURNAL_ADDRESS).forward(req, resp);
                break;
            case ConstantsClass.UPDATE:
                usernumber = req.getParameter(ConstantsClass.USERNUMBER);
                if (usernumber != null && !usernumber.equals("")) {
                    int num = Integer.parseInt(usernumber);
                    req.setAttribute(ConstantsClass.NAME, journals.get(num).getName());
                    req.setAttribute(ConstantsClass.DESCRIPTION, journals.get(num).getDescription());
                    req.getRequestDispatcher(ConstantsClass.EDIT_JOURNAL_ADDRESS).forward(req, resp);
                } else {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_CHOOSE_JOURNAL);
                    req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                }
                break;
            case ConstantsClass.DELETE:
                usernumber = req.getParameter(ConstantsClass.USERNUMBER);
                if (usernumber != null && !usernumber.equals("")) {
                    int num = Integer.parseInt(usernumber);
                    // todo вызов метода удаления журнала
                } else {
                    req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, ConstantsClass.ERROR_CHOOSE_JOURNAL);
                    req.getRequestDispatcher(ConstantsClass.MAIN_PAGE_ADDRESS).forward(req, resp);
                }
                break;
            case ConstantsClass.CHOOSE:
                usernumber = req.getParameter(ConstantsClass.USERNUMBER);
                Journal journal = journals.get(Integer.parseInt(usernumber));
                req.getSession().setAttribute(ConstantsClass.JOURNAL_PARAMETER, journal);
                req.getRequestDispatcher(ConstantsClass.TASKS_PAGE_ADDRESS).forward(req, resp);
                break;
            case ConstantsClass.SORT:
                String sortColumn = req.getParameter(ConstantsClass.SORT_COLUMN);
                String sortCriteria = req.getParameter(ConstantsClass.SORT_CRITERIA);
                resp.getWriter().print("sort");
                // todo вызов метода для сортировки, распарс нового журнала, отправка на страницу
                break;
        }
    }
}
