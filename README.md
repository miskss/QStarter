# QStarter


#### 介绍

SpringBoot快速开发的通用后台和APP服务及对应的后台管理系统。采用前后端分离模式，
接口使用基于OAuth2.0 JWT token 来授权。

+ 后台服务：SpringBoot2.X + MySQL + SpringSecurity + Spring Data JPA + Quartz
+ 后台页面：[`AdminLte3.0`](https://github.com/ColorlibHQ/AdminLTE) + Thymeleaf 
+ [`oauth2.0 access_token 和 refresh_token 的使用`](/doc/token和refresh_token的使用.doc) 

---

#### 安装教程

后台管理系统页面，在线演示：[`http://122.51.217.213:8080`](http://122.51.217.213:8080)  用户名 `adminsuperme` 密码 `123456`


1. mvn clean package -Dmaven.test.skip=true

2. 执行sql 文件夹下的  [`boot-template.sql`](/sql/boot-template.sql) 和 [`quartz_sql.sql`](/sql/quartz_sql.sql)

3. 启动
    +  linux      `nohup java -jar admin-0.0.1-SNAPSHOT.jar  >/dev/null 2>log & `
    +  windows    `java -jar admin-0.0.1-SNAPSHOT.jar`

---
### 使用说明

1. #### 项目模块说明
      整个项目有四个模块构成：`security`、`core` 、`admin` 、`app`。
   
    + `security`模块：整个系统权限控制，包括所有的用户权限及登录授权获取token。
   
    + `core` 模块：整个系统的通用功能和相关配置，及工具类等。
   
    + `admin` 模块：负责网页后台的所有接口及数据读写。
    
    + `app` 模块：用户app端的接口及数据处理。

2. #### 部分功能说明
   
   * 采用OAuth2.0 协议下的grant-type：password 和自定义 的类型 来进行授权。
   
   * 获取token接口：`$curl -X POST -u client:secret@localhost:8080/oauth/token  -d grant_type=password -d username=user -d password=pwd`
   * 根据RefreshToken刷新token 接口 `$curl -X  POST -u client:secret@localhost:8080/oauth/token -d grant_type=refresh_token -d refresh_token=refresh_token`
   * /oauth/token 接口只能在系统内部调用。不能暴露出去，也就是说app和admin 模块应该额外提供对外的登录接口，在该登录接口中使用http请求去向权限系统获取token。
   * 系统所有的接口都应遵循 RESTful 风格
---

