package cn.schoolwow.quickapi.annotation;

import cn.schoolwow.quickapi.controller.QuickAPIController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {QuickAPIController.class})
public class QuickAPIConfiguration {
}