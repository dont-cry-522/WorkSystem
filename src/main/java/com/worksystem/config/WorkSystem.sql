
CREATE TABLE user (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
                      password VARCHAR(100) NOT NULL COMMENT '密码',
                      name VARCHAR(50) NOT NULL COMMENT '姓名',
                      user_type ENUM('student', 'teacher') NOT NULL COMMENT '用户类型，student为学生，teacher为管理老师',
                      college VARCHAR(100) DEFAULT NULL COMMENT '学院（学生适用）',
                      mobile VARCHAR(20) DEFAULT NULL COMMENT '手机号',
                      email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
                      introduction TEXT COMMENT '个人简介（学生适用）'
);


CREATE TABLE position (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          position_name VARCHAR(100) NOT NULL COMMENT '岗位名称',
                          work_content TEXT NOT NULL COMMENT '工作内容',
                          work_time VARCHAR(200) COMMENT '工作时间要求',
                          salary DECIMAL(10, 2) NOT NULL COMMENT '薪酬',
                          recruitment_status ENUM('recruiting', 'full', 'closed') DEFAULT 'recruiting' COMMENT '招聘状态，recruiting为招聘中，full为已满员，closed为已关闭',
                          teacher_id INT NOT NULL COMMENT '发布岗位的老师id，关联user表',
                          FOREIGN KEY (teacher_id) REFERENCES user(id)
);

CREATE TABLE position_application (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      student_id INT NOT NULL COMMENT '申请学生的id，关联user表',
                                      position_id INT NOT NULL COMMENT '申请岗位的id，关联position表',
                                      application_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
                                      application_status ENUM('pending', 'approved', 'rejected') DEFAULT 'pending' COMMENT '申请状态，pending为待审核，approved为已通过，rejected为已拒绝',
                                      FOREIGN KEY (student_id) REFERENCES user(id),
                                      FOREIGN KEY (position_id) REFERENCES position(id)
);

CREATE TABLE attendance (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            student_id INT NOT NULL COMMENT '学生id，关联user表',
                            position_id INT NOT NULL COMMENT '岗位id，关联position表',
                            work_date DATE NOT NULL COMMENT '考勤日期',
                            work_hours DECIMAL(5, 2) NOT NULL COMMENT '工作时长',
                            FOREIGN KEY (student_id) REFERENCES user(id),
                            FOREIGN KEY (position_id) REFERENCES position(id)
);

CREATE TABLE message (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         sender_id INT NOT NULL COMMENT '发送者id，关联user表',
                         receiver_id INT NOT NULL COMMENT '接收者id，关联user表',
                         message_content TEXT NOT NULL COMMENT '消息内容',
                         send_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
                         is_read TINYINT(1) DEFAULT 0 COMMENT '是否已读，0为未读，1为已读',
                         FOREIGN KEY (sender_id) REFERENCES user(id),
                         FOREIGN KEY (receiver_id) REFERENCES user(id)
);

CREATE TABLE verification_code (
                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                   user_id INT NOT NULL COMMENT '用户id，关联user表',
                                   code VARCHAR(10) NOT NULL COMMENT '验证码',
                                   create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
                                   valid_time INT NOT NULL COMMENT '有效时长（分钟）',
                                   FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE position_statistics (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     position_id INT NOT NULL COMMENT '岗位id，关联position表',
                                     participated_student_count INT DEFAULT 0 COMMENT '参与该岗位的学生数量',
                                     willing_to_stay_count INT DEFAULT 0 COMMENT '愿意继续留在该岗位的学生数量',
                                     FOREIGN KEY (position_id) REFERENCES position(id)
);