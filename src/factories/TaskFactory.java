package factories;


import model.Task;
import model.TaskStatus;

import java.util.Date;

public class TaskFactory  //фабрика Task только она должна явно создавать объект Task

{
   //реализовать тут проверку корректности данных?
    //Так как знаем что задача описыватеся 1 классом ->не абстрактная
   public static Task createTask (String name, TaskStatus status, String description, Date notificationDate, Date plannedDate)
   {
        // todo фаблика должна быть ответственна за конструирование объекта таски. И за проставление у таски ID - тоже
       return new Task(name, status, description, notificationDate, plannedDate);/// назначить ли id тут? или оставить определение id классу Journal

   }
//... еще какие-нибудь перегруженные методы

}
