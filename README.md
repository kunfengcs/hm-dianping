# hm-dianping_back

#### 介绍
毕业设计项目
同城生活O2O系统平台后端
其实就是黑马点评

#### [项目前端](dianping_front/nginx-1.18.0/html/hmdp)
## 使用的技术
#### 后端
- Spring Boot 2.3.12.RELEASE
- Redis（通过spring-boot-starter-data-redis集成）   6.0 以上
- Commons Pool 2
- Spring Web（spring-boot-starter-web）
- MySQL Connector Java
- Lombok
- MyBatis Plus 3.5.3.1
- Hutool 5.7.17
- Knife4j Spring Boot Starter 2.0.9
- AspectJ Weaver

#### 前端
1. 非脚手架，引用vue.js v2.5.16;
2. element.js
## 构建和运行

确保你的环境中已经安装了以下工具和软件：

- Java 1.8
- Maven
#### 前端
1.  配置[nginx.conf](nginx_conf/nginx.conf)
2.  将项目放入nginx/html 下

### 运行
#### redis 使用6.0以上版本
#### 图片上传文件地址在 nginx里面 ，需要修改 [SystemConstants.java](src/main/java/com/hmdp/utils/SystemConstants.java)
#### 需要在 Nginx中创建temp文件夹
否则Nginx 无法启动
报错：2024/04/15 13:39:34 [emerg] 29240#27228: CreateDirectory() "E:\sh_sg\graduation_project\hmdp\hm-dianping_front\nginx-1.18.0/temp/client_body_temp" failed (3: The system cannot find the path specified)


### 
其实就是黑马点评

### 可扩展点
点评扩展点：
1.上多级缓存，监听binglog用mq保证缓存跟数据库的一致性
2.压测秒杀接口，算出塌陷区，用sentinel限流熔断
3.上设计模式（秒杀优惠卷流程 查找优惠卷信息 判断活动时间 判断库存 判断个人库存 扣减库存 设计成模版模式）
4.用xxl-job定时预热缓存 