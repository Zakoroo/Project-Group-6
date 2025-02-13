import java.sql.Timestamp;

public record Message(
    String userName, 
    String chatName, 
    Timestamp timestamp,
    String type,
    String text_msg,
    String img_url
) {}