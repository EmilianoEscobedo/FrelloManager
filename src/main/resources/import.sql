INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_SALES');
INSERT INTO roles (name) VALUES ('ROLE_KITCHEN');
INSERT INTO roles (name) VALUES ('ROLE_DELIVERY');

INSERT INTO states (name) VALUES ('COOKING');
INSERT INTO states (name) VALUES ('DELIVERY');
INSERT INTO states (name) VALUES ('DELIVERED');
INSERT INTO states (name) VALUES ('CANCELED');
INSERT INTO states (name) VALUES ('PAYED');

INSERT INTO users (password, username, role_id) VALUES ('$2a$10$0lLmImRyzr/IKHQY8WDO/eX0A7B9bCNLN7DDAdJxLdiXKIYvXE7Ai', 'emi', '1');