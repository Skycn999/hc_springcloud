服务注册中心
开发阶段启用一个注册中心即可。

高可用服务注册中心配置：

1.修改application.yml文件为application-peer1.yml
配置如下：
server:
  port: 8761
eureka:
  instance:
    hostname: localhost
  client:
    service-url:
      default-zone: http://peer1:8760/eureka/
spring:
  profiles: peer1
  application:
    name: eureka-server-peer1
    
2.创建另外一个application-peer2.yml
server:
  port: 8760
spring:
  profiles: peer2
eureka:
  instance:
    hostname: eureka-server-peer2
  client:
    serviceUrl:
      defaultZone: http://peer1:8761/eureka/
      
 3.按照官方文档的指示，需要改变etc/hosts，linux系统通过vim /etc/hosts ,加上：
   
   127.0.0.1 peer1
   127.0.0.1 peer2
   
 4.服务提供方修改application.yml的defaultZone 为两个注册中心的url
 
 5.需要通过命令的方式启动注册中心
    java -jar eureka-server-0.0.1-SNAPSHOT.jar - -spring.profiles.active=peer1
    java -jar eureka-server-0.0.1-SNAPSHOT.jar - -spring.profiles.active=peer2
    
  6.启动服务提供方查看效果。
    
    