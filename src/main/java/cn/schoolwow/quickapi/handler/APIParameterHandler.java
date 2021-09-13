package cn.schoolwow.quickapi.handler;

import cn.schoolwow.quickapi.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理API参数
 * */
public class APIParameterHandler extends AbstractHandler{
    private Logger logger = LoggerFactory.getLogger(APIParameterHandler.class);

    public APIParameterHandler(QuickAPIOption option) {
        super(option);
    }

    @Override
    public Handler handle(APIDocument apiDocument) throws Exception {
        for(APIController apiController:apiDocument.apiControllerList){
            for(API api:apiController.apiList){
                getAPIParameter(api);
            }
        }
        return new APIEntityHandler(option);
    }

    /**
     * 提取api参数列表
     * */
    private void getAPIParameter(API api){
        //参数原始名称
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterRawNames = u.getParameterNames(api.method);
        //API参数
        List<APIParameter> apiParameterList = new ArrayList<>();
        //方法参数
        Parameter[] parameters = api.method.getParameters();
        for(int i=0;i<parameters.length;i++){
            Parameter parameter = parameters[i];
            APIParameter apiParameter = getByPathVariableAnnotation(api,parameter);
            if(null==apiParameter){
                apiParameter = getByRequestParamAnnotation(api,parameter);
            }
            if(null==apiParameter){
                apiParameter = getByRequestBodyAnnotation(api,parameter);
            }
            if(null==apiParameter){
                apiParameter = getByRequestPartAnnotation(api,parameter);
            }
            if(null==apiParameter){
                apiParameter = getByMultipartFile(api,parameter);
            }
            if(null!=option.apiParameterFunction){
                apiParameter = option.apiParameterFunction.apply(api,parameter);
            }
            if(null==apiParameter){
                //不支持处理该参数
                logger.debug("[不支持处理该参数]方法名:{},参数名:{}",api.method.getName(),parameter.getName());
                continue;
            }
            apiParameter.setName(apiParameter.annotationParameterName);
            apiParameter.setName(parameterRawNames[i]);
            apiParameter.parameterName = parameterRawNames[i];
            apiParameter.typeClassName = parameter.getType().getName();
            apiParameter.parameter = parameters[i];
            apiParameterList.add(apiParameter);
        }
        api.apiParameterList = apiParameterList;
    }

    /**
     * 通过RequestBody注解获取参数
     * */
    private APIParameter getByPathVariableAnnotation(API api, Parameter parameter){
        PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
        if(null==pathVariable){
            return null;
        }
        APIParameter apiParameter = new APIParameter();
        apiParameter.annotationParameterName = pathVariable.value();
        if(StringUtils.isEmpty(apiParameter.annotationParameterName)){
            apiParameter.annotationParameterName = pathVariable.name();
        }
        apiParameter.required = pathVariable.required();
        apiParameter.position = "path";
        return apiParameter;
    }

    /**
     * 通过RequestParam注解获取参数
     * */
    private APIParameter getByRequestParamAnnotation(API api, Parameter parameter){
        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
        if(null==requestParam){
            return null;
        }
        APIParameter apiParameter = new APIParameter();
        apiParameter.annotationParameterName = requestParam.value();
        if(StringUtils.isEmpty(apiParameter.annotationParameterName)){
            apiParameter.annotationParameterName = requestParam.name();
        }
        if(!requestParam.defaultValue().equals(ValueConstants.DEFAULT_NONE)){
            apiParameter.defaultValue = requestParam.defaultValue();
        }
        apiParameter.required = requestParam.required();
        apiParameter.position = "query";
        if("POST".equalsIgnoreCase(api.requestMethod)
                ||"PUT".equalsIgnoreCase(api.requestMethod)
                ||"PATCH".equalsIgnoreCase(api.requestMethod)
        ){
            apiParameter.position = "body";
        }
        return apiParameter;
    }

    /**
     * 通过RequestParam注解获取参数
     * */
    private APIParameter getByRequestPartAnnotation(API api, Parameter parameter){
        RequestPart requestPart = parameter.getAnnotation(RequestPart.class);
        if(null==requestPart){
            return null;
        }
        APIParameter apiParameter = new APIParameter();
        apiParameter.annotationParameterName = requestPart.value();
        if(StringUtils.isEmpty(apiParameter.annotationParameterName)){
            apiParameter.annotationParameterName = requestPart.name();
        }
        apiParameter.required = requestPart.required();
        apiParameter.requestType = "file";
        api.contentType = "multipart/form-data";
        return apiParameter;
    }

    /**
     * 通过RequestBody注解获取参数
     * */
    private APIParameter getByRequestBodyAnnotation(API api, Parameter parameter){
        RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
        if(null==requestBody){
            return null;
        }
        APIParameter apiParameter = new APIParameter();
        apiParameter.required = requestBody.required();
        apiParameter.position = "body";
        apiParameter.requestType = "textarea";
        api.contentType = "application/json";
        return apiParameter;
    }

    /**
     * 通过MultipartFile类型获取参数
     * */
    private APIParameter getByMultipartFile(API api, Parameter parameter){
        String multipartFileClassName = MultipartFile.class.getName();
        String parameterTypeClassName = parameter.getType().getName();
        if(multipartFileClassName.equalsIgnoreCase(parameterTypeClassName)
                ||(parameterTypeClassName.startsWith("[L")&&parameterTypeClassName.contains(multipartFileClassName))
        ){
            APIParameter apiParameter = new APIParameter();
            apiParameter.position = "body";
            apiParameter.requestType = "file";
            api.contentType = "multipart/form-data";
            return apiParameter;
        }
        return null;
    }
}