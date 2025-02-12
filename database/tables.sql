CREATE TABLE Users (
    name TEXT PRIMARY KEY,
    email TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL
);

CREATE TABLE ChatRooms (
    name TEXT PRIMARY KEY,
    chathost TEXT REFERENCES Users(name)
);

CREATE TABLE ChatMembers (
    username TEXT REFERENCES Users(name), 
    chatname TEXT REFERENCES ChatRooms(name),
    PRIMARY KEY (username, chatname)
);

CREATE TABLE Messages (
    username TEXT,
    chatname TEXT,
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
