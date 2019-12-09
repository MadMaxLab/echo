DELETE FROM core.contact;
DELETE from core."user";
INSERT INTO core."user"(user_id, name, login, password) VALUES ('3e87cf37-d92d-4201-9d09-b4330c268bd0', 'Test User 1', 'testlogin1', 'password');
INSERT INTO core."user"(user_id, name, login, password) VALUES ('e852f3ae-d37f-469d-90ac-e7368bd46ceb', 'Test User 2', 'testlogin2', 'password');
INSERT INTO core."user"(user_id, name, login, password) VALUES ('4be47ff1-96f0-41d4-a8c5-c85dde2512c2', '')
INSERT INTO core.contact(initiator_id, contact_id, accept)