package shared;

import java.io.*;

public class Container implements Serializable {
    private static final long serialVersionUID = 404;
    
    private String command;
    private Object data; 

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

    public void setCommand(String command) {
        this.command = command;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Container{" +
                "command='" + command + '\'' +
                ", data=" + data +
                '}';
    }

    public Object getMessage() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMessage'");
    }
}
