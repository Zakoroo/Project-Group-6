class ChatRoom {
    private String name;

    public ChatRoom(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addUser(User user) {
        System.out.println("User " + user.getUsername() + " joined the chat room: " + name);
    }

    public void removeUser(User user) {
        System.out.println("User " + user.getUsername() + " left the chat room: " + name);
    }
}
