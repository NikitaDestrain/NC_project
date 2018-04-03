package server.model;

import java.sql.Date;
import java.util.Calendar;

public class ParameterParser {

    public static TaskStatus parseStatus(String status) {
        switch (status) {
            case "Planned":
                return TaskStatus.Planned;

            case "Overdue":
                return TaskStatus.Overdue;

            case "Rescheduled":
                return TaskStatus.Rescheduled;

            case "Cancelled":
                return TaskStatus.Cancelled;

            case "Completed":
                return TaskStatus.Completed;
        }
        return null;
    }

    public static String parseDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String days = calendar.get(Calendar.DAY_OF_MONTH) + "";
        days = days.length() == 1 ? "0" + days : days;

        String months = (calendar.get(Calendar.MONTH) + 1) + "";
        months = months.length() == 1 ? "0" + months : months;

        return days + "-" + months + "-" + calendar.get(Calendar.YEAR);
    }
}
