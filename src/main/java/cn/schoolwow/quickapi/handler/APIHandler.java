package cn.schoolwow.quickapi.handler;

import cn.schoolwow.quickapi.domain.*;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * API方法处理器
 * */
public class APIHandler extends AbstractHandler{

    public APIHandler(QuickAPIOption option) {
        super(option);
    }

    @Override
    public Handler handle(APIDocument apiDocument) throws Exception {
        for(APIController apiController:apiDocument.apiControllerList){
            getAPI(apiController);
        }
        return new APIParameterHandler(option);
    }

    /**
     * 提取api列表
     * */
    private void getAPI(APIController apiController){
        //获取baseUrl
        String baseUrl = getBaseUrl(apiController);

        //获取api
        List<API> apiList = new ArrayList<>();
        for(Method method: apiController.clazz.getDeclaredMethods()){
            API api = getMethodMappingAnnotation(method);
            if(null==api){
                api = getRequestMappingAnnotation(method);
            }
            if(null!=api){
                api.url = baseUrl + api.url;
                getAPI(api,method);
                apiList.add(api);
            }
        }
        apiController.apiList = apiList;
    }

    /**
     * 提取API信息
     * */
    private void getAPI(API api, Method method){
        api.methodName = method.getName();
        if(null!=method.getAnnotation(Deprecated.class)){
            api.deprecated = true;
        }
        api.returnClassName = method.getReturnType().getName();
        Type type = method.getGenericReturnType();
        if (type instanceof ParameterizedType) {
            api.returnClassName = method.getGenericReturnType().getTypeName();
        }
        //获取抛出异常
        Class[] exceptionTypes = method.getExceptionTypes();
        api.apiExceptions = new APIException[exceptionTypes.length];
        for(int i=0;i<exceptionTypes.length;i++){
            APIException apiException = new APIException();
            apiException.className = exceptionTypes[i].getName();
            api.apiExceptions[i] = apiException;
        }
        api.method = method;
    }

    /**
     * 获取RequestMapping注解修饰的方法
     * */
    private API getRequestMappingAnnotation(Method method){
        RequestMapping methodRequestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        if(null==methodRequestMapping){
            return null;
        }
        API api = new API();
        RequestMethod[] requestMethods = methodRequestMapping.method();
        if(requestMethods.length>0){
            api.requestMethod = requestMethods[0].name().toUpperCase();
        }
        if(methodRequestMapping.value().length>0){
            api.url = methodRequestMapping.value()[0];
        }else{
            api.url = method.getName();
        }
        if(api.url.charAt(0)!='/'){
            api.url = "/" + api.url;
        }
        return api;
    }

    /**
     * 获取getMapping等注解修饰的方法
     * */
    private API getMethodMappingAnnotation(Method method){
        Class[] mappingClasses = new Class[]{GetMapping.class, PostMapping.class, PutMapping.class, DeleteMapping.class,PatchMapping.class};
        for(Class mappingClass:mappingClasses) {
            Annotation annotation = method.getDeclaredAnnotation(mappingClass);
            if(annotation == null){
                continue;
            }
            //存在mapping注解
            String requestMethod = mappingClass.getSimpleName().substring(0,mappingClass.getSimpleName().lastIndexOf("Mapping")).toUpperCase();
            API api = new API();
            api.requestMethod = requestMethod.toUpperCase();
            try {
                String[] values = (String[]) mappingClass.getDeclaredMethod("value").invoke(annotation);
                api.url = values[0];
                if (api.url.charAt(0) != '/') {
                    api.url = "/" + api.url;
                }
            }catch (Exception e){
                continue;
            }
            return api;
        }
        return null;
    }

    /**
     * 获取基础路径
     * */
    private String getBaseUrl(APIController apiController){
        String baseUrl = "";
        RequestMapping classRequestMapping = (RequestMapping) apiController.clazz.getDeclaredAnnotation(RequestMapping.class);
        if(null!=classRequestMapping){
            if(classRequestMapping.value().length>0){
                baseUrl = classRequestMapping.value()[0];
            }else{
                baseUrl = apiController.clazz.getSimpleName().toLowerCase();
            }
            if(baseUrl.charAt(0)!='/'){
                baseUrl = "/"+baseUrl;
            }
        }
        return baseUrl;
    }
}