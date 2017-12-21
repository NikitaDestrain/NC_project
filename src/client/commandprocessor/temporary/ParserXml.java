package client.commandprocessor.temporary;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import server.factories.TaskFactory;
import server.model.Task;
import server.model.TaskStatus;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



public class ParserXml
{

    public ParserXml() {

    }




    public void readXMLCommand(InputStream in)  throws ParserConfigurationException, IOException, SAXException //парсинг пришедшей xml из указанного потока
    {
        DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();




        // Document document  =  builder.parse(new File("test_xml_delTask.xml"));
        Document document  = builder.parse(in);


        Element commandElement = document.getDocumentElement();
        String commandType= document.getDocumentElement().getAttribute("CommandType");

        if(commandType.equals("AddTask")) {
            ArrayList<String> params = new ArrayList<String >(); //распарвсенные параметры
            NodeList elementTasks = document.getElementsByTagName("Task");
            for (int i = 0; i < elementTasks.getLength(); i++) {
                Node nodeTask = elementTasks.item(i);
                // Если нода не текст, то это Task - заходим внутрь
                if (nodeTask.getNodeType() != Node.TEXT_NODE) {
                    NodeList taskFields = nodeTask.getChildNodes();
                    for (int j = 0; j < taskFields.getLength(); j++) {

                        Node field = taskFields.item(j);
                        // Если нода не текст, то это один из параметров Task
                        if (field.getNodeType() != Node.TEXT_NODE) {
                            //System.out.println(field.getNodeName() + ":" + field.getChildNodes().item(0).getTextContent());
                            params.add(field.getChildNodes().item(0).getTextContent());
                        }
                    }


                }
            }

            addTask(params);

        }


        if(commandType.equals("DellTask"))
        {
            NodeList elementTasks = document.getElementsByTagName("Task");
            int dellId= Integer.parseInt(document.getElementsByTagName("Task").item(0).getAttributes().item(0).getTextContent());
            delTask(dellId);

        }



    }

    private void addTask(ArrayList<String> params) {
        long dat = Calendar.getInstance().getTime().getTime();

        Date notifDate = new Date(Long.parseLong(params.get(3)));
        Date planedDate = new Date((Long.parseLong(params.get(4).toString())));

        TaskStatus ts = TaskStatus.Cancelled; //ничего не значит простот для буфера

        if(params.get(1).equals(TaskStatus.Cancelled.toString()))
        {
            ts=TaskStatus.Cancelled;
        }
        if(params.get(1).equals(TaskStatus.Completed.toString()))
        {
            ts=TaskStatus.Completed;
        }
        if(params.get(1).equals(TaskStatus.Rescheduled.toString()))
        {
            ts=TaskStatus.Rescheduled;
        }
        if(params.get(1).equals(TaskStatus.Overdue.toString()))
        {
            ts=TaskStatus.Overdue;
        }
        if(params.get(1).equals(TaskStatus.Planned.toString()))
        {
            ts=TaskStatus.Planned;
        }
//Создали объект задачи
        Task task = TaskFactory.createTask(params.get(0),ts, params.get(2), notifDate, planedDate );
        System.out.println("Распарсенная Task: "+task);


        //работа с контроллером сервера
    }

    private void delTask(int dellId)
    {
        //удаляем с сервера
    }

   public void creatXMLCommandAddTask(OutputStream out, Task addTask) throws ParserConfigurationException, TransformerException  //используется клиентом для отправки на сервер

   {
    DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
       Document document  =  builder.newDocument();
    Element command = document.createElement("Command");
    command.setAttribute("CommandType", "AddTask");

    Element task = document.createElement("Task");

     Element TaskName = document.createElement("TaskName");
     TaskName.setTextContent(addTask.getName());

       Element StatusTask = document.createElement("TaskStatus");
       StatusTask.setTextContent(addTask.getStatus().toString());

       Element Description = document.createElement("Description");
       Description.setTextContent(addTask.getDescription());

       Element PlannedDate = document.createElement("PlannedDate");
       PlannedDate.setTextContent(Long.toString(addTask.getPlannedDate().getTime()));



       Element NotificationDate = document.createElement("NotificationDate");
       NotificationDate.setTextContent(Long.toString(addTask.getNotificationDate().getTime()));

     task.appendChild(TaskName);
       task.appendChild(StatusTask);
       task.appendChild(Description);
       task.appendChild(PlannedDate);
       task.appendChild(NotificationDate);

       document.appendChild(command);
       command.appendChild(task);



       Transformer t = TransformerFactory.newInstance().newTransformer();
       t.setOutputProperty(OutputKeys.INDENT, "yes");


       t.transform(new DOMSource(document), new StreamResult(out));


   }







}
