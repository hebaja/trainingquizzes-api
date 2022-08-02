INSERT INTO user(email, enabled, password, uid, username) VALUES('hebaja@hebaja.com', true, '$2y$12$gHbpvmLsjanixDzV7JWYaeiJWM.okY2f91daohaVyvKxhUsSwK9fa', '188445677544887521', 'administrator');
INSERT INTO user(email, enabled, password, uid, username, picture_url) VALUES('henrique@hebaja.com', true, '$2y$12$gHbpvmLsjanixDzV7JWYaeiJWM.okY2f91daohaVyvKxhUsSwK9fa', '845751357545687899', 'user', 'https://icons.iconarchive.com/icons/mathijssen/tuxlets/48/Baby-Tux-icon.png');
INSERT INTO user(email, enabled, password, uid, username) VALUES('feanor_esc@hotmail.com', true, '$2y$12$gHbpvmLsjanixDzV7JWYaeiJWM.okY2f91daohaVyvKxhUsSwK9fa', '787454512169654589', 'feanor');
INSERT INTO user(email, enabled, password, uid, username, picture_url) VALUES('student@hebaja.com', true, '$2y$12$gHbpvmLsjanixDzV7JWYaeiJWM.okY2f91daohaVyvKxhUsSwK9fa', '21455666489878466', 'student', 'https://icons.iconarchive.com/icons/femfoyou/angry-birds/48/angry-bird-icon.png');
INSERT INTO user(email, enabled, password, uid, username, picture_url) VALUES('pupil@hebaja.com', true, '$2y$12$gHbpvmLsjanixDzV7JWYaeiJWM.okY2f91daohaVyvKxhUsSwK9fa', '57896544452157896', 'pupil', 'https://icons.iconarchive.com/icons/femfoyou/angry-birds/48/angry-bird-green-icon.png');

INSERT INTO user_roles(user_id, role) values(1, 'ROLE_ADMIN');
INSERT INTO user_roles(user_id, role) values(2, 'ROLE_TEACHER');
INSERT INTO user_roles(user_id, role) values(3, 'ROLE_TEACHER');
INSERT INTO user_roles(user_id, role) values(4, 'ROLE_STUDENT');
INSERT INTO user_roles(user_id, role) values(5, 'ROLE_STUDENT');

--INSERT INTO authority(user_id, role) values(1, 'ROLE_ADMIN');
--INSERT INTO authority(user_id, role) values(2, 'ROLE_TEACHER');
--INSERT INTO authority(user_id, role) values(3, 'ROLE_TEACHER');
--INSERT INTO authority(user_id, role) values(4, 'ROLE_STUDENT');
--INSERT INTO authority(user_id, role) values(5, 'ROLE_STUDENT');

INSERT INTO user_accounts(user_id, account_type) values(1, 'EMAIL');
INSERT INTO user_accounts(user_id, account_type) values(2, 'EMAIL');
INSERT INTO user_accounts(user_id, account_type) values(3, 'EMAIL');
INSERT INTO user_accounts(user_id, account_type) values(4, 'EMAIL');
INSERT INTO user_accounts(user_id, account_type) values(5, 'EMAIL');

INSERT INTO subject(title, user_id, level, creation_date) VALUES('Comparative and superlative', 2, 'EASY', '2021-12-29 20:31:34.0000000');
INSERT INTO subject(title, user_id, level, creation_date) VALUES('Modals', 2, 'MEDIUM', '2022-01-05 20:31:34.0000000');
INSERT INTO subject(title, user_id, level, creation_date) VALUES('Articles and determiners', 2, 'HARD', '2022-02-01 20:31:34.0000000');
INSERT INTO subject(title, user_id, level, creation_date) VALUES('Adverbs', 2, 'MEDIUM', '2022-03-29 20:31:34.0000000');
INSERT INTO subject(title, user_id, level, creation_date) VALUES('Comparative and superlative', 3, 'MEDIUM', '2022-04-19 20:31:34.0000000');

INSERT INTO task (prompt,subject_id,shuffle_options) VALUES
 	('The shops are a lot ___ than usual.',1 ,true),
 	('I feel much ___ since I started exercising.',1 ,true),
	('The girl is ___ than she seemed in her photo.',1, true),
	('The town on Sunday is ___.',1 ,true),
	('I think this sofa is ___ than the other.',1 ,true),
	('The Eiffel Tower is ___ site in France.',1 ,true),
	('She is a ___ musician than her friend.',1 ,true),
	('What is ___ river in the world?',1 ,true),
	('Which gloves are ___ ?',1 ,true),
	('My friend is much ___ than I am.',1 ,true),
	('Who is ___ politician in the Parliment?',1 ,true),
    ('What is ___ painting in this Gallery?',1 ,true),
    ('My car is ___ than yours.',1 ,true),
    ('It was ___ thing.',1 ,true),
    ('It is not ___  it was yesterday.',1 ,true),
    ('He was not  ___ I thought he would be.',1 ,true),
    ('The children were ___ gold.',1 ,true),
    ('The tickets were twice ___  before.',1 ,true),
    ('We climbed ___ up the hill.',1 ,true),
    ('What is the ___ planet from the sun?',1 ,true);

INSERT INTO task (prompt,subject_id,shuffle_options) VALUES
    ('He ___ play tennis very well.',2 ,true),
 	('___ you like another drink?',2 ,true),
 	('If you travel. You ___ take your camera.',2 ,true),
 	('You ___ pass the exam if you don''t study.',2 ,true),
 	('You ___ talk loudly in the library.',2 ,true),
 	('___ I borrow your pen.',2 ,true),
 	('Excuse me. ___ I answer the phone?',2 ,true),
 	('You ___ be over 21 to buy liquor.',2 ,true),
 	('It''s late. This child ___ be in bed.',2 ,true),
 	('It''s late. My parents ___ be sleeping.',2 ,true),
	('I wish I ___ run faster.',2 ,true),
 	('This time tomorrow we ___ be traveling.',2 ,true),
 	('Waiter! ___ I have the menu please?',2 ,true),
 	('You ___ drive so fast here.',2 ,true),
 	('Children ___ drink alcohol.',2 ,true),
 	('I ___ go out tonight. I have to study.',2 ,true),
 	('Children ___ drink alcohol.',2 ,true),
 	('Spain ___ be very hot in the summer.',2 ,true),
 	('Everybody ___ eat less junk food',2 ,true),
 	('You have a cold. You ___ stay home.',2 ,true);

INSERT INTO task (prompt,subject_id,shuffle_options) VALUES
	('Thats the very ___ offer after losing the book.',3 ,true),
	('___ as I hate to do this, we''ll have to cancel the party.',3 ,true),
	('Above ___ her priority is to finish her degree course.',3 ,true),
	('Paul doesn''t have many friends, just a select ___.',3 ,true),
	('___ way, they''re going to close the deal.',3 ,true),
	('Polo is a sport that is associated with ___ rich and powerful.',3 ,true),
	('There was ___ a time when I had to help him.',3 ,true),
	('I''ve had ___ of his complaints.',3 ,true),
	('We had many candidates, but ___ was good for the position.',3 ,true),
	('That country has two official languages, but ___ is English.',3 ,true),
	('They cost 2 dollars ___.',3 ,true),
	('___ argument could move ___ man from this decision.',3 ,true),
	('___ Peter ___ Michael often come here but ___ of them help us.',3 ,true),
	('He gave ___ of us advice about our present goals.',3 ,true),
	('___ players played well; In fact they ___ played quite badly.',3 ,true),
	('There were ___ on the beach, so we weren''t completely alone.',3 ,true),
	('___ them were tired, because ___ them had slept well.',3 ,true),
	('We should have a check up with the dentist ___ six months.',3 ,true);
	
INSERT INTO task (prompt,subject_id,shuffle_options) VALUES
	('Adverbs can modify ___',4, true),
	('An adverb is a word that can tell us more about a noun.',4, true),
	('''We''ll win the game easily.'' Which is the adverb?',4, true),
	('''The boys were really dirty.'' The adverb ''really'' is modifying ___',4, true),
	('Joyfully and slowly are adverbs that tell HOW something was done.',4, true),
	('Sadly, always, tomorrow, and very are NOT adverbs.',4, true),
	('Which sentence is NOT correct.',4, true),
	('Which sentence is correct.',4, true),
	('Which of these sentences does not contain an adverb?',4, true),
	('Which of these statements about adverbs is false?',4, true),
	('Which of these adverbs is an adverb of time?',4, true);
	
INSERT INTO task (prompt,subject_id,shuffle_options) VALUES
 	('The shops are a lot ___ than usual.',5 ,true),
 	('I feel much ___ since I started exercising.',5 ,true),
	('The girl is ___ than she seemed in her photo.',5 , true),
	('The town on Sunday is ___.',5 ,true),
	('I think this sofa is ___ than the other.',5 ,true),
	('The Eiffel Tower is ___ site in France.',5 ,true),
	('She is a ___ musician than her friend.',5 ,true),
	('What is ___ river in the world?',5 ,true),
	('Which gloves are ___ ?',5 ,true),
	('My friend is much ___ than I am.',5 ,true),
	('Who is ___ politician in the Parliment?',5 ,true),
    ('What is ___ painting in this Gallery?',5 ,true),
    ('My car is ___ than yours.',5 ,true),
    ('It was ___ thing.',5 ,true),
    ('It is not ___  it was yesterday.',5 ,true),
    ('He was not  ___ I thought he would be.',5 ,true),
    ('The children were ___ gold.',5 ,true),
    ('The tickets were twice ___  before.',5 ,true),
    ('We climbed ___ up the hill.',5 ,true),
    ('What is the ___ planet from the sun?',5 ,true);	

INSERT INTO task_options (task_id,correct,prompt) VALUES
	 (1,1,'quieter'),
	 (1,0,'more quiet'),
	 (1,0,'more quiet'),
	 (2,0,'fiter'),
	 (2,1,'fitter'),
	 (2,0,'more fit'),
	 (3,0,'more pretty'),
	 (3,0,'most pretty'),
	 (3,1,'prettier'),
	 (4,1,'less crowded'),
	 (4,0,'least crowded'),
	 (4,0,'crowdeder'),
	 (5,0,'comfortabler'),
	 (5,1,'more comfortable'),
	 (5,0,'most comfortable'),
	 (6,0,'the more popular'),
	 (6,0,'popularest'),
	 (6,1,'the most popular'),
	 (7,1,'better'),
	 (7,0,'gooder'),
	 (7,0,'best'),
	 (8,0,'the longer'),
	 (8,1,'the longest'),
	 (8,0,'longer than'),
	 (9,0,'the cheap'),
	 (9,0,'more cheaper'),
	 (9,1,'cheaper'),
	 (10,1,'taller'),
	 (10,0,'most tall'),
	 (10,0,'the tallest'),
	 (11,0,'the badest'),
	 (11,1,'the worst'),
	 (11,0,'worse'),
	 (12,0,'the more beautiful'),
	 (12,0,'the beautifulest'),
	 (12,1,'the most beautiful'),
	 (13,1,'less expensive'),
	 (13,0,'least expensive'),
	 (13,0,'expensiver'),
	 (14,0,'the less surprising'),
	 (14,1,'the least surprising'),
	 (14,0,'the surprisinger'),
	 (15,0,'as hot than'),
	 (15,0,'as hot so'),
	 (15,1,'as hot as'),
	 (16,1,'as tall as'),
	 (16,0,'as tall like'),
	 (16,0,'the tall as'),
	 (17,0,'more good than'),
	 (17,1,'as good as'),
	 (17,0,'less better than'),
	 (18,0,'as expensive like'),
	 (18,0,'more expensiver than'),
	 (18,1,'as expensive as'),
	 (19,1,'further'),
	 (19,0,'farer'),
	 (19,0,'fartest'),
	 (20,0,'farest'),
	 (20,1,'furthest'),
	 (20,0,'fariest');

INSERT INTO task_options (task_id,correct,prompt) VALUES
	 (21,1,'can'),
	 (21,0,'must'),
	 (21,0,'should'),
	 (22,0,'Could'),
	 (22,0,'Should'),
	 (22,1,'Would'),
	 (23,0,'must'),
	 (23,1,'should'),
	 (23,0,'ought to'),
	 (24,1,'won''t'),
	 (24,0,'wouldn''t'),
	 (24,0,'mustn''t'),
	 (25,0,'couldn''t'),
	 (25,1,'mustn''t'),
	 (25,0,'wouldn''t'),
	 (26,0,'Must'),
	 (26,0,'Will'),
	 (26,1,'Shall'),
	 (27,0,'Will'),
	 (27,0,'Would'),
	 (27,1,'May'),
	 (28,1,'must'),
	 (28,0,'may'),
	 (28,0,'should'),
	 (29,1,'ought to'),
	 (29,0,'could'),
	 (29,0,'will'),
	 (30,0,'shall'),
	 (30,1,'might'),
	 (30,0,'would'),
	 (31,1,'could'),
	 (31,0,'would'),
	 (31,0,'ought to'),
	 (32,1,'will'),
	 (32,0,'can''t'),
	 (32,0,'might'),
	 (33,0,'Must'),
	 (33,0,'Should'),
	 (33,1,'Could'),
	 (34,0,'couldn''t'),
	 (34,0,'wouldn''t'),
	 (34,1,'shouldn''t'),
	 (35,1,'mustn''t'),
	 (35,0,'wouldn''t'),
	 (35,0,'won''t'),
	 (36,1,'won''t'),
	 (36,0,'wouldn''t'),
	 (36,0,'couldn''t'),
	 (37,1,'mustn''t'),
	 (37,0,'wouldn''t'),
	 (37,0,'won''t'),
	 (38,0,'could'),
	 (38,1,'can'),
	 (38,0,'should'),
	 (39,1,'should'),
	 (39,0,'can'),
	 (39,0,'might'),
	 (40,0,'may'),
	 (40,0,'could'),
	 (40,1,'should');

INSERT INTO task_options (task_id,correct,prompt) VALUES
	 (41,1,'least she could'),
	 (41,0,'last she could'),
	 (41,0,'leess she could'),
	 (42,0,'more'),
	 (42,1,'much'),
	 (42,0,'such'),
	 (43,0,'any'),
	 (43,0,'every'),
	 (43,1,'all'),
	 (44,1,'few'),
	 (44,0,'little'),
	 (44,0,'half'),
	 (45,0,'Both'),
	 (45,1,'Either'),
	 (45,0,'Each'),
	 (46,0,'some'),
	 (46,0,'0'),
	 (46,1,'the'),
	 (47,1,'many'),
	 (47,0,'more than'),
	 (47,0,'much'),
	 (48,0,'every'),
	 (48,1,'enough'),
	 (48,0,'other'),
	 (49,0,'much of them'),
	 (49,0,'neither of them'),
	 (49,1,'none of them'),
	 (50,1,'neither of them'),
	 (50,0,'none of them'),
	 (50,0,'both of them'),
	 (51,0,'either'),
	 (51,1,'each'),
	 (51,0,'every'),
	 (52,0,'Each / all'),
	 (52,0,'No / neither'),
	 (52,1,'No / either '),
	 (53,1,'Both / and / neither '),
	 (53,0,'Both / and / either'),
	 (53,0,'Neither / nor / both'),
	 (54,0,'every'),
	 (54,1,'each'),
	 (54,0,'much'),
	 (55,0,'neither of / both'),
	 (55,0,'neither of the / all'),
	 (55,1,'none of the / all'),
	 (56,1,'a few'),
	 (56,0,'fewer'),
	 (56,0,'fewest'),
	 (57,0,'None of / either of'),
	 (57,1,'Both of / neither of'),
	 (57,0,'Either of / none of'),
	 (58,0,'all'),
	 (58,0,'each'),
	 (58,1,'every');
	 
INSERT INTO task_options (task_id,prompt,correct) VALUES
	 (59,'adjectives and other adverbs.',1),
	 (59,'pronouns and other nouns.',0),
	 (60,'true',0),
	 (60,'false',1),
	 (61,'will',0),
	 (61,'win',0),
	 (61,'game',0),
	 (61,'easily',1),
	 (62,'an adjective',1),
	 (62,'a verb',0),
	 (62,'a noun',0),
	 (62,'an article',0),
	 (63,'true',1),
	 (63,'false',0),
	 (64,'true',0),
	 (64,'false',1),
	 (65,'They ran quickly.',0),
	 (65,'The boys slept heavily.',0),
	 (65,'She dances well.',0),
	 (65,'He runs fast.',0),
	 (65,'We worked hardly.',1),
	 (66,'She talks friendly.',0),
	 (66,'I am quietly.',0),
	 (66,'That smells well.',0),
	 (66,'You sang that song nicely.',1),
	 (66,'This sounds greatly.',0),
	 (67,'The child ran happily towards his mother.',0),
	 (67,'Lisa walked to the shops.',1),
	 (67,'That smells well.',0),
	 (67,'Brendan gently woke the sleeping baby.',0),
	 (68,'We use an adverb to say how something happens.',0),
	 (68,'We use an adverb to say how often something happens.',0),
	 (68,'We use an adverb to say when or where something happens.',0),
	 (68,'We use an adverb in place of a noun.',1),
	 (69,'yesterday',1),
	 (69,'slowly',0),
	 (69,'loudly',0),
	 (69,'upstairs',0),
	 (69,'warmly',0);
	
INSERT INTO task_options (task_id,correct,prompt) VALUES
	(70, 1,	'quieter'),
	(70, 0,	'more quiet'),
	(70, 0,	'more quiet'),
	(71, 0,	'fiter'),
	(71, 1,	'fitter'),
	(71, 0,	'more fit'),
	(72, 0,	'more pretty'),
	(72, 0,	'most pretty'),
	(72, 1,	'prettier'),
	(73, 1,	'less crowded'),
	(73, 0,	'least crowded'),
	(73, 0,	'crowdeder'),
	(74, 0,	'comfortabler'),
	(74, 1,	'more comfortable'),
	(74, 0,	'most comfortable'),
	(75, 0,	'the more popular'),
	(75, 0,	'popularest'),
	(75, 1,	'the most popular'),
	(76, 1,	'better'),
	(76, 0,	'gooder'),
	(76, 0,	'best'),
	(77, 0,	'the longer'),
	(77, 1,	'the longest'),
	(77, 0,	'longer than'),
	(78, 0,	'the cheap'),
	(78, 0,	'more cheaper'),
	(78, 1,	'cheaper'),
	(79, 1,	'taller'),
	(79, 0,	'most tall'),
	(79, 0,	'the tallest'),
	(80, 0,	'the badest'),
	(80, 1,	'the worst'),
	(80, 0,	'worse'),
	(81, 0,	'the more beautiful'),
	(81, 0,	'the beautifulest'),
	(81, 1,	'the most beautiful'),
	(82, 1,	'less expensive'),
	(82, 0,	'least expensive'),
	(82, 0,	'expensiver'),
	(83, 0,	'the less surprising'),
	(83, 1,	'the least surprising'),
	(83, 0,	'the surprisinger'),
	(84, 0,	'as hot than'),
	(84, 0,	'as hot so'),
	(84, 1,	'as hot as'),
	(85, 1,	'as tall as'),
	(85, 0,	'as tall like'),
	(85, 0,	'the tall as'),
	(86, 0,	'more good than'),
	(86, 1,	'as good as'),
	(86, 0,	'less better than'),
	(87, 0,	'as expensive like'),
	(87, 0,	'more expensiver than'),
	(87, 1,	'as expensive as'),
	(88, 1,	'further'),
	(88, 0,	'farer'),
	(88, 0,	'fartest'),
	(89, 0,	'farest'),
	(89, 1,	'furthest'),
	(89, 0,	'fariest');
	 
INSERT INTO exercise (`level`,score,user_id,subject_id) VALUES
	 ('EASY',8.0,4,1),
	 ('EASY',6.0,4,1),
	 ('EASY',4.0,4,1),
	 ('EASY',3.0,4,1),
	 ('EASY',2.0,4,1),
	 ('EASY',8.0,4,1),
	 ('EASY',5.0,4,1),
	 ('EASY',4.0,4,1),
	 ('EASY',2.0,4,1),
	 ('MEDIUM',6.0,4,2),
	 ('MEDIUM',3.0,4,2),
	 ('HARD',3.0,4,3),
	 ('MEDIUM',9.0,4,4);
	 
INSERT INTO subject_tasks(subject_id, tasks_id) values(1, 1);
INSERT INTO subject_tasks(subject_id, tasks_id) values(1, 2);
INSERT INTO subject_tasks(subject_id, tasks_id) values(1, 3);
INSERT INTO subject_tasks(subject_id, tasks_id) values(1, 4);
INSERT INTO subject_tasks(subject_id, tasks_id) values(1, 5);
INSERT INTO subject_tasks(subject_id, tasks_id) values(1, 6);
INSERT INTO subject_tasks(subject_id, tasks_id) values(1, 7);
INSERT INTO subject_tasks(subject_id, tasks_id) values(1, 8);
INSERT INTO subject_tasks(subject_id, tasks_id) values(1, 9);
INSERT INTO subject_tasks(subject_id, tasks_id) values(1, 10);
INSERT INTO subject_tasks(subject_id, tasks_id) values(1, 11);
INSERT INTO subject_tasks(subject_id, tasks_id) values(1, 12);
INSERT INTO subject_tasks(subject_id, tasks_id) values(1, 13);
INSERT INTO subject_tasks(subject_id, tasks_id) values(1, 14);
INSERT INTO subject_tasks(subject_id, tasks_id) values(1, 15);
INSERT INTO subject_tasks(subject_id, tasks_id) values(1, 16);
INSERT INTO subject_tasks(subject_id, tasks_id) values(1, 17);
INSERT INTO subject_tasks(subject_id, tasks_id) values(1, 18);
INSERT INTO subject_tasks(subject_id, tasks_id) values(1, 19);
INSERT INTO subject_tasks(subject_id, tasks_id) values(1, 20);

INSERT INTO subject_tasks(subject_id, tasks_id) values(2, 21);
INSERT INTO subject_tasks(subject_id, tasks_id) values(2, 22);
INSERT INTO subject_tasks(subject_id, tasks_id) values(2, 23);
INSERT INTO subject_tasks(subject_id, tasks_id) values(2, 24);
INSERT INTO subject_tasks(subject_id, tasks_id) values(2, 25);
INSERT INTO subject_tasks(subject_id, tasks_id) values(2, 26);
INSERT INTO subject_tasks(subject_id, tasks_id) values(2, 27);
INSERT INTO subject_tasks(subject_id, tasks_id) values(2, 28);
INSERT INTO subject_tasks(subject_id, tasks_id) values(2, 29);
INSERT INTO subject_tasks(subject_id, tasks_id) values(2, 30);
INSERT INTO subject_tasks(subject_id, tasks_id) values(2, 31);
INSERT INTO subject_tasks(subject_id, tasks_id) values(2, 32);
INSERT INTO subject_tasks(subject_id, tasks_id) values(2, 33);
INSERT INTO subject_tasks(subject_id, tasks_id) values(2, 34);
INSERT INTO subject_tasks(subject_id, tasks_id) values(2, 35);
INSERT INTO subject_tasks(subject_id, tasks_id) values(2, 36);
INSERT INTO subject_tasks(subject_id, tasks_id) values(2, 37);
INSERT INTO subject_tasks(subject_id, tasks_id) values(2, 38);
INSERT INTO subject_tasks(subject_id, tasks_id) values(2, 39);
INSERT INTO subject_tasks(subject_id, tasks_id) values(2, 40);

INSERT INTO subject_tasks(subject_id, tasks_id) values(3, 41);
INSERT INTO subject_tasks(subject_id, tasks_id) values(3, 42);
INSERT INTO subject_tasks(subject_id, tasks_id) values(3, 43);
INSERT INTO subject_tasks(subject_id, tasks_id) values(3, 44);
INSERT INTO subject_tasks(subject_id, tasks_id) values(3, 45);
INSERT INTO subject_tasks(subject_id, tasks_id) values(3, 46);
INSERT INTO subject_tasks(subject_id, tasks_id) values(3, 47);
INSERT INTO subject_tasks(subject_id, tasks_id) values(3, 48);
INSERT INTO subject_tasks(subject_id, tasks_id) values(3, 49);
INSERT INTO subject_tasks(subject_id, tasks_id) values(3, 50);
INSERT INTO subject_tasks(subject_id, tasks_id) values(3, 51);
INSERT INTO subject_tasks(subject_id, tasks_id) values(3, 52);
INSERT INTO subject_tasks(subject_id, tasks_id) values(3, 53);
INSERT INTO subject_tasks(subject_id, tasks_id) values(3, 54);
INSERT INTO subject_tasks(subject_id, tasks_id) values(3, 55);
INSERT INTO subject_tasks(subject_id, tasks_id) values(3, 56);
INSERT INTO subject_tasks(subject_id, tasks_id) values(3, 57);
INSERT INTO subject_tasks(subject_id, tasks_id) values(3, 58);

INSERT INTO subject_tasks(subject_id, tasks_id) values(4, 59);
INSERT INTO subject_tasks(subject_id, tasks_id) values(4, 60);
INSERT INTO subject_tasks(subject_id, tasks_id) values(4, 61);
INSERT INTO subject_tasks(subject_id, tasks_id) values(4, 62);
INSERT INTO subject_tasks(subject_id, tasks_id) values(4, 63);
INSERT INTO subject_tasks(subject_id, tasks_id) values(4, 64);
INSERT INTO subject_tasks(subject_id, tasks_id) values(4, 65);
INSERT INTO subject_tasks(subject_id, tasks_id) values(4, 66);
INSERT INTO subject_tasks(subject_id, tasks_id) values(4, 67);
INSERT INTO subject_tasks(subject_id, tasks_id) values(4, 68);
INSERT INTO subject_tasks(subject_id, tasks_id) values(4, 69);

INSERT INTO subject_tasks(subject_id, tasks_id) values(5, 70);
INSERT INTO subject_tasks(subject_id, tasks_id) values(5, 71);
INSERT INTO subject_tasks(subject_id, tasks_id) values(5, 72);
INSERT INTO subject_tasks(subject_id, tasks_id) values(5, 73);
INSERT INTO subject_tasks(subject_id, tasks_id) values(5, 74);
INSERT INTO subject_tasks(subject_id, tasks_id) values(5, 75);
INSERT INTO subject_tasks(subject_id, tasks_id) values(5, 76);
INSERT INTO subject_tasks(subject_id, tasks_id) values(5, 77);
INSERT INTO subject_tasks(subject_id, tasks_id) values(5, 78);
INSERT INTO subject_tasks(subject_id, tasks_id) values(5, 79);
INSERT INTO subject_tasks(subject_id, tasks_id) values(5, 80);
INSERT INTO subject_tasks(subject_id, tasks_id) values(5, 81);
INSERT INTO subject_tasks(subject_id, tasks_id) values(5, 82);
INSERT INTO subject_tasks(subject_id, tasks_id) values(5, 83);
INSERT INTO subject_tasks(subject_id, tasks_id) values(5, 84);
INSERT INTO subject_tasks(subject_id, tasks_id) values(5, 85);
INSERT INTO subject_tasks(subject_id, tasks_id) values(5, 86);
INSERT INTO subject_tasks(subject_id, tasks_id) values(5, 87);
INSERT INTO subject_tasks(subject_id, tasks_id) values(5, 88);
INSERT INTO subject_tasks(subject_id, tasks_id) values(5, 89);
		 
-- ##### Ordinary quest ############## --	 
INSERT INTO quest (user_id, title, subject_id, start_date, finish_date, time_interval, time_unit, finished) values (2, 'test_quest', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + 4, 2, 7, false);
--INSERT INTO quest_subscribed_users (quest_id, subscribed_users_id) values (1, 4);
--INSERT INTO quest_subscribed_users (quest_id, subscribed_users_id) values (1, 5);
INSERT INTO quest_subscribed_users_ids (quest_id, subscribed_users_ids) values (1, 4);
INSERT INTO quest_subscribed_users_ids (quest_id, subscribed_users_ids) values (1, 5);

INSERT INTO trial (quest_id, subscribed_user_id, start_date, finish_date, trial_number, finished) values (1, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + 1.9999, 1, false);
INSERT INTO trial (quest_id, subscribed_user_id, start_date, finish_date, trial_number, finished) values (1, 4, CURRENT_TIMESTAMP + 2, CURRENT_TIMESTAMP + 3.9999, 2, false);
INSERT INTO trial (quest_id, subscribed_user_id, start_date, finish_date, trial_number, finished) values (1, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + 1.9999, 1, false);
INSERT INTO trial (quest_id, subscribed_user_id, start_date, finish_date, trial_number, finished) values (1, 5, CURRENT_TIMESTAMP + 2, CURRENT_TIMESTAMP + 3.9999, 2, false);

--INSERT INTO quest_trials (quest_id, trials_id) values (1, 1);
--INSERT INTO quest_trials (quest_id, trials_id) values (1, 2);
--INSERT INTO quest_trials (quest_id, trials_id) values (1, 3);
--INSERT INTO quest_trials (quest_id, trials_id) values (1, 4);

-- ##### Quest with expired trial ############## --
INSERT INTO quest (user_id, title, subject_id, start_date, finish_date, time_interval, time_unit, finished) values (2, 'quest_one_expired', 1, CURRENT_TIMESTAMP - 1, CURRENT_TIMESTAMP + 2, 3, 7, false);
--INSERT INTO quest_subscribed_users (quest_id, subscribed_users_id) values (2, 4);
--INSERT INTO quest_subscribed_users (quest_id, subscribed_users_id) values (2, 5);
INSERT INTO quest_subscribed_users_ids (quest_id, subscribed_users_ids) values (2, 4);
INSERT INTO quest_subscribed_users_ids (quest_id, subscribed_users_ids) values (2, 5);

INSERT INTO trial (quest_id, subscribed_user_id, start_date, finish_date, trial_number, finished) values (2, 4, CURRENT_TIMESTAMP - 1, CURRENT_TIMESTAMP, 1, false);
INSERT INTO trial (quest_id, subscribed_user_id, start_date, finish_date, trial_number, finished) values (2, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + 1, 2, false);
INSERT INTO trial (quest_id, subscribed_user_id, start_date, finish_date, trial_number, finished) values (2, 4, CURRENT_TIMESTAMP + 1, CURRENT_TIMESTAMP + 2, 3, false);
INSERT INTO trial (quest_id, subscribed_user_id, start_date, finish_date, trial_number, finished) values (2, 5, CURRENT_TIMESTAMP - 1, CURRENT_TIMESTAMP, 1, false);
INSERT INTO trial (quest_id, subscribed_user_id, start_date, finish_date, trial_number, finished) values (2, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + 1, 2, false);
INSERT INTO trial (quest_id, subscribed_user_id, start_date, finish_date, trial_number, finished) values (2, 5, CURRENT_TIMESTAMP + 1, CURRENT_TIMESTAMP + 2, 3, false);

--INSERT INTO quest_trials (quest_id, trials_id) values (2, 5);
--INSERT INTO quest_trials (quest_id, trials_id) values (2, 6);
--INSERT INTO quest_trials (quest_id, trials_id) values (2, 7);
--INSERT INTO quest_trials (quest_id, trials_id) values (2, 8);
--INSERT INTO quest_trials (quest_id, trials_id) values (2, 9);
--INSERT INTO quest_trials (quest_id, trials_id) values (2, 10);

---- ##### Quest with low time interval ############## --
INSERT INTO quest (user_id, title, subject_id, start_date, finish_date, time_interval, time_unit, finished) values (2, 'quest_low_interval', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + 0.008333334, 3, 4, false);
--INSERT INTO quest_subscribed_users (quest_id, subscribed_users_id) values (3, 4);
--INSERT INTO quest_subscribed_users (quest_id, subscribed_users_id) values (3, 5);
INSERT INTO quest_subscribed_users_ids (quest_id, subscribed_users_ids) values (3, 4);
INSERT INTO quest_subscribed_users_ids (quest_id, subscribed_users_ids) values (3, 5);

INSERT INTO trial (quest_id, subscribed_user_id, start_date, finish_date, trial_number, finished) values (3, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + 0.002777778, 1, false);
INSERT INTO trial (quest_id, subscribed_user_id, start_date, finish_date, trial_number, finished) values (3, 4, CURRENT_TIMESTAMP + 0.002777778, CURRENT_TIMESTAMP + 0.005555556, 2, false);
INSERT INTO trial (quest_id, subscribed_user_id, start_date, finish_date, trial_number, finished) values (3, 4, CURRENT_TIMESTAMP + 0.005555556, CURRENT_TIMESTAMP + 0.008333334, 3, false);
INSERT INTO trial (quest_id, subscribed_user_id, start_date, finish_date, trial_number, finished) values (3, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + 0.002777778, 1, false);
INSERT INTO trial (quest_id, subscribed_user_id, start_date, finish_date, trial_number, finished) values (3, 5, CURRENT_TIMESTAMP + 0.002777778, CURRENT_TIMESTAMP + 0.005555556, 2, false);
INSERT INTO trial (quest_id, subscribed_user_id, start_date, finish_date, trial_number, finished) values (3, 5, CURRENT_TIMESTAMP + 0.005555556, CURRENT_TIMESTAMP + 0.008333334, 3, false);

--INSERT INTO quest_trials (quest_id, trials_id) values (3, 11);
--INSERT INTO quest_trials (quest_id, trials_id) values (3, 12);
--INSERT INTO quest_trials (quest_id, trials_id) values (3, 13);
--INSERT INTO quest_trials (quest_id, trials_id) values (3, 14);
--INSERT INTO quest_trials (quest_id, trials_id) values (3, 15);
--INSERT INTO quest_trials (quest_id, trials_id) values (3, 16);

---- ##### Quest with VERY low time interval ############## --
INSERT INTO quest (user_id, title, subject_id, start_date, finish_date, time_interval, time_unit, finished) values (2, 'very_short_quest', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + 0.002777776, 2, 4, false);
--INSERT INTO quest_subscribed_users (quest_id, subscribed_users_id) values (4, 4);
--INSERT INTO quest_subscribed_users (quest_id, subscribed_users_id) values (4, 5);
INSERT INTO quest_subscribed_users_ids (quest_id, subscribed_users_ids) values (4, 4);
INSERT INTO quest_subscribed_users_ids (quest_id, subscribed_users_ids) values (4, 5);

INSERT INTO trial (quest_id, subscribed_user_id, start_date, finish_date, trial_number, finished) values (4, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + 0.001388888, 1, false);
INSERT INTO trial (quest_id, subscribed_user_id, start_date, finish_date, trial_number, finished) values (4, 4, CURRENT_TIMESTAMP + 0.001388888, CURRENT_TIMESTAMP + 0.002777776, 2, false);
INSERT INTO trial (quest_id, subscribed_user_id, start_date, finish_date, trial_number, finished) values (4, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + 0.002777778, 1, false);
INSERT INTO trial (quest_id, subscribed_user_id, start_date, finish_date, trial_number, finished) values (4, 5, CURRENT_TIMESTAMP + 0.001388888, CURRENT_TIMESTAMP + 0.002777776, 2, false);

--INSERT INTO quest_trials (quest_id, trials_id) values (4, 17);
--INSERT INTO quest_trials (quest_id, trials_id) values (4, 18);
--INSERT INTO quest_trials (quest_id, trials_id) values (4, 19);
--INSERT INTO quest_trials (quest_id, trials_id) values (4, 20);

---- ##### Quest already finished ############## --
INSERT INTO quest (user_id, title, subject_id, start_date, finish_date, time_interval, time_unit, finished) values (2, 'already_finished_quest', 1, CURRENT_TIMESTAMP - 2, CURRENT_TIMESTAMP, 2, 7, true);
--INSERT INTO quest_subscribed_users (quest_id, subscribed_users_id) values (5, 4);
--INSERT INTO quest_subscribed_users (quest_id, subscribed_users_id) values (5, 5);
INSERT INTO quest_subscribed_users_ids (quest_id, subscribed_users_ids) values (5, 4);
INSERT INTO quest_subscribed_users_ids (quest_id, subscribed_users_ids) values (5, 5);

INSERT INTO trial (quest_id, subscribed_user_id, start_date, finish_date, trial_number, finished, score) values (5, 4, CURRENT_TIMESTAMP - 2, CURRENT_TIMESTAMP - 1, 1, true, 5.0);
INSERT INTO trial (quest_id, subscribed_user_id, start_date, finish_date, trial_number, finished, score) values (5, 4, CURRENT_TIMESTAMP - 1, CURRENT_TIMESTAMP, 2, true, 7.0);
INSERT INTO trial (quest_id, subscribed_user_id, start_date, finish_date, trial_number, finished, score) values (5, 5, CURRENT_TIMESTAMP - 2, CURRENT_TIMESTAMP - 1, 1, true, 8.0);
INSERT INTO trial (quest_id, subscribed_user_id, start_date, finish_date, trial_number, finished, score) values (5, 5, CURRENT_TIMESTAMP - 1, CURRENT_TIMESTAMP, 2, true, 6.0);

--INSERT INTO quest_trials (quest_id, trials_id) values (5, 21);
--INSERT INTO quest_trials (quest_id, trials_id) values (5, 22);
--INSERT INTO quest_trials (quest_id, trials_id) values (5, 23);
--INSERT INTO quest_trials (quest_id, trials_id) values (5, 24);

INSERT INTO quest_result (quest_id, result_key, result) values (5, 'student', 5.5);
INSERT INTO quest_result (quest_id, result_key, result) values (5, 'pupil', 6.0);

--ADD QUESTS TO SUBSCRIBED QUEST IN USERS
--INSERT INTO user_subscribed_quests (user_id, subscribed_quests_id) values (4, 1);
--INSERT INTO user_subscribed_quests (user_id, subscribed_quests_id) values (4, 2);
--INSERT INTO user_subscribed_quests (user_id, subscribed_quests_id) values (4, 3);
--INSERT INTO user_subscribed_quests (user_id, subscribed_quests_id) values (4, 4);
--INSERT INTO user_subscribed_quests (user_id, subscribed_quests_id) values (4, 5);
--INSERT INTO user_subscribed_quests (user_id, subscribed_quests_id) values (5, 1);
--INSERT INTO user_subscribed_quests (user_id, subscribed_quests_id) values (5, 2);
--INSERT INTO user_subscribed_quests (user_id, subscribed_quests_id) values (5, 3);
--INSERT INTO user_subscribed_quests (user_id, subscribed_quests_id) values (5, 4);
--INSERT INTO user_subscribed_quests (user_id, subscribed_quests_id) values (5, 5);
