use questions;

-- 用户表
create table users(
user_id INTEGER auto_increment, -- 用户ID
user_name varchar(20),          -- 用户名
user_password varchar(60),      -- 用户密码
user_status INTEGER DEFAULT 0,  -- 用户状态，0表示正常，1表示被拉黑
primary key (user_id),          -- 设置逐渐
UNIQUE(user_name)               -- 设置用户名不重复
);

-- 添加用户触发器，每当users表中新增用户的时候，就会在users_roles表中新增用户的角色
CREATE TRIGGER `add_user_trigger1` AFTER INSERT ON `users` FOR EACH ROW BEGIN
INSERT INTO users_roles (user_id, role_id) values (NEW.user_id, 2);
END;

-- 角色表，表中数据包括管理员 admin，普通用户 user，未通过管理员同意注册用户 illegal
create table roles(
role_id INTEGER auto_increment,  -- 角色ID
role_name varchar(20),           -- 角色名
primary key (role_id),           -- 设置主键
UNIQUE(role_name)                --设置角色名不重复
);

-- 用户角色关系表
create table users_roles(
id INTEGER AUTO_INCREMENT,       -- 数据ID
user_id INTEGER,                 -- 用户ID
role_id INTEGER,                 -- 用户拥有的角色ID
foreign key(user_id) references users(user_id),  -- 设置用户ID为外键
foreign key(role_id) references roles(role_id),  -- 设置角色ID为外键
primary key (id)                                 -- 设置主键
);

-- 问题表
create table questions(
question_id INTEGER auto_increment,                   -- 问题ID
user_id INTEGER,                                      -- 发布问题的用户的ID
question_title TEXT,                                  -- 问题标题
question_details TEXT,                                -- 问题详情
last_answer_date TIMESTAMP DEFAULT current_timestamp, -- 问题的最后一个回答的时间
question_solved TINYINT(1) DEFAULT 0,                 -- 问题是否已解决
question_blocked TINYINT(1) DEFAULT 0,                -- 问题是否被屏蔽
primary key (question_id),                            -- 设置问题ID主键
foreign key(user_id) references users(user_id)        -- 设置用户ID外键
);

-- 问题的回答表
create table answers(
answer_id INTEGER auto_increment,                        -- 回答ID
question_id INTEGER,                                     -- 回答所属于的问题的ID
user_id INTEGER,                                         -- 发表回答的用户的ID
answer_details TEXT,                                     -- 回答详情
answer_date TIMESTAMP DEFAULT current_timestamp,         -- 回答的日期
answer_blocked TINYINT(1) DEFAULT 0,                     -- 问题是否被屏蔽
primary key (answer_id),                                 -- 设置回答ID主键
foreign key(question_id) references questions(user_id),  -- 设置问题ID外键
foreign key(user_id) references users(user_id)           -- 设置用户ID外键
);

-- 回答表触发器，当回答表有新回答插入时，根据插入回答的question_id去更新问题表当中问题的的最后一个回答的时间
CREATE DEFINER = `root`@`localhost` TRIGGER `add_answer_trigger` AFTER INSERT ON `answers` FOR EACH ROW BEGIN
UPDATE questions SET last_answer_date=NOW() where question_id=NEW.question_id;
END;