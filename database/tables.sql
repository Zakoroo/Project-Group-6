CREATE TABLE Users (
    name TEXT PRIMARY KEY,
    email TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL
);

CREATE TABLE ChatRooms (
    name TEXT PRIMARY KEY,
    chathost INT REFERENCES Users
);

CREATE TABLE ChatMembers (
    username INT REFERENCES Users, 
    chatname INT REFERENCES ChatRooms,
    PRIMARY KEY (username, chatname)
);

CREATE TABLE Messages (
    username INT,
    chatname INT,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    type TEXT CHECK (type IN ('text', 'image')),
    textmsg TEXT,
    imageurl TEXT,
    PRIMARY KEY (username, chatname, timestamp),
    FOREIGN KEY (username, chatname) REFERENCES ChatMembers(username, chatname),
    CHECK(
        (type = 'text' AND textmsg IS NOT NULL AND imageurl IS NULL) OR
        (type = 'image' AND textmsg IS NULL AND imageurl IS NOT NULL)
    )
);

