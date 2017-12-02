package factories;


import model.Task;
import model.TaskStatus;

import java.util.Date;

public class TaskFactory  

{
   
   
   public static Task createTask (String name, TaskStatus status, String description, Date notificationDate, Date plannedDate)
   {
        // todo фаблика должна быть ответственна за конструирование объекта таски. И за проставление у таски ID - тоже
       return new Task(name, status, description, notificationDate, plannedDate);// id назначается в контсрукторе Task

   }


}
