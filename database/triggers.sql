--  TODO: Set new host to a chatroom after deleting the old host
CREATE FUNCTION delete_user_func() RETURNS TRIGGER AS
$$
BEGIN

END
$$ LANGUAGE plpgsql;

CREATE TRIGGER delete_user_trig
    BEFORE DELETE
    ON Users
    FOR EACH ROW
    EXECUTE PROCEDURE delete_user_func();