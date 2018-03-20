package clientserverclasses.oldserverclasses.controller;

import server.model.TaskStatus;

public class LifecycleManager {
    private static LifecycleManager ourInstance = new LifecycleManager();

    public static LifecycleManager getInstance() {
        return ourInstance;
    }

    private LifecycleManager() {
    }

    public boolean isStatusConversionValid(TaskStatus oldStatus, TaskStatus newStatus) {
        switch (oldStatus) {
            case Overdue:
                switch (newStatus) {
                    case Planned:
                        return false;
                    case Completed:
                        return false;
                    case Cancelled:
                        return false;
                    case Rescheduled:
                        return true;
                    case Overdue:
                        return true;
                }
                break;
            case Rescheduled:
                switch (newStatus) {
                    case Overdue:
                        return false;
                    case Rescheduled:
                        return true;
                    case Cancelled:
                        return true;
                    case Completed:
                        return true;
                    case Planned:
                        return false;
                }
                break;
            case Planned:
                switch (newStatus) {
                    case Planned:
                        return false;
                    case Completed:
                        return true;
                    case Cancelled:
                        return true;
                    case Rescheduled:
                        return true;
                    case Overdue:
                        return false;
                }
                break;
            case Cancelled:
                return false;
            case Completed:
                return false;
        }
        return false;
    }
}
