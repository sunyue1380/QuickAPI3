# API生成配置

QuickAPI支持若干配置项,您可以通过改变这些配置项来定制化生成API.

> 若未配置QuickAPIOption则QuickAPI会使用缺省配置.

要设置配置项,在SpringBoot环境下注入QuickAPIOption对象即可.

```java
@Bean
public QuickAPIOption quickAPIOption(){
    QuickAPIOption quickAPIOption = new QuickAPIOption();
    //设置各种配置项
    quickAPIOption.xxxxx = xxxx;
    return quickAPIOption;
}
```

## 设置基本信息

设置文档标题,描述信息

```java
quickAPIOption.title = "自定义标题";
quickAPIOption.description = "自定义描述信息";
```

## 设置源文件路径

QuickAPI通过javadoc获取类注释.若未指定源文件路径,则不会读取源文件注释信息.

> 请确保JAVA_HOME环境变量已经正确设置

> 若未指定源文件路径,则会使用默认值``System.getProperty("user.dir")+"/src/main/java"``

```java
quickAPIOption.sourcePath = System.getProperty("user.dir")+"/src/main/java";
```

## 设置实体类判断函数

当API参数或者返回结果为非基本类型时,您需要指定哪些类属于实体类以便在API接口界面展示实体类字段信息等.

如果您未设置此项,则API界面不会展示任何实体类信息.

```java
quickAPIOption.apiEntityPredicate = (clazz->{
    //所有以包名cn.schoolwow.quickapi.springboot.domain开头的类视为实体类
    return clazz.getName().startsWith("cn.schoolwow.quickapi.springboot.domain");
});
```

## 设置API信息

默认情况下,QuickAPI从java类以及javadoc获取注释信息和接口信息.

但如果您的项目引入了swagger,quickdao等其他框架,且需要从这些框架上获取API信息或者实体类信息时,您就可以通过此项配置来设置信息.

```java
//从swagger注解中获取信息
quickAPIOption.apiControllerConsumer = (apiController)->{
    Api apiAnnotation = (Api) apiController.clazz.getAnnotation(Api.class);
    if(null!=apiAnnotation){
        String description = apiAnnotation.value().isEmpty()?apiAnnotation.tags()[0]:apiAnnotation.value();
        if(!description.isEmpty()){
            apiController.setDescription(description);
        }
    }
    for(API api:apiController.apiList){
        ApiOperation apiOperation = api.method.getAnnotation(ApiOperation.class);
        if(null!=apiOperation){
            if(!apiOperation.notes().isEmpty()){
                api.setDescription(apiOperation.notes());
            }
        }
        for(APIParameter apiParameter:api.apiParameterList){
            ApiParam apiParam = apiParameter.parameter.getAnnotation(ApiParam.class);
            if(null!=apiParam){
                if(!apiParam.name().isEmpty()){
                    apiParameter.setName(apiParam.name());
                }
                if(!apiParam.value().isEmpty()){
                    apiParameter.setDescription(apiParam.value());
                }
            }
        }
};
quickAPIOption.apiEntityConsumer = (apiEntity)->{
    //从QuickDAO注解获取实体类信息
    Comment tableComment = (Comment) apiEntity.clazz.getAnnotation(Comment.class);
    if(null!=tableComment){
        apiEntity.setDescription(tableComment.value());
    }
    for(APIField apiField:apiEntity.apiFields){
        Comment fieldComment = apiField.field.getAnnotation(Comment.class);
        if (null!=fieldComment) {
            apiField.setDescription(fieldComment.value());
        }
        if (null != apiField.field.getAnnotation(Ignore.class)) {
            apiField.ignore = true;
        }
        Constraint constraint = apiField.field.getAnnotation(Constraint.class);
        if(null!=constraint){
            apiField.required = constraint.notNull();
        }
    }
    //从Swagger注解获取实体类信息
    ApiModel apiModel = (ApiModel) apiEntity.clazz.getAnnotation(ApiModel.class);
    if (null!=apiModel) {
        String description = apiModel.value().isEmpty()?apiModel.description():apiModel.value();
        if(!description.isEmpty()){
            apiEntity.setDescription(description);
        }
    }
    {
        for(APIField apiField:apiEntity.apiFields){
            ApiModelProperty apiModelProperty = apiField.field.getAnnotation(ApiModelProperty.class);
            if (null!=apiModelProperty) {
                apiField.setDescription(apiModelProperty.value());
            }
        }
    }
};
```

## 设置API参数处理

默认情况下QuickAPI只处理指定注解的API参数(例如@RequestParam,@RequestPart等),若您的项目中API参数有不支持的注解需要处理时,您可以设置此项.

```java
quickAPIOption.apiParameterFunction = ((api, parameter) -> {
 //根据api和参数信息,返回APIParameter对象
        APIParameter apiParameter = new APIParameter();
        //请求参数名称
        apiParameter.name = "xxx";
        //参数描述信息
        apiParameter.description = "xxxx";
        //参数位置
        apiParameter.position = "path";//取值为path,body
        //参数展示类型
        apiParameter.requestType = "text";//取值为text,file,textarea
        //注解参数名称,可选
        apiParameter.annotationParameterName = "xxx";
        //是否必填,默认为false
        apiParameter.required = false;
        //默认值
        apiParameter.defaultValue = "xxx";
        //参数类名
        apiParameter.typeClassName = parameter.getType().getName();
        //参数名称
        apiParameter.parameterName = parameter.getName();
        return apiParameter;
});
```

## 设置JavaDoc提取配置

默认情况QuickAPI将javadoc的整个注释信息作为描述API的描述信息,但是某些情况下可能不符合项目需求.

例如项目采用了@Description标签作为描述信息,您就可以设置此项来改变默认行为.

```java
//采用Description标签作为api描述信息
quickAPIOption.methodDocConsumer = ((methodDoc, api) -> {
    Tag[] tags = methodDoc.tags("Description");
    if(null==tags||tags.length==0){
        api.displayName = methodDoc.commentText();
        if(null==api.displayName||api.displayName.isEmpty()){
            api.displayName = api.methodName;
        }
        return;
    }
    api.displayName = tags[0].text();
    api.description = methodDoc.commentText();
});
```