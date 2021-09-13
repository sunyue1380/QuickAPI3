package cn.schoolwow.quickapi.handler;

import cn.schoolwow.quickapi.domain.*;
import cn.schoolwow.quickapi.util.JavaDocUtil;
import com.sun.javadoc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * JavaDoc处理类
 * */
public class JavaDocHandler extends AbstractHandler{
    private Logger logger = LoggerFactory.getLogger(JavaDocHandler.class);

    public JavaDocHandler(QuickAPIOption option) {
        super(option);
    }

    @Override
    public Handler handle(APIDocument apiDocument) throws Exception {
        if(null!=option.sourcePath&&Files.exists(Paths.get(option.sourcePath))){
            ClassDoc[] classDocs = JavaDocUtil.getJavaDoc(apiDocument,option);
            handleAPIController(classDocs,apiDocument.apiControllerList);
            handleAPIEntity(classDocs,apiDocument);
        }else{
            logger.info("[无法读取源文件注释]源文件路径未设置或者路径不存在!源文件路径:{}",option.sourcePath);
        }
        for(APIController apiController:apiDocument.apiControllerList){
            apiController.setDisplayName(apiController.clazz.getSimpleName());
            for(API api:apiController.apiList){
                api.setDisplayName(api.methodName);
            }
        }
        return new ConsumerHandler(option);
    }

    /**
     * 处理APIController
     * @param classDocs javadoc注释
     * @param apiControllerList 控制器列表
     * */
    private void handleAPIController(ClassDoc[] classDocs, List<APIController> apiControllerList){
        for (APIController apiController : apiControllerList) {
            for (ClassDoc classDoc : classDocs) {
                //判断控制器类名是否匹配
                if (!apiController.className.equals(classDoc.qualifiedName())) {
                    continue;
                }
                apiController.setDisplayName(classDoc.commentText());
                apiController.setDescription(classDoc.commentText());
                //添加描述
                if(null!=option.classDocConsumer){
                    option.classDocConsumer.accept(classDoc,apiController);
                }
                //匹配方法
                MethodDoc[] methodDocs = classDoc.methods();
                for (API api : apiController.apiList) {
                    for (MethodDoc methodDoc : methodDocs) {
                        //方法名和参数类型,个数匹配才算匹配
                        if(!api.method.getName().equals(methodDoc.name())){
                            continue;
                        }
                        if(api.method.getParameterCount()!=methodDoc.parameters().length){
                            continue;
                        }
                        api.setDisplayName(methodDoc.commentText());
                        api.setDescription(methodDoc.commentText());
                        Tag[] authorTags = methodDoc.tags("author");
                        if (null != authorTags && authorTags.length > 0) {
                            api.author = authorTags[0].text();
                        }
                        Tag[] sinceTags = methodDoc.tags("since");
                        if (null != sinceTags && sinceTags.length > 0) {
                            api.since = sinceTags[0].text();
                        }
                        //获取参数信息
                        ParamTag[] paramTags = methodDoc.paramTags();
                        for (APIParameter apiParameter : api.apiParameterList) {
                            for (ParamTag paramTag : paramTags) {
                                if (null!=apiParameter.parameter&&apiParameter.parameterName.equals(paramTag.parameterName())) {
                                    apiParameter.setName(paramTag.parameterName());
                                    apiParameter.setDescription(paramTag.parameterComment());
                                    break;
                                }
                            }
                        }
                        //获取抛出异常信息
                        ThrowsTag[] throwsTags = methodDoc.throwsTags();
                        if (null != throwsTags && throwsTags.length > 0) {
                            for(APIException apiException:api.apiExceptions){
                                for (int i = 0; i < throwsTags.length; i++) {
                                    if(apiException.className.equalsIgnoreCase(throwsTags[i].exceptionName())){
                                        apiException.description = throwsTags[i].exceptionComment();
                                        break;
                                    }
                                }
                            }
                        }
                        if(null!=option.methodDocConsumer){
                            option.methodDocConsumer.accept(methodDoc,api);
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * 处理APIEntity
     * @param classDocs javadoc注释
     * @param apiDocument 文档对象
     * */
    private void handleAPIEntity(ClassDoc[] classDocs, APIDocument apiDocument){
        for (APIEntity apiEntity : apiDocument.apiEntityMap.values()) {
            for (ClassDoc classDoc : classDocs) {
                if (apiEntity.className.equals(classDoc.qualifiedName())) {
                    apiEntity.setDescription(classDoc.commentText());
                    Tag[] authorTags = classDoc.tags("author");
                    if (null != authorTags && authorTags.length > 0) {
                        apiEntity.author = authorTags[0].text();
                    }
                    Tag[] sinceTags = classDoc.tags("since");
                    if (null != sinceTags && sinceTags.length > 0) {
                        apiEntity.since = sinceTags[0].text();
                    }
                    for (APIField apiField : apiEntity.apiFields) {
                        for (FieldDoc fieldDoc : getAllFieldDoc(classDoc)) {
                            if (apiField.name.equals(fieldDoc.name())) {
                                apiField.setDescription(fieldDoc.commentText());
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    /**
     * 获得该类所有字段(包括父类字段)
     * @param classDoc 类文档
     * */
    public static FieldDoc[] getAllFieldDoc(ClassDoc classDoc){
        List<FieldDoc> fieldDocList = new ArrayList<>();
        ClassDoc tempClassDoc = classDoc;
        while (null != tempClassDoc) {
            FieldDoc[] fieldDocs = tempClassDoc.fields();
            //排除静态变量和常量
            for(FieldDoc fieldDoc:fieldDocs){
                if(Modifier.isFinal(fieldDoc.modifierSpecifier())||Modifier.isStatic(fieldDoc.modifierSpecifier())){
                    continue;
                }
                fieldDocList.add(fieldDoc);
            }
            tempClassDoc = tempClassDoc.superclass();
            if (null!=tempClassDoc&&"java.lang.Object".equals(tempClassDoc.qualifiedName())) {
                break;
            }
        }
        return fieldDocList.toArray(new FieldDoc[0]);
    }
}