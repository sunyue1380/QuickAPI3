# 快速入门

## 三步入门

### 1 引入QuickAPI
```xml
<dependency>
      <groupId>cn.schoolwow</groupId>
      <artifactId>QuickAPI</artifactId>
      <version>{最新版本}</version>
    </dependency>
```

> [QuickAPI最新版本查询](https://search.maven.org/search?q=a:QuickAPI)

### 2 启用QuickAPI

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

### 3 访问QuickAPI地址

启动程序,访问http://ip:port/quickapi/index.html即可

## 进阶配置

您可以通过设置API配置对象来改变默认的API生成过程.[点此访问](/zh-cn/start/option.md)

查看API界面使用教程.[点此访问](/zh-cn/start/front.md)