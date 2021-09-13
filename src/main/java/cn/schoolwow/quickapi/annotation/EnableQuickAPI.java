package cn.schoolwow.quickapi.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(QuickAPIConfiguration.class)
public @interface EnableQuickAPI {
}
