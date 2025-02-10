CREATE TABLE Users (
    id SERIAL PRIMARY KEY,
    name TEXT UNIQUE NOT NULL,
    email TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL
);

CREATE TABLE Chat_Rooms (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    chat_host INT REFERENCES Users
);

CREATE TABLE Chat_Members (
    user_id INT REFERENCES Users, 
    chat_id INT REFERENCES Chat_Rooms,
    PRIMARY KEY (user_id, chat_id)
);

CREATE TABLE Messages (
    user_id INT,
    chat_id INT,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    type TEXT CHECK (type IN ('text', 'image')),
    text_msg TEXT,
    image_url TEXT,
    PRIMARY KEY (user_id, chat_id, timestamp),
    FOREIGN KEY (user_id, chat_id) REFERENCES Chat_Members(user_id, chat_id),
    CHECK(
        (type = 'text' AND text_msg IS NOT NULL AND image_url IS NULL) OR
        (type = 'image' AND text_msg IS NULL AND image_url IS NOT NULL)
    )
);

