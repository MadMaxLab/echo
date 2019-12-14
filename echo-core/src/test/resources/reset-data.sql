DELETE FROM core.message;
DELETE FROM core.message_type;
DELETE FROM core.contact;
DELETE FROM core."user";

-- Users
INSERT INTO core."user"(user_id, name, login, password) VALUES ('3e87cf37-d92d-4201-9d09-b4330c268bd0', 'Test User 1', 'testlogin1', 'password');
INSERT INTO core."user"(user_id, name, login, password) VALUES ('e852f3ae-d37f-469d-90ac-e7368bd46ceb', 'Test User 2', 'testlogin2', 'password');
INSERT INTO core."user"(user_id, name, login, password) VALUES ('4be47ff1-96f0-41d4-a8c5-c85dde2512c2', 'My User Name', 'mylogin', 'password');
INSERT INTO core."user"(user_id, name, login, password) VALUES ('5c17064a-c020-4e98-88b4-48ee4f0fd112', 'My Accepted Contact', 'acceptedcontact', 'password');
INSERT INTO core."user"(user_id, name, login, password) VALUES ('e4c0afe4-c333-4045-a252-b12779dee047', 'My Request Contact', 'requestcontact', 'password');
INSERT INTO core."user"(user_id, name, login, password) VALUES ('36298bad-e2dc-42ec-b94a-6d424ecdd8a3', 'Request to me Contact', 'requesttomecontact', 'password');

--Contacts
INSERT INTO core.contact(contact_id, initiator_id, accepter_id, accept)VALUES ('7cb4e7a4-d03e-4c7c-9b2b-9b7ba3b635d1', '5c17064a-c020-4e98-88b4-48ee4f0fd112', '4be47ff1-96f0-41d4-a8c5-c85dde2512c2', true);
INSERT INTO core.contact(contact_id, initiator_id, accepter_id, accept) VALUES ('416ae193-ca5b-49fe-a12e-d383790bcefc', '4be47ff1-96f0-41d4-a8c5-c85dde2512c2', 'e4c0afe4-c333-4045-a252-b12779dee047', false);
INSERT INTO core.contact(contact_id, initiator_id, accepter_id, accept) VALUES ('70734d03-1819-41a0-b4c2-cc146ea11d4d', '36298bad-e2dc-42ec-b94a-6d424ecdd8a3', '4be47ff1-96f0-41d4-a8c5-c85dde2512c2', false);

--Message Types
INSERT INTO core.message_type(message_type_id, name) VALUES ('e07f6e4b-29e2-4941-a4e4-ee82cd2d4236', 'Text');

--Message
INSERT INTO core.message(message_id, create_at, is_delivered, text, type, "from", "to")
VALUES ('c2a3dbfe-e4a5-4153-b5f0-332805e5f138', '2019-12-13', true, 'Привет, как дела?', 0, '4be47ff1-96f0-41d4-a8c5-c85dde2512c2', '5c17064a-c020-4e98-88b4-48ee4f0fd112');
INSERT INTO core.message(message_id, create_at, is_delivered, text, type, "from", "to")
VALUES ('1ed2e7d6-7182-4920-8753-90fff6ba8e52', '2019-12-13', false, 'Норм, как сам?', 0, '5c17064a-c020-4e98-88b4-48ee4f0fd112', '4be47ff1-96f0-41d4-a8c5-c85dde2512c2');