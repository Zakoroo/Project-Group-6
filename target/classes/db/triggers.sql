-- Author: Hussein Hafid
-- Latest modifications: 2025-02-16

--  Add the host as a member to their chatroom upon creation
CREATE FUNCTION create_chat_func() RETURNS TRIGGER AS
$$
BEGIN
    INSERT INTO ChatMembers (username, chatname) 
    VALUES (NEW.chathost, NEW.chatname)
    ON CONFLICT DO NOTHING;

    RETURN NEW;
END
$$ LANGUAGE plpgsql;

CREATE TRIGGER create_chat_trig
    AFTER INSERT
    ON ChatRooms
    FOR EACH ROW
    EXECUTE PROCEDURE create_chat_func();

-- Move a user from Users to DeletedUsers upon deletion
CREATE FUNCTION move_user_to_deleted_func() RETURNS TRIGGER AS
$$
BEGIN
    INSERT INTO DeletedUsers (username, nickname, email, timestamp) 
    VALUES (OLD.username, OLD.nickname, OLD.email, CURRENT_TIMESTAMP)
    ON CONFLICT DO NOTHING;

    RETURN OLD;
END
$$ LANGUAGE plpgsql;

CREATE TRIGGER move_user_to_deleted_trig
    BEFORE DELETE
    ON Users
    FOR EACH ROW
    EXECUTE PROCEDURE move_user_to_deleted_func();

-- Check the user is not deleted before creating a new account
CREATE FUNCTION check_user_not_delete_func() RETURNS TRIGGER AS
$$
BEGIN
    IF (EXISTS(SELECT 1 FROM DeletedUsers WHERE username = NEW.username)) THEN 
        RAISE EXCEPTION 'User has been deleted';
    END IF;

    RETURN NEW;
END
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_user_not_deleted_trig
    BEFORE INSERT
    ON Users
    FOR EACH ROW
    EXECUTE PROCEDURE check_user_not_delete_func();

-- Check if user is a chat member before allowing them to send messages to a chatroom
CREATE FUNCTION check_user_is_member_func() RETURNS TRIGGER AS
$$
BEGIN
    IF (NOT EXISTS(SELECT 1 FROM ChatMembers WHERE username = NEW.username AND chatname = NEW.chatname)) THEN 
        RAISE EXCEPTION 'User is not a member in the chatroom';
    END IF;

    RETURN NEW;
END
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_user_is_member_trig
    BEFORE INSERT
    ON Messages
    FOR EACH ROW
    EXECUTE PROCEDURE check_user_is_member_func();

-- If a chatroom is empty, delete it
CREATE FUNCTION delete_empty_chatrooms_func() RETURNS TRIGGER AS
$$
BEGIN
    IF (NOT EXISTS(SELECT 1 FROM ChatMembers WHERE chatname = OLD.chatname)) THEN 
        DELETE FROM ChatRooms 
        WHERE chatname = OLD.chatname;
    END IF;

    RETURN OLD;
END
$$ LANGUAGE plpgsql;

CREATE TRIGGER delete_empty_chatrooms_trig
    AFTER DELETE
    ON ChatMembers
    FOR EACH ROW
    EXECUTE PROCEDURE delete_empty_chatrooms_func();

-- Assign a random host to a chatroom if a chatroom become hostless 
CREATE FUNCTION assign_new_host_func() RETURNS TRIGGER AS
$$
DECLARE
    newhost TEXT;
BEGIN
    IF ((SELECT chathost FROM ChatRooms WHERE chatname = OLD.chatname) IS NULL
        OR (SELECT chathost FROM ChatRooms WHERE chatname = OLD.chatname) = OLD.username) THEN 
        SELECT username INTO newhost 
        FROM ChatMembers WHERE chatname = OLD.chatname
        ORDER BY RANDOM()
        LIMIT 1;
        
        UPDATE ChatRooms SET chathost = newhost WHERE chatname = OLD.chatname;
    END IF;

    RETURN OLD;
END
$$ LANGUAGE plpgsql;

CREATE TRIGGER assign_new_host_trig
    AFTER DELETE
    ON ChatMembers
    FOR EACH ROW
    EXECUTE PROCEDURE assign_new_host_func();

-- Notify the server about changes in the table Messages
CREATE OR REPLACE FUNCTION notify_func() RETURNS trigger AS 
$$
BEGIN
    PERFORM pg_notify('update_channel', NEW.chatname);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER notify_trig
AFTER INSERT ON Messages
FOR EACH ROW EXECUTE FUNCTION notify_func();