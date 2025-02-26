package shared;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

public record Message (
    String username, 
    String type,
    String text,
    byte[] image,
    Timestamp timestamp
) 
implements Serializable {
    private static final long serialVersionUID = 42L;

    public Message(String username, String text, Timestamp timestamp) {
        this(username, "text", text, null, timestamp);
    }

    public Message(String username, byte[] image, Timestamp timestamp) {
        this(username, "image", null, image, timestamp);
    }
}
