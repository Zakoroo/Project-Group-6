package shared;

import java.io.*;

public class Container implements Serializable {
    private static final long serialVersionUID = 42L;
    
    private String command;
    private Object data; 
    private String id;  // Added correlation ID

    public Container(String command, Object data) {
        this.command = command;
        this.data = data;
    }

    public String getCommand() {
        return command;
    }

    public Object getData() {
        return data;
    }
    
    public String getId() {
        return id;
    }
    
    public void setCommand(String command) {
        this.command = command;
    }

    public void setData(Object data) {
        this.data = data;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Container{" +
                "command='" + command + '\'' +
                ", data=" + data +
                ", id=" + id +
                '}';
    }
}
