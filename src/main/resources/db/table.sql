CREATE TABLE user_tb(
    id int auto_increment primary key,
    username varchar unique not null,
    password varchar not null,
    email varchar not null,
    profile varchar, -- 사진을 파일로 저장할 것임! 웹서버가 html 파일을 가지고 있는데... 거기에다가 사진을 저장 할 것. (경로만 사용할 것이란 말.)
    role varchar default 'USER',
    created_at timestamp not null
);

CREATE TABLE board_tb(
    id int auto_increment primary key,
    title varchar(100) not null, --byte가 아니라 문자열의 길이.
    content longtext not null,
    thumbnail longtext not null,
    user_id int not null,
    created_at timestamp not null
);

CREATE TABLE reply_tb(
    id int auto_increment primary key,
    comment varchar(100) not null, 
    user_id int not null,
    board_id int not null,
    created_at timestamp not null
);


