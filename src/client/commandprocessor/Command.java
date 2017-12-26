package client.commandprocessor;

import client.model.Journal;
import client.model.Task;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlType(propOrder = {"name", "object"}, name = "Command")
@XmlRootElement(name = "command")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Task.class, Journal.class, User.class})
public class Command implements Serializable {
    @XmlElement (name = "name")
    private String name;
    @XmlElement (name = "object")
    private Object object;

    public Command() {}

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
