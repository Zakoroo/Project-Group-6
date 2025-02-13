public class Database {
    final private String databaseName;
    
    public Database(String databaseName) {
        this.databaseName = databaseName;
    }

    public String url() {
        return String.format("jdbc:postgresql:%s", databaseName);
    } 
}
