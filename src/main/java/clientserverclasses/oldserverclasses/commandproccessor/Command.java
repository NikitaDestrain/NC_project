package clientserverclasses.oldserverclasses.commandproccessor;

import server.model.Journal;
import server.model.Task;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * An object that used for transferring a required command via streams of a clientserverclasses.oldclientclasses.client and a server
 */

@XmlType(propOrder = {"name", "object"}, name = "Command")
@XmlRootElement(name = "command")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Task.class, Journal.class, User.class})
public class Command implements Serializable {

    /**
     * Name of the command. The scenario of a processing of a command depends on it
     */

    @XmlElement(name = "name")
    private String name;

    /**
     * Object that is transferred with the Command object and that is used for processing of a command
     */

    @XmlElement(name = "object")
    private Object object;

    public Command() {
    }

    public Command(String name, Object object) {
        this.name = name;
        this.object = object;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Command: " + this.getName() + ", Object: " + object.toString());
        return stringBuffer.toString();
    }
}
