-- Author: Hussein Hafid
-- Latest modifications: 2025-02-16

CREATE TABLE Users (
    username TEXT PRIMARY KEY,
    nickname TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE DeletedUsers (
    username TEXT PRIMARY KEY,
    nickname TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- PROBLEM: Deleting a user will leave a chatroom hostless
CREATE TABLE ChatRooms (
    chatname TEXT PRIMARY KEY,
    chathost TEXT REFERENCES Users(username) ON DELETE SET NULL
);

CREATE TABLE ChatMembers (
    username TEXT REFERENCES Users(username) ON DELETE CASCADE, 
    chatname TEXT REFERENCES ChatRooms(chatname) ON DELETE CASCADE,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (username, chatname) 
);

CREATE TABLE Messages (
    username TEXT,
    chatname TEXT REFERENCES ChatRooms(chatname),
    type TEXT CHECK (type IN ('text', 'image')),
    textmsg TEXT,
    imageurl TEXT,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (username, chatname, timestamp),
    CHECK(
        (type = 'text' AND textmsg IS NOT NULL AND imageurl IS NULL) OR
        (type = 'image' AND textmsg IS NULL AND imageurl IS NOT NULL)
    )
);
