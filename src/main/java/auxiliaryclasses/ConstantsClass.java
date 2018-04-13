package auxiliaryclasses;

public class ConstantsClass {
    public static final String ADD = "Add";
    public static final String EDIT = "Edit";
    public static final String DELETE = "Delete";
    public static final String RENAME = "Rename";
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

    public static final String CANCELLED_STATUS = "Cancelled";
    public static final String COMPLETED_STATUS = "Completed";

    public static final String PATH_TO_CONFIG = "data/properties/config.properties";
    public static final String PATH_TO_JOURNAL = "PATH_TO_JOURNAL";
    public static final String MAIN_FORM_ICON = "MAIN_FORM_ICON";
    public static final String NOTIF_SOUND = "NOTIF_SOUND";
    public static final String JOURNAL_XML_FILE = "localbackup/journal.xml";
    public static final String NAMES_XML_FILE = "localbackup/names.xml";
    public static final String NAMES_XSD_FILE = "xsd/names.xsd";
    public static final String JOURNALS_XML_FILE = "localbackup/journals.xml";
    public static final String JOURNAL_XSD_FILE = "xsd/journal.xsd";
    public static final String JOURNALS_XSD_FILE = "xsd/journals.xsd";
    public static final String TASK_XML_FILE = "localbackup/task.xml";
    public static final String TASK_XSD_FILE = "xsd/task.xsd";
    public static final String USER_DATA = "USER_DATA";
    public static final String SCRIPT_FILE = "scripts/databasescript.sql";

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
    public static final String ERROR_XML_WRITING = "Could not write xml file properly!";
    public static final String ERROR_XSD_COMPARING = "Comparing xml with xsd was not successful!";

    public static final String ERROR_CHOOSE_JOURNAL = "Choose a journal to perform an action!";
    public static final String ERROR_CHOOSE_TASK = "Choose a task to perform an action!";

    public static final String ERROR_NAME_LENGTH = "The length of the \"name\" field \n should be between 0 and 18 characters and\nit should contain latin characters, whitespaces and numbers!";
    public static final String ERROR_DESCRIPTION_LENGTH = "The length of the \"description\" field \n should be less than 80 characters and\nit should contain latin characters, whitespaces and numbers!";
    public static final String ERROR_DATE_PARSE = "Enter date in format [d]d-[m]m-yyyy!";
    public static final String ERROR_FILTER_LIKE = "\"Like\" filter should contain latin characters, numbers, \"%\" and \"_\" symbols!";
    public static final String ERROR_FILTER_EQUALS = "\"Equals\" filter should contain latin characters and numbers!";
    public static final String ERROR_AUTH = "Login and password should contain latin characters, whitespaces and numbers!\nAnd login length should not be more than 18 characters!";
    public static final String ERROR_NO_DATA_FOR_THIS_CRITERION = "There was no suitable data for this request!";
    public static final String ERROR_LAZY_MESSAGE = "Error! Try later";

    public static final String CURRENT_USER = "currentuser";

    // web constants

    public static final String REPLACE = "replace";
    public static final String IGNORE = "ignore";
    public static final String EXCEPTION = "exception";

    public static final String IS_ADD = "isadd";
    public static final String IS_EDIT = "isedit";
    public static final String IS_RENAME = "isrename";

    public static final String AUTH_SERVLET_ADDRESS = "/authservlet";
    public static final String JOURNAL_SERVLET_ADDRESS = "/journalservlet";
    public static final String JOURNAL_UPDATE_SERVLET_ADDRESS = "/journalupdateservlet";
    public static final String TASK_SERVLET_ADDRESS = "/taskservlet";
    public static final String TASK_UPDATE_SERVLET_ADDRESS = "/taskupdateservlet";
    public static final String IMPORT_SERVLET_ADDRESS = "/importservlet";
    public static final String DOWNLOAD_SERVLET_ADDRESS = "/download";

    public static final String JOURNAL_PAGE_ADDRESS = "/mainpage";
    public static final String TASKS_PAGE_ADDRESS = "/tasks";
    public static final String SIGN_IN_ADDRESS = "/signin";
    public static final String SIGN_UP_ADDRESS = "/signup";
    public static final String UPDATE_TASKS_ADDRESS = "/updatetasks";
    public static final String UPDATE_JOURNALS_ADDRESS = "/updatejournals";
    public static final String JOURNALS_XSL_ADDRESS = "/journalxsl";
    public static final String TASKS_XSL_ADDRESS = "/taskxsl";

    public static final String DO_SIGN_IN = "signin";
    public static final String SIGN_IN_ACTION = "signinaction";
    public static final String DO_SIGN_UP = "signup";
    public static final String DO_SELECT = "select";
    public static final String DO_ADD_TASK = "addtask";
    public static final String DO_EDIT_TASK = "edittask";
    public static final String DO_RENAME_TASKS = "renametasks";
    public static final String DO_CRUD_FROM_JOURNAL = "crudactionmain";
    public static final String DO_CRUD_FROM_TASKS = "crudactiontasks";
    public static final String DO_ADD_JOURNAL = "addjournal";
    public static final String DO_EDIT_JOURNAL = "editjournal";
    public static final String BACK_TO_MAIN = "backtomain";
    public static final String BACK_TO_TASKS = "backtotasks";

    public static final String CHOOSE_STRATEGY = "choosestrategy";

    public static final String SELECTED = "selected";
    public static final String IMPORT = "imp";
    public static final String EXPORT = "exp";
    public static final String USERACTION = "useraction";
    public static final String USERNUMBER = "usernumber";
    public static final String RENAMENUMBER = "renamenumber";
    public static final String JOURNAL_NAME = "journalname";
    public static final String ACTION = "action";
    public static final String RESCHEDULE_TASK = "rescheduletask";
    public static final String CHANGE_JOURNAL = "changejournal";
    public static final String CHOOSE = "Choose";
    public static final String SORT = "Sort";
    public static final String RELOAD = "reload";

    //SORT/FILTER CONSTANT
    public static final String SORT_COLUMN = "sortcolumn";
    public static final String SORT_CRITERIA = "sortcriteria";
    public static final String SORT_ASC = "ASC";
    public static final String SORT_DESC = "DESC";
    public static final String FILTER_LIKE = "LIKE";
    public static final String FILTER_EQUALS = "equals";

    public static final String STATUS = "Status";
    public static final String PLANNED_DATE = "Planned_date";
    public static final String NOTIFICATION_DATE = "Notification_date";
    public static final String UPLOAD_DATE = "Upload_date";
    public static final String CHANGE_DATE = "Change_date";
    public static final String PREFIX = "Prefix";

    public static final String HIBERNATE_NAME = "name";
    public static final String HIBERNATE_DESCRIPTION = "description";
    public static final String HIBERNATE_STATUS = "stringStatus";
    public static final String HIBERNATE_PLANNED_DATE = "plannedDate";
    public static final String HIBERNATE_NOTIFICATION_DATE = "notificationDate";
    public static final String HIBERNATE_UPLOAD_DATE = "uploadDate";
    public static final String HIBERNATE_CHANGE_DATE = "changeDate";
    public static final String HIBERNATE_JOURNAL_ID = "journalId";

    public static final String JOURNAL_NAMES = "journalNames";
    public static final String CURRENT_TASK = "task";
    public static final String CURRENT_TASK_XML = "taskxml";
    public static final String CURRENT_JOURNAL_NAME = "curname";
    public static final String CURRENT_JOURNAL = "currentjournal";

    public static final String LOGIN_PARAMETER = "login";
    public static final String PASSWORD_PARAMETER = "password";
    public static final String REPEAT_PASSWORD_PARAMETER = "repeatpassword";
    public static final String MESSAGE_ATTRIBUTE = "message";
    public static final String JOURNAL_CONTAINER_PARAMETER = "journalContainer";
    public static final String JOURNAL_PARAMETER = "journal";
    public static final String CURRENT_JOURNAL_ID = "curId";
    public static final String IS_SORTED = "issorted";
    public static final String SHOW_ALL = "allvals";
    public static final String XSL_JOURNAL_CONTAINER_PARAMETER = "xslJournals";
    public static final String XSL_JOURNAL_PARAMETER = "xslJournal";

    public static final String IMPORT_PARAMETER = "import";
    public static final String EXPORT_PARAMETER = "export";

    public static final String NAME = "Name";
    public static final String DESCRIPTION = "Description";

    public static final String SAVE = "Save";
    public static final String OK = "OK";

    public static final int NAME_FIELD_LENGTH = 18;
    public static final int LOGIN_FIELD_LENGTH = 18;
    public static final int DESCRIPTION_FIELD_LENGTH = 80;
    public static final String CURRENT_STATUS = "curstatus";

    public static final String USER_ROLE = "User";
    public static final String MODERATOR_ROLE = "Moderator";
    public static final String ADMINISTRATOR_ROLE = "Administrator";

    // XSL path constants
    public static final String NAMES_XSL = "localbackup/names.xsl";
    public static final String JOURNALS_XSL = "localbackup/journals.xsl";
    public static final String TASKS_XSL = "localbackup/tasks.xsl";

    // XSL parameters

    public static final String XSL_JOURNAL_NAMES_ATTRIBUTE = "xslNames";

    public static final String INCORRECT_FILE_CONTENT = "Incorrect XML-file content! Could not perform import!";
    public static final String JOURNAL_CONTAINER_TAG = "<journalContainer>";
    public static final String JOURNAL_TAG = "<journal>";

    public static final String HOME_DOWNLOADS = System.getProperty("user.home") + "/Downloads/export.xml";

    public static final String JOURNAL_STRATEGY = "journalStrategy";
    public static final String TASK_STRATEGY = "taskStrategy";
}
