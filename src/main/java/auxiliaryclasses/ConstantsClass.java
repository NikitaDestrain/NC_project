package auxiliaryclasses;

public class ConstantsClass {
    public static final String ADD = "Add";
    public static final String EDIT = "Edit";
    public static final String DELETE = "Delete";
    public static final String LATER = "Later";
    public static final String FINISH = "Finish";
    public static final String CANCEL = "Cancel";
    public static final String SIGN_IN = "Sign in";
    public static final String SIGN_UP = "Sign up";
    public static final String DISCONNECT = "Disconnect";

    public static final String UPDATE = "Update";
    public static final String NOTIFICATION = "Notification";
    public static final String UNSUCCESSFUL_SIGN_IN = "Unsuccessful auth";
    public static final String UNSUCCESSFUL_SIGN_UP = "Unsuccessful sign up";
    public static final String SUCCESSFUL_AUTH = "Successful auth";
    public static final String UNSUCCESSFUL_ACTION = "Unsuccessfully";

    public static final int DEFAULT_SERVER_PORT = 1337;
    public static final int SLEEP_FOR_250_SEC = 250;
    public static final int SLEEP_FOR_500_SEC = 500;
    public static final int DEFAULT_MAX_COUNT_CLIENTS = 20;
    public static final int DEFAULT_CURRENT_COUNT_CLIENTS = 0;

    public static final String PATH_TO_JOURNAL = "PATH_TO_JOURNAL";
    public static final String MAIN_FORM_ICON = "MAIN_FORM_ICON";
    public static final String NOTIF_SOUND = "NOTIF_SOUND";
    public static final String XML_FILE = "XML_FILE";
    public static final String USER_DATA = "USER_DATA";

    public static final int SERVER_TREATMENT = 1;
    public static final int NOT_SERVER_TREATMENT = 0;

    public static final String CLIENT_CRASH_MESSAGE = "Something went wrong! Restart application for correct work!";
    public static final String SERVER_IS_NOT_AVAILABLE = "Server is not available! Try later!";
    public static final String UNSUCCESSFUL_CONNECTION = "Unsuccessful connection! Restart application for correct work! Close application?";
    public static final String CRASH_FINISH = "Crash finish! Something went wrong!";
    public static final String UNSAFE_FINISH = "Unsafe finish: server is not available! Data may be lost!";
    public static final String ERROR_SIGN_IN = "Could not send Sign In command! Server is offline. Restart application for correct work!";
    public static final String ERROR_SIGN_UP = "Could not send Sign Up command! Server is offline. Restart application for correct work!";
    public static final String INCORRECT_COMMAND = "Incorrect command!";
    public static final String EXIST_LOGIN = "User with such login already exists!";
    public static final String UNKNOWN_COMMAND = "Error! Unknown command!";
    public static final String ERROR_CLIENT_CONNECTION = "Something went wrong and one of clients has not connected successfully!";
    public static final String ERROR_SERVER_START = "Server could not start! Restart application!";
    public static final String ERROR_PROPERTY = "The configuration file is corrupt or missing!. The application will be closed!";
    public static final String ERROR_XML_READING = "Could not read xml file properly!";

    public static final String ERROR_CHOOSE_JOURNAL = "Choose a journal to perform an action!";
    public static final String ERROR_CHOOSE_TASK = "Choose a task to perform an action!";

    public static final String ERROR_JOURNAL_NAME_LENGTH = "The length of the \"name\" field \n should be less than 18 characters";
    public static final String ERROR_JOURNAL_DESCRIPTION_LENGTH = "The length of the \"description\" field \n should be less than 80 characters";

    // web constants

    public static final String SERVLET_ADDRESS = "/taskscheduler";
    public static final String MAIN_PAGE_ADDRESS = "/mainpage";
    public static final String TASKS_PAGE_ADDRESS = "/tasks";
    public static final String SIGN_IN_ADDRESS = "/signin";
    public static final String SIGN_UP_ADDRESS = "/signup";
    public static final String ADD_TASK_ADDRESS = "/addtask";
    public static final String EDIT_TASK_ADDRESS = "/edittask";
    public static final String ADD_JOURNAL_ADDRESS = "/addjournal";
    public static final String EDIT_JOURNAL_ADDRESS = "/editjournal";

    public static final String DO_SIGN_IN = "signin";
    public static final String SIGN_IN_ACTION = "signinaction";
    public static final String DO_SIGN_UP = "signup";
    public static final String DO_SELECT = "select";
    public static final String DO_ADD_TASK = "addtask";
    public static final String DO_EDIT_TASK = "edittask";
    public static final String DO_CRUD_FROM_MAIN = "crudactionmain";
    public static final String DO_CRUD_FROM_TASKS = "crudactiontasks";
    public static final String DO_ADD_JOURNAL = "addjournal";
    public static final String DO_EDIT_JOURNAL = "editjournal";
    public static final String BACK_TO_MAIN = "backtomain";
    public static final String BACK_TO_TASKS = "backtotasks";

    public static final String USERACTION = "useraction";
    public static final String USERNUMBER = "usernumber";
    public static final String JOURNAL_NAME = "journalname";
    public static final String ACTION = "action";
    public static final String RESCHEDULE_TASK = "rescheduletask";
    public static final String CHANGE_JOURNAL = "changejournal";
    public static final String CHOOSE = "Choose";
    public static final String SORT = "Sort";

    public static final String SORT_COLUMN = "sortcolumn";
    public static final String SORT_CRITERIA = "sortcriteria";
    public static final String SORT_ASC = "asc";
    public static final String SORT_DESC = "desc";
    public static final String STATUS = "status";
    public static final String PLANNED_DATE = "planneddate";
    public static final String NOTIFICATION_DATE = "notificationdate";
    public static final String UPLOAD_DATE = "uploaddate";
    public static final String CHANGE_DATE = "changedate";
    public static final String JOURNAL_NAMES = "journalnames";

    public static final String LOGIN_PARAMETER = "login";
    public static final String PASSWORD_PARAMETER = "password";
    public static final String REPEAT_PASSWORD_PARAMETER = "repeatpassword";
    public static final String MESSAGE_ATTRIBUTE = "message";
    public static final String JOURNAL_CONTAINER_PARAMETER = "journalContainer";
    public static final String JOURNAL_PARAMETER = "journal";

    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";

    public static final String SAVE = "Save";
    public static final String OK = "OK";
}
