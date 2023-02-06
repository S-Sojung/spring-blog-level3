CREATE TABLE user_tb(
    id int auto_increment primary key,
    username varchar unique not null,
    password varchar not null,
    email varchar not null,
    created_at timestamp not null
);

CREATE TABLE board_tb(
    id int auto_increment primary key,
    title varchar(100) not null, --byte가 아니라 문자열의 길이.
    content longtext not null,
    user_id int not null,
    created_at timestamp not null
);
    -- constraint fk_user foreign key (user_id) references user_tb (id)


