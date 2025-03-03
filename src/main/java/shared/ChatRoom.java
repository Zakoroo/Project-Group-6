package shared;

import java.io.Serializable;
import java.util.List;

public record ChatRoom (
    String name, 
    String host, 
    List<Message> history
    
) implements Serializable {
    private static final long serialVersionUID = 42L;
}
