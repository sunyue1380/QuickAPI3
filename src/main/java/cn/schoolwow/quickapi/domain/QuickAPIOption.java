package cn.schoolwow.quickapi.domain;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;

import java.lang.reflect.Parameter;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * QuickAPI配置项
 * */
public class QuickAPIOption {
    /**API文档标题*/
    public String title = "QuickAPI";

    /**API文档描述*/
    public String description;

    /**Java源文件路径*/
    public String sourcePath;

    /**判断该类型是否为参数实体类*/
    public Predicate<Class> apiEntityPredicate;

    /**APIController配置*/
    public Consumer<APIController> apiControllerConsumer;

    /**APIEntity配置*/
    public Consumer<APIEntity> apiEntityConsumer;

    /**API参数处理函数*/
    public BiFunction<API, Parameter, APIParameter> apiParameterFunction;

    /**classDoc配置*/
    public BiConsumer<ClassDoc,APIController> classDocConsumer;

    /**methodDoc配置*/
    public BiConsumer<MethodDoc,API> methodDocConsumer;
}