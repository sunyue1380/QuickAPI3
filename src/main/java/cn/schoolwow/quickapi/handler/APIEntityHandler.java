package cn.schoolwow.quickapi.handler;

import cn.schoolwow.quickapi.domain.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * API实体类处理器
 * */
public class APIEntityHandler extends AbstractHandler{
    private Logger logger = LoggerFactory.getLogger(APIEntityHandler.class);

    public APIEntityHandler(QuickAPIOption option) {
        super(option);
    }

    @Override
    public Handler handle(APIDocument apiDocument) throws Exception {
        if(null!=option.apiEntityPredicate){
            for(APIController apiController:apiDocument.apiControllerList){
                for(API api:apiController.apiList){
                    //添加API参数实体类信息
                    for(APIParameter apiParameter: api.apiParameterList){
                        api.parameterEntityNameList.addAll(getRecycleAPIEntity(apiParameter.parameter.getType(),apiDocument));
                        api.parameterEntityNameList.addAll(getRecycleAPIEntity(getGenericTypeClass(apiParameter.parameter.getParameterizedType()),apiDocument));
                    }
                    //添加API返回值实体类信息
                    api.returnEntityNameList.addAll(getRecycleAPIEntity(api.method.getReturnType(),apiDocument));
                    api.returnEntityNameList.addAll(getRecycleAPIEntity(getGenericTypeClass(api.method.getGenericReturnType()),apiDocument));
                }
            }
        }else{
            logger.warn("[未扫描实体类信息]若要扫描实体类,请设置apiEntityPredicate属性!");
        }
        return new JavaDocHandler(option);
    }

    /**
     * 获取所有相关实体类信息
     * */
    private List<String> getRecycleAPIEntity(Class rootClazz, APIDocument apiDocument) throws ClassNotFoundException {
        List<String> classNameList = new ArrayList<>();
        if(null==rootClazz){
            return classNameList;
        }
        Stack<Class> apiEntityClassStack = new Stack();
        apiEntityClassStack.push(rootClazz);
        while(!apiEntityClassStack.isEmpty()){
            Class clazz = apiEntityClassStack.pop();
            if(!option.apiEntityPredicate.test(clazz)){
                continue;
            }
            APIEntity apiEntity = getAPIEntity(clazz);
            classNameList.add(apiEntity.className);
            if(!apiDocument.apiEntityMap.containsKey(apiEntity.className)){
                apiDocument.apiEntityMap.put(apiEntity.className,apiEntity);
            }
            for(APIField apiField:apiEntity.apiFields){
                apiEntityClassStack.push(apiField.field.getType());
                if(apiField.className.contains("<")){
                    String genericTypeName = apiField.className.substring(apiField.className.indexOf("<")+1,apiField.className.lastIndexOf(">"));
                    apiEntityClassStack.push(ClassLoader.getSystemClassLoader().loadClass(genericTypeName));
                }
            }
        }
        return classNameList;
    }

    /**提取APIEntity*/
    private APIEntity getAPIEntity(Class clazz) {
        if(null!=clazz.getComponentType()){
            clazz = clazz.getComponentType();
        }

        APIEntity apiEntity = new APIEntity();
        apiEntity.clazz = clazz;
        apiEntity.className = clazz.getName();
        apiEntity.simpleName = clazz.getSimpleName();

        Field[] fields = getAllField(clazz);
        APIField[] apiFields = new APIField[fields.length];
        for (int i = 0; i < fields.length; i++) {
            APIField apiField = new APIField();
            apiField.field = fields[i];
            apiField.name = fields[i].getName();
            apiField.className = fields[i].getType().getName();
            //处理泛型
            Type fieldType = fields[i].getGenericType();
            if (fieldType instanceof ParameterizedType) {
                ParameterizedType pType = (ParameterizedType) fieldType;
                Type genericType = pType.getActualTypeArguments()[0];
                apiField.className += "<" + genericType.getTypeName() + ">";
            }
            apiFields[i] = apiField;
        }
        apiEntity.apiFields = apiFields;
        try {
            apiEntity.instance = JSON.toJSONString(clazz.newInstance(), SerializerFeature.WriteMapNullValue);
        } catch (Exception e) {
            logger.warn("[实例化失败]原因:{},类名:{}",e.getMessage(),clazz.getName());
        }
        return apiEntity;
    }

    /**
     * 获得该类所有字段(包括父类字段)
     * @param clazz 类
     * */
    private Field[] getAllField(Class clazz){
        List<Field> fieldList = new ArrayList<>();
        Class tempClass = clazz;
        while (null != tempClass) {
            Field[] fields = tempClass.getDeclaredFields();
            //排除静态变量和常量
            Field.setAccessible(fields, true);
            for (Field field : fields) {
                if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                field.setAccessible(true);
                fieldList.add(field);
            }
            tempClass = tempClass.getSuperclass();
        }
        Field[] fields = fieldList.toArray(new Field[0]);
        Field.setAccessible(fields,true);
        return fields;
    }

    /**
     * 获取泛型类对象
     * @param type 泛型类型
     * */
    private Class getGenericTypeClass(Type type) throws ClassNotFoundException {
        //处理泛型
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Type genericType = pType.getActualTypeArguments()[0];
            return ClassLoader.getSystemClassLoader().loadClass(genericType.getTypeName());
        }
        return null;
    }
}