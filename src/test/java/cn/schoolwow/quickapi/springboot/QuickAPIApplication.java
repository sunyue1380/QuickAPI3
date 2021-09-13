package cn.schoolwow.quickapi.springboot;

import cn.schoolwow.quickapi.annotation.EnableQuickAPI;
import cn.schoolwow.quickapi.domain.QuickAPIOption;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableQuickAPI
public class QuickAPIApplication {
    @Bean
    public QuickAPIOption quickAPIOption(){
        QuickAPIOption quickAPIOption = new QuickAPIOption();
        quickAPIOption.sourcePath = System.getProperty("user.dir") + "/src/test/java";
        quickAPIOption.apiEntityPredicate = (clazz->{
            return clazz.getName().startsWith("cn.schoolwow.quickapi.springboot.domain");
        });
        return quickAPIOption;
    }

    public static void main(String[] args) {
        SpringApplication.run(QuickAPIApplication.class, args);
    }
}