INSERT INTO user_tb(username, password, email, role, created_at) values('admin', '1234', 'admin@nate.com','ADMIN', now());
INSERT INTO user_tb(username, password, email, created_at) values('ssar', '1234', 'ssar@nate.com', now());
INSERT INTO user_tb(username, password, email, created_at) values('love', '1234', 'love@nate.com', now());
-- 나에 대한 상세한 정보는 웹 사이트를 이용하는 중간에 추가하던가함.

INSERT INTO board_tb(title, content, thumbnail, user_id, created_at) values('1번째 제목', '1번째 내용', '/images/dora.png', 3, now());
INSERT INTO board_tb(title, content, thumbnail, user_id, created_at) values('2번째 제목', '2번째 내용', '/images/dora.png', 3, now());
INSERT INTO board_tb(title, content, thumbnail, user_id, created_at) values('3번째 제목', '3번째 내용', '/images/dora.png', 3, now());
INSERT INTO board_tb(title, content, thumbnail, user_id, created_at) values('4번째 제목', '4번째 내용', '/images/dora.png', 2, now());
INSERT INTO board_tb(title, content, thumbnail, user_id, created_at) values('5번째 제목', '5번째 내용', '/images/dora.png', 2, now());
INSERT INTO board_tb(title, content, thumbnail, user_id, created_at) values('6번째 제목', '6번째 내용', '/images/dora.png', 2, now());

INSERT INTO reply_tb(comment, user_id, board_id, created_at) values('댓글1이에용', 2, 2, now());
INSERT INTO reply_tb(comment, user_id, board_id, created_at) values('댓글2이에용', 2, 3, now());
INSERT INTO reply_tb(comment, user_id, board_id, created_at) values('댓글3이에용', 3, 2, now());
INSERT INTO reply_tb(comment, user_id, board_id, created_at) values('댓글4이에용', 3, 3, now());

INSERT INTO love_tb(love, user_id, board_id, created_at) values('fa-solid', 2, 3, now());
INSERT INTO love_tb(love, user_id, board_id, created_at) values('fa-solid', 3, 3, now());
INSERT INTO love_tb(love, user_id, board_id, created_at) values('fa-solid', 2, 4, now());
INSERT INTO love_tb(love, user_id, board_id, created_at) values('fa-solid', 3, 4, now());
INSERT INTO love_tb(love, user_id, board_id, created_at) values('fa-solid', 3, 5, now());

commit;