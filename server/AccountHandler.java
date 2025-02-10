public class AccountHandler {
        private Database database;

        public AccountHandler(Database database) {
            this.database = database;
        }

        public void createAccount(String username, String password) {
            String sql = "INSERT INTO users (username, password) VALUES ('" + username + "', '" + password + "');";
            database.query(sql);
            System.out.println("Account created for " + username);
        }

        public void login(String username, String password) {
            String sql = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "';";
            database.query(sql);
            System.out.println("User " + username + " logged in.");
        }
        public void removeAccount(String username) {
            String sql = "DELETE FROM users WHERE username = '" + username + "';";
            database.query(sql);
            System.out.println("User " + username + " removed.");
        }
}