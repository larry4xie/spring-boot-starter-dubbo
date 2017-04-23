# spring-boot-starter-dubbo
> Dubbo 项目的 Spring Boot 脚手架

主要包含功能:

1. DubboHolder: 阻止dubbo服务端进程的退出（和spring生命周期绑定解决ut无法退出问题）
2. 同时支持 XML and Annotation 方式进行配置
3. spring boot actuator
    * DubboEndpoint
    * DubboHealthIndicator

## dependency
```xml
<dependency>
  <groupId>xyz.lxie</groupId>
  <artifactId>spring-boot-starter-dubbo</artifactId>
  <version>${starter-dubbo.version}</version>
</dependency>
```

> search [newest version](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22xyz.lxie%22%20a%3A%22spring-boot-starter-dubbo%22)

## config
> Annotation based，可选功能，可以继续使用xml方式配置，也可以两者混合使用

```properties
# 替代 XML <dubbo:application name="" owner=""/>, <dubbo:registry address="" group=""/>, <dubbo:protocol port=""/>
spring.dubbo.application.name=starter-dubbo-demo
spring.dubbo.application.owner=xiegang
spring.dubbo.registry.address=zookeeper://zk1.lxie.xyz:22181?backup=zk2.lxie.xyz:22181,zk.lxie.xyz:22181
spring.dubbo.registry.group=dubbo_test
spring.dubbo.protocol.port=20880

# 替代 XML <dubbo:annotation package="xyz.lxie.xxx" />
spring.dubbo.scan=lxie.xyz.xxx
```

## actuator
### dubbo health
```json
{
  "status": "UP",
  "dubbo": {
    "status": "UP",
    "load": "[OK] load:2.18408203125,cpu:4",
    "memory": "[OK] max:1820M,total:192M,used:126M,free:66M",
    "registry": "[OK] zktestserver1.lxie.xyz:22181(connected)",
    "server": "[OK] /172.16.106.81:20880(clients:0)",
    "threadpool": "[OK] Pool status:OK, max:200, core:200, largest:0, active:0, task:0, service port: 20880"
  }
}
```

### dubbo endpoint
```json
{
  "services": {
    "lxie.xyz.service.DemoService": [
      "sayHello",
      "hello"
    ]
  },
  "status": {
    "server": {
      "level": "OK",
      "message": "/172.16.106.81:20880(clients:0)"
    },
    "registry": {
      "level": "OK",
      "message": "zktestserver1.lxie.xyz:22181(connected)"
    },
    "memory": {
      "level": "OK",
      "message": "max:1820M,total:192M,used:125M,free:67M"
    },
    "load": {
      "level": "OK",
      "message": "load:2.3046875,cpu:4"
    },
    "threadpool": {
      "level": "OK",
      "message": "Pool status:OK, max:200, core:200, largest:0, active:0, task:0, service port: 20880"
    }
  }
}
```
