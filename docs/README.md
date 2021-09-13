# QuickAPI3

QuickAPI3是一个生成后台API接口并且支持在线测试的Web服务框架.

> 目前版本只支持SpringBoot环境

# 快速入门

## 1 引入QuickAPI
```xml
<dependency>
      <groupId>cn.schoolwow</groupId>
      <artifactId>QuickAPI</artifactId>
      <version>{最新版本}</version>
    </dependency>
```

> [QuickAPI最新版本查询](https://search.maven.org/search?q=a:QuickAPI)

## 2 启用QuickAPI

启用QuickAPI非常简单,只需要添加注解@EnableQuickAPI即可

```java
@SpringBootApplication
@EnableQuickAPI
public class QuickAPIApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuickAPIApplication.class, args);
    }
}
```

## 3 访问QuickAPI地址

启动程序,访问http://ip:port/quickapi/index.html即可

# 详细文档

[点此访问](https://quickapi.schoolwow.cn/)

# 反馈

* 提交Issue
* 邮箱: 648823596@qq.com
* QQ群: 958754367(quick系列交流,群初建,人较少)

# 开源协议
本软件使用 [GPL](http://www.gnu.org/licenses/gpl-3.0.html) 开源协议!