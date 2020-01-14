CREATE DATABASE `temp_boot` DEFAULT CHARACTER SET utf8mb4;

DROP TABLE IF EXISTS app_user;

DROP TABLE IF EXISTS app_version;

DROP TABLE IF EXISTS log_message;

DROP TABLE IF EXISTS oauth_client_details;

DROP TABLE IF EXISTS sys_role_html;

DROP TABLE IF EXISTS system_html;

DROP TABLE IF EXISTS system_html_resources;

DROP TABLE IF EXISTS system_resource;

DROP TABLE IF EXISTS system_role;

DROP TABLE IF EXISTS system_role_resource;

DROP TABLE IF EXISTS system_user;

DROP TABLE IF EXISTS system_user_role;

DROP TABLE IF EXISTS web_user;


-- app_user: table
CREATE TABLE `app_user`
(
    `id`           bigint(20)  NOT NULL,
    `create_time`  datetime(6) NOT NULL,
    `update_time`  datetime(6) NOT NULL,
    `icon_url`     varchar(255) DEFAULT NULL,
    `nickname`     varchar(64)  DEFAULT NULL,
    `open_id`      varchar(64)  DEFAULT NULL,
    `phone_number` varchar(16)  DEFAULT NULL,
    `union_id`     varchar(64)  DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `app_user_union_id_uindex` (`union_id`),
    UNIQUE KEY `app_user_phone_number_uindex` (`phone_number`),
    UNIQUE KEY `app_user_open_id_uindex` (`open_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
;

-- app_version: table
CREATE TABLE `app_version`
(
    `id`               bigint(20)  NOT NULL AUTO_INCREMENT,
    `create_time`      datetime(6) NOT NULL,
    `update_time`      datetime(6) NOT NULL,
    `android_apk_path` varchar(255) DEFAULT NULL,
    `content`          varchar(255) DEFAULT NULL,
    `device`           varchar(8)  NOT NULL,
    `force_update`     bit(1)       DEFAULT NULL,
    `ios_url`          varchar(255) DEFAULT NULL,
    `title`            varchar(255) DEFAULT NULL,
    `version`          varchar(32)  DEFAULT NULL,
    `version_code`     int(11)      DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `app_version_device_version_version_code_uindex` (`device`, `version`, `version_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
;

-- log_message: table
CREATE TABLE `log_message`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT,
    `action_type` varchar(32)   DEFAULT NULL,
    `content`     varchar(1024) DEFAULT NULL,
    `create_time` datetime(6) NOT NULL,
    `title`       varchar(255)  DEFAULT NULL,
    `web_user`    varchar(64)   DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
;

-- oauth_client_details: table
CREATE TABLE `oauth_client_details`
(
    `client_id`               varchar(256) NOT NULL,
    `resource_ids`            varchar(256)  DEFAULT NULL,
    `client_secret`           varchar(256)  DEFAULT NULL,
    `scope`                   varchar(256)  DEFAULT NULL,
    `authorized_grant_types`  varchar(256)  DEFAULT NULL,
    `web_server_redirect_uri` varchar(256)  DEFAULT NULL,
    `authorities`             varchar(256)  DEFAULT NULL,
    `access_token_validity`   int(11)       DEFAULT NULL,
    `refresh_token_validity`  int(11)       DEFAULT NULL,
    `additional_information`  varchar(4096) DEFAULT NULL,
    `autoapprove`             varchar(256)  DEFAULT NULL,
    PRIMARY KEY (`client_id`),
    UNIQUE KEY `oauth_client_details_client_secret_uindex` (`client_secret`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
;


-- sys_role_html: table
CREATE TABLE `sys_role_html`
(
    `sys_role_id` bigint(20) NOT NULL,
    `sys_html_id` int(11)    NOT NULL,
    PRIMARY KEY (`sys_role_id`, `sys_html_id`),
    KEY `FKglmabyq3at966wttsoa57jhd5` (`sys_html_id`, `sys_role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
;

-- No native definition for element: FKglmabyq3at966wttsoa57jhd5 (index)

-- system_html: table
CREATE TABLE `system_html`
(
    `id`          int(11)     NOT NULL AUTO_INCREMENT,
    `create_time` datetime(6) NOT NULL,
    `update_time` datetime(6) NOT NULL,
    `alias`       varchar(32)  DEFAULT NULL,
    `html_addr`   varchar(128) DEFAULT NULL,
    `html_name`   varchar(64)  DEFAULT NULL,
    `html_type`   varchar(16)  DEFAULT NULL,
    `icon_url`    varchar(255) DEFAULT NULL,
    `sort_num`    int(11)     NOT NULL,
    `parent_id`   int(11)      DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `system_html_html_name_html_type_html_addr_uindex` (`html_name`, `html_type`, `html_addr`),
    KEY `FKkayk48iwhpw4b1jxbnosldta` (`parent_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 44
  DEFAULT CHARSET = utf8mb4
;

-- No native definition for element: FKkayk48iwhpw4b1jxbnosldta (index)

-- system_html_resources: table
CREATE TABLE `system_html_resources`
(
    `system_html_id` int(11)    NOT NULL,
    `resources_id`   bigint(20) NOT NULL,
    KEY `FKg94pg4cm9vbj3nl4jivcbxeti` (`resources_id`),
    KEY `FKp5arufmofkao1q0ebmcoq2vn0` (`system_html_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
;

-- No native definition for element: FKp5arufmofkao1q0ebmcoq2vn0 (index)

-- No native definition for element: FKg94pg4cm9vbj3nl4jivcbxeti (index)

-- system_resource: table
CREATE TABLE `system_resource`
(
    `id`            bigint(20)  NOT NULL AUTO_INCREMENT,
    `create_time`   datetime(6) NOT NULL,
    `update_time`   datetime(6) NOT NULL,
    `operation`     varchar(255) DEFAULT NULL,
    `resource_name` varchar(255) DEFAULT NULL,
    `resource_url`  varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `system_resource_resource_name_resource_url_operation_uindex` (`resource_name`, `resource_url`, `operation`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 86
  DEFAULT CHARSET = utf8mb4
;

-- system_role: table
CREATE TABLE `system_role`
(
    `id`               bigint(20)  NOT NULL AUTO_INCREMENT,
    `create_time`      datetime(6) NOT NULL,
    `update_time`      datetime(6) NOT NULL,
    `role_description` varchar(128) DEFAULT NULL,
    `role_name`        varchar(64)  DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `system_role_role_name_uindex` (`role_name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 8
  DEFAULT CHARSET = utf8mb4
;

-- system_role_resource: table
CREATE TABLE `system_role_resource`
(
    `system_role_id` bigint(20) NOT NULL,
    `resource_id`    bigint(20) NOT NULL,
    PRIMARY KEY (`system_role_id`, `resource_id`),
    KEY `FKa2xhmvsofrpev8xob1opta5cd` (`resource_id`, `system_role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
;

-- No native definition for element: FKa2xhmvsofrpev8xob1opta5cd (index)

-- system_user: table
CREATE TABLE `system_user`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT,
    `create_time` datetime(6) NOT NULL,
    `update_time` datetime(6) NOT NULL,
    `password`    varchar(128) DEFAULT NULL,
    `username`    varchar(128) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_username_uni_idx` (`username`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
;

-- system_user_role: table
CREATE TABLE `system_user_role`
(
    `system_user_id` bigint(20) NOT NULL,
    `role_id`        bigint(20) NOT NULL,
    PRIMARY KEY (`system_user_id`, `role_id`),
    KEY `FKnp61n3syn415rmbwvhnw87u3a` (`role_id`, `system_user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
;

-- No native definition for element: FKnp61n3syn415rmbwvhnw87u3a (index)

-- web_user: table
CREATE TABLE `web_user`
(
    `id`           bigint(20)  NOT NULL,
    `create_time`  datetime(6) NOT NULL,
    `update_time`  datetime(6) NOT NULL,
    `icon_url`     varchar(255)         DEFAULT NULL,
    `phone_number` varchar(16) NOT NULL DEFAULT '',
    `username`     varchar(32) NOT NULL DEFAULT '',
    PRIMARY KEY (`id`),
    UNIQUE KEY `web_user_username_uni_idx` (`username`),
    UNIQUE KEY `web_user_phone_number_uindex` (`phone_number`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
;

-- 插入数据
INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('walkmoneyjwtclient', null, '$2a$10$ZHW3WO.eNzoPjwoyqUSUduU/fUzzD.KFiPqxlEUHDY/YIi9An9Ojm', 'read,write', 'password,refresh_token,client_credentials,authorization_code,id', null, 'ROLE_CLIENT,ROLE_TRUSTED_CLIENT', 43200, 1296000, null, null);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 1);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 3);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 5);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 6);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 7);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 8);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 9);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 10);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 11);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 13);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 14);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 15);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 16);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 17);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 18);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 19);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 20);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 21);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 22);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 23);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 24);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 25);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 26);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 27);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 33);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 34);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 35);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 36);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 37);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 38);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 39);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 40);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 41);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 42);
INSERT INTO sys_role_html (sys_role_id, sys_html_id) VALUES (7, 43);
INSERT INTO system_html (id, create_time, update_time, alias, html_addr, html_name, html_type, icon_url, sort_num, parent_id) VALUES (1, '2019-11-19 11:53:06.207000', '2019-12-30 09:39:59.033000', null, '', '系统设置', 'PAGE', 'fas fa-user-cog', 10, null);
INSERT INTO system_html (id, create_time, update_time, alias, html_addr, html_name, html_type, icon_url, sort_num, parent_id) VALUES (3, '2019-12-05 09:42:16.008000', '2019-12-05 09:42:16.008000', null, '/page/system-user.html', '人员管理', 'PAGE', '', 0, 1);
INSERT INTO system_html (id, create_time, update_time, alias, html_addr, html_name, html_type, icon_url, sort_num, parent_id) VALUES (5, '2019-12-05 09:44:14.313000', '2019-12-05 09:44:14.313000', null, '', '新增人员', 'FUNC', '', 0, 3);
INSERT INTO system_html (id, create_time, update_time, alias, html_addr, html_name, html_type, icon_url, sort_num, parent_id) VALUES (6, '2019-12-05 09:44:57.163000', '2019-12-05 09:44:57.163000', null, '', '修改人员', 'FUNC', '', 2, 3);
INSERT INTO system_html (id, create_time, update_time, alias, html_addr, html_name, html_type, icon_url, sort_num, parent_id) VALUES (7, '2019-12-05 09:45:23.411000', '2019-12-05 09:45:23.411000', null, '', '删除人员', 'FUNC', '', 3, 3);
INSERT INTO system_html (id, create_time, update_time, alias, html_addr, html_name, html_type, icon_url, sort_num, parent_id) VALUES (8, '2019-12-05 16:20:21.597000', '2019-12-05 16:20:21.597000', null, '/page/system-role.html', '角色管理', 'PAGE', '', 1, 1);
INSERT INTO system_html (id, create_time, update_time, alias, html_addr, html_name, html_type, icon_url, sort_num, parent_id) VALUES (40, '2019-12-30 09:32:05.435000', '2019-12-30 09:32:15.166000', null, '', '新增角色', 'FUNC', '', 0, 8);
INSERT INTO system_html (id, create_time, update_time, alias, html_addr, html_name, html_type, icon_url, sort_num, parent_id) VALUES (41, '2019-12-30 09:32:56.293000', '2019-12-30 09:33:47.748000', null, '', '角色权限配置', 'FUNC', '', 0, 8);
INSERT INTO system_html (id, create_time, update_time, alias, html_addr, html_name, html_type, icon_url, sort_num, parent_id) VALUES (42, '2019-12-30 09:34:21.437000', '2019-12-30 09:34:21.437000', null, '', '删除角色', 'FUNC', '', 0, 8);
INSERT INTO system_html (id, create_time, update_time, alias, html_addr, html_name, html_type, icon_url, sort_num, parent_id) VALUES (43, '2019-12-30 09:36:21.745000', '2019-12-30 09:37:11.348000', null, '/page/sys-log.html', '系统日志', 'PAGE', '', 0, 1);
INSERT INTO system_html_resources (system_html_id, resources_id) VALUES (3, 16);
INSERT INTO system_html_resources (system_html_id, resources_id) VALUES (5, 12);
INSERT INTO system_html_resources (system_html_id, resources_id) VALUES (6, 13);
INSERT INTO system_html_resources (system_html_id, resources_id) VALUES (7, 14);
INSERT INTO system_html_resources (system_html_id, resources_id) VALUES (8, 15);
INSERT INTO system_html_resources (system_html_id, resources_id) VALUES (40, 1);
INSERT INTO system_html_resources (system_html_id, resources_id) VALUES (40, 60);
INSERT INTO system_html_resources (system_html_id, resources_id) VALUES (41, 2);
INSERT INTO system_html_resources (system_html_id, resources_id) VALUES (41, 84);
INSERT INTO system_html_resources (system_html_id, resources_id) VALUES (41, 60);
INSERT INTO system_html_resources (system_html_id, resources_id) VALUES (42, 3);
INSERT INTO system_html_resources (system_html_id, resources_id) VALUES (43, 85);
INSERT INTO system_resource (id, create_time, update_time, operation, resource_name, resource_url) VALUES (1, '2019-09-26 14:34:38.000000', '2019-09-26 14:34:38.000000', 'POST', '新增角色', '/api/web/roles');
INSERT INTO system_resource (id, create_time, update_time, operation, resource_name, resource_url) VALUES (2, '2019-09-26 14:35:04.000000', '2019-09-26 14:35:04.000000', 'PUT', '修改角色', '/api/web/roles');
INSERT INTO system_resource (id, create_time, update_time, operation, resource_name, resource_url) VALUES (3, '2019-09-26 14:35:25.000000', '2019-09-26 14:35:25.000000', 'DELETE', '删除角色', '/api/web/roles');
INSERT INTO system_resource (id, create_time, update_time, operation, resource_name, resource_url) VALUES (12, '2019-09-26 14:36:09.000000', '2019-09-26 14:36:09.000000', 'POST', '新增人员', '/api/web/manage-user');
INSERT INTO system_resource (id, create_time, update_time, operation, resource_name, resource_url) VALUES (13, '2019-09-26 14:36:53.000000', '2019-09-26 14:36:53.000000', 'PUT', '修改人员', '/api/web/manage-user');
INSERT INTO system_resource (id, create_time, update_time, operation, resource_name, resource_url) VALUES (14, '2019-09-26 14:37:16.000000', '2019-09-26 14:37:16.000000', 'DELETE', '删除人员', '/api/web/manage-user');
INSERT INTO system_resource (id, create_time, update_time, operation, resource_name, resource_url) VALUES (15, '2019-09-26 14:38:40.000000', '2019-11-11 13:47:27.476000', 'GET', '查看角色列表', '/api/web/roles');
INSERT INTO system_resource (id, create_time, update_time, operation, resource_name, resource_url) VALUES (16, '2019-09-26 14:39:01.000000', '2019-09-26 14:39:01.000000', 'GET', '查看人员列表', '/api/web/manage-user');
INSERT INTO system_resource (id, create_time, update_time, operation, resource_name, resource_url) VALUES (59, '2019-12-05 09:40:51.933000', '2019-12-05 09:40:51.933000', 'GET', '菜单列表', '/api/web/html');
INSERT INTO system_resource (id, create_time, update_time, operation, resource_name, resource_url) VALUES (60, '2019-12-06 09:05:03.093000', '2019-12-06 09:05:03.093000', 'GET', '页面树资源', '/api/web/html/ztree');
INSERT INTO system_resource (id, create_time, update_time, operation, resource_name, resource_url) VALUES (84, '2019-12-30 09:33:39.434000', '2019-12-30 09:33:39.434000', 'GET', '角色拥有的资源数', '/api/web/roles/ztree');
INSERT INTO system_resource (id, create_time, update_time, operation, resource_name, resource_url) VALUES (85, '2019-12-30 09:36:56.007000', '2019-12-30 09:36:56.007000', 'GET', '系统日志', '/api/web/log/sys-log');
INSERT INTO system_role (id, create_time, update_time, role_description, role_name) VALUES (1, '2019-09-26 06:10:20.000000', '2019-09-26 06:10:22.000000', '超级管理员', 'SUPER_ADMIN');
INSERT INTO system_role (id, create_time, update_time, role_description, role_name) VALUES (2, '2019-09-26 06:10:44.000000', '2019-09-26 06:10:46.000000', 'WEB', 'WEB');
INSERT INTO system_role (id, create_time, update_time, role_description, role_name) VALUES (4, '2019-09-27 02:25:10.000000', '2019-09-27 02:25:12.000000', 'APP', 'APP');
INSERT INTO system_role (id, create_time, update_time, role_description, role_name) VALUES (7, '2019-12-04 11:13:12.187000', '2019-12-30 09:42:53.885000', '管理员', 'admin');
INSERT INTO system_role_resource (system_role_id, resource_id) VALUES (7, 1);
INSERT INTO system_role_resource (system_role_id, resource_id) VALUES (7, 2);
INSERT INTO system_role_resource (system_role_id, resource_id) VALUES (7, 3);
INSERT INTO system_role_resource (system_role_id, resource_id) VALUES (7, 12);
INSERT INTO system_role_resource (system_role_id, resource_id) VALUES (7, 13);
INSERT INTO system_role_resource (system_role_id, resource_id) VALUES (7, 14);
INSERT INTO system_role_resource (system_role_id, resource_id) VALUES (7, 15);
INSERT INTO system_role_resource (system_role_id, resource_id) VALUES (7, 16);
INSERT INTO system_role_resource (system_role_id, resource_id) VALUES (7, 60);
INSERT INTO system_role_resource (system_role_id, resource_id) VALUES (7, 84);
INSERT INTO system_role_resource (system_role_id, resource_id) VALUES (7, 85);
INSERT INTO system_user (id, create_time, update_time, password, username) VALUES (1, '2019-09-26 03:42:21.000000', '2019-12-13 17:12:23.548000', '$2a$10$DHKyvex2OO9QFfcWCQnAvOS0V7ktMAWNaGx1fGs1ZAvrRsLs9Kre6', 'adminsuperme@WEB');
INSERT INTO system_user (id, create_time, update_time, password, username) VALUES (2, '2019-12-30 09:42:20.010000', '2019-12-30 09:42:20.010000', '$2a$10$L6it/z0vxT3BTd05i7/9bOxVIOm1qZbsJHdfP/O1FXLdJlvJd.sAC', 'admin@WEB');
INSERT INTO system_user_role (system_user_id, role_id) VALUES (1, 1);
INSERT INTO system_user_role (system_user_id, role_id) VALUES (1, 2);
INSERT INTO system_user_role (system_user_id, role_id) VALUES (2, 2);
INSERT INTO system_user_role (system_user_id, role_id) VALUES (2, 7);
INSERT INTO web_user (id, create_time, update_time, icon_url, phone_number, username) VALUES (1, '2019-12-27 03:23:29.082000', '2019-12-27 03:23:26.921000', null, '10000000000', 'adminsuperme');
INSERT INTO web_user (id, create_time, update_time, icon_url, phone_number, username) VALUES (2, '2019-12-30 09:42:20.020000', '2019-12-30 09:42:20.020000', null, '12222222222', 'admin');