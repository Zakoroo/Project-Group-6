CREATE TABLE Users (
    name TEXT PRIMARY KEY,
    email TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL
);

-- PROBLEM: Deleting a user will leave a chatroom hostless
CREATE TABLE ChatRooms (
    name TEXT PRIMARY KEY,
    chathost TEXT REFERENCES Users(name) ON DELETE SET NULL
);

CREATE TABLE ChatMembers (
    username TEXT REFERENCES Users(name) ON DELETE CASCADE, 
    chatname TEXT REFERENCES ChatRooms(name) ON DELETE CASCADE,
    PRIMARY KEY (username, chatname) 
);

-- PROBLEM: Deleting a user will remove all the messages 
CREATE TABLE Messages (
    username TEXT,
    chatname TEXT,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    type TEXT CHECK (type IN ('text', 'image')),
    textmsg TEXT,
    imageurl TEXT,
    PRIMARY KEY (username, chatname, timestamp),
    FOREIGN KEY (username, chatname) REFERENCES ChatMembers(username, chatname) ON DELETE CASCADE,
    CHECK(
        (type = 'text' AND textmsg IS NOT NULL AND imageurl IS NULL) OR
        (type = 'image' AND textmsg IS NULL AND imageurl IS NOT NULL)
    )
);
