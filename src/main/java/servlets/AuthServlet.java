package servlets;

import auxiliaryclasses.ConstantsClass;
import database.postgresql.PostgreSQLDAOManager;
import server.controller.Controller;
import server.controller.PasswordEncoder;
import server.exceptions.ControllerActionException;
import server.exceptions.DAOFactoryActionException;
import server.exportdata.ExportConstants;
import server.exportdata.config.ExportConfigHelper;
import server.exportdata.config.ExportConfigParser;
import server.model.User;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@WebServlet(ConstantsClass.AUTH_SERVLET_ADDRESS)
public class AuthServlet extends HttpServlet {

    private PasswordEncoder encoder;
    private DataUpdateUtil updateUtil;
    private PatternChecker patternChecker;
    private Controller controller;

    private CurrentUserContainer currentUserContainer;
    private User currentUser;


    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            currentUserContainer = CurrentUserContainer.getInstance();
            patternChecker = PatternChecker.getInstance();
            encoder = PasswordEncoder.getInstance();
            PostgreSQLDAOManager.getInstance(config.getServletContext().getRealPath(ConstantsClass.SCRIPT_FILE));
            controller = Controller.getInstance();
            updateUtil = DataUpdateUtil.getInstance();
            ExportConfigParser.getInstance(config.getServletContext().getRealPath(ExportConstants.PATH_TO_PROPERTIES));
            ExportConfigHelper.getInstance();
        } catch (DAOFactoryActionException | ControllerActionException | IOException | NumberFormatException e) {
            throw new ServletException(e.getMessage());
        }
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
                if (!patternChecker.isCorrectLogin(login) || !patternChecker.isCorrectLogin(password) || login.length() > ConstantsClass.LOGIN_FIELD_LENGTH) {
                    incorrectSignIn(req, resp, login, ConstantsClass.ERROR_AUTH);
                } else {
                    try {
                        encryptedPassword = encoder.encode(password);
                    } catch (NoSuchAlgorithmException e) {
                        incorrectSignIn(req, resp, login, ConstantsClass.UNSUCCESSFUL_ACTION);
                        break;
                    }
                    currentUser = controller.signInUser(login, encryptedPassword);
                    if (currentUser != null) {
                        req.getSession().setAttribute(ConstantsClass.CURRENT_USER, currentUser);
                        currentUserContainer.setUser(currentUser);
                        updateUtil.updateJournals(req, resp);
                    } else {
                        incorrectSignIn(req, resp, login, ConstantsClass.UNSUCCESSFUL_SIGN_IN);
                    }
                    break;
                }
            case ConstantsClass.DO_SIGN_UP:
                req.getRequestDispatcher(ConstantsClass.SIGN_UP_ADDRESS).forward(req, resp);
                break;
        }
    }

    private void incorrectSignIn(HttpServletRequest req, HttpServletResponse resp, String login, String message)
            throws ServletException, IOException {
        req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, message);
        req.setAttribute(ConstantsClass.LOGIN_PARAMETER, login);
        req.getRequestDispatcher(ConstantsClass.SIGN_IN_ADDRESS).forward(req, resp);
    }

    private void doSignUp(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter(ConstantsClass.LOGIN_PARAMETER);
        String password = req.getParameter(ConstantsClass.PASSWORD_PARAMETER);
        String encryptedPassword = null;
        if (!patternChecker.isCorrectLogin(login) || !patternChecker.isCorrectLogin(password) || login.length() > ConstantsClass.LOGIN_FIELD_LENGTH) {
            incorrectSignUp(req, resp, login, ConstantsClass.ERROR_AUTH);
        } else {
            try {
                encryptedPassword = encoder.encode(password);
            } catch (NoSuchAlgorithmException e) {
                incorrectSignUp(req, resp, login, ConstantsClass.UNSUCCESSFUL_ACTION);
            }
            try {
                controller.addUser(login, encryptedPassword, ConstantsClass.USER_ROLE);
                currentUser = controller.signInUser(login, encryptedPassword);
                if (currentUser != null) {
                    req.getSession().setAttribute(ConstantsClass.CURRENT_USER, currentUser);
                    currentUserContainer.setUser(currentUser);
                    updateUtil.updateJournals(req, resp);
                } else {
                    incorrectSignUp(req, resp, login, ConstantsClass.UNSUCCESSFUL_SIGN_UP);
                }
            } catch (ControllerActionException e) {
                incorrectSignUp(req, resp, login, e.getMessage());
            }
        }
    }

    private void incorrectSignUp(HttpServletRequest req, HttpServletResponse resp, String login, String message)
            throws ServletException, IOException {
        req.setAttribute(ConstantsClass.MESSAGE_ATTRIBUTE, message);
        req.setAttribute(ConstantsClass.LOGIN_PARAMETER, login);
        req.getRequestDispatcher(ConstantsClass.SIGN_UP_ADDRESS).forward(req, resp);
    }
}
