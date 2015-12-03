CREATE TABLE person(
id IDENTITY PRIMARY KEY,
username VARCHAR(255) NOT NULL,
crypted_password VARCHAR(255) NOT NULL,
UNIQUE KEY AK_username (username)
);
--;;
CREATE TABLE room(
id IDENTITY PRIMARY KEY,
roomname VARCHAR(255) NOT NULL,
created_by BIGINT NOT NULL,
UNIQUE KEY AK_roomname (roomname),
FOREIGN KEY (created_by) REFERENCES person(id)
);
