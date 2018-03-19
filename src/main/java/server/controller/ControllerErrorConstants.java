package server.controller;

public class ControllerErrorConstants {

    public static final String ERROR_NAME_EXISTS = "Error! Name already exists.";
    public static final String ERROR_EMPTY_NAME = "Error! Name can not be empty.";
    public static final String ERROR_JOURNAL_NOT_FOUND = "Error! Journal has not been found.";
    public static final String ERROR_LAZY_MESSAGE = "Error! Try later";

    //user
    public static final String ERROR_ADD_USER = "Error! User has not been added. Try later.";
    public static final String ERROR_DELETE_USER = "Error! User has not been deleted. Try later.";
    public static final String ERROR_EDIT_USER = "Error! User has not been edited. Try later.";
    public static final String ERROR_LOGIN_EXISTS = "Error! Login already exists.";
    public static final String ERROR_EDIT_USER_ROLE = "Error! User's role has been not edited. Try later.";

    //date
    public static final String ERROR_PAST_PLANNED_DATE = "Error! Planned date can not be in the past.";
    public static final String ERROR_PAST_NOTIFICATION_DATE = "Error! Notification date can not be in the past.";
    public static final String ERROR_NOTIFICATION_AFTER_PLANNED = "Error! Notification date can not be after planned.";

    //task
    public static final String ERROR_ADD_TASK = "Error! Task has not been added. Try later.";
    public static final String ERROR_DELETE_TASK = "Error! Task has not been deleted. Try later.";
    public static final String ERROR_EDIT_TASK = "Error! Task has not been edited. Try later.";

    //journal
    public static final String ERROR_ADD_JOURNAL = "Error! Journal has not been added. Try later.";
    public static final String ERROR_DELETE_JOURNAL = "Error! Journal has not been deleted. Try later.";
    public static final String ERROR_EDIT_JOURNAL = "Error! Journal has not been edited. Try later.";
}
