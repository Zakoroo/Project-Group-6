package shared;

import java.io.Serializable;

public record ChatRoom (
    String name, 
    String host
    
) implements Serializable {
    private static final long serialVersionUID = 42L;
}
