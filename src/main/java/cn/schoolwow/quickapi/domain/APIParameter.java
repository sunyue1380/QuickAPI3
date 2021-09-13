package cn.schoolwow.quickapi.domain;

import java.lang.reflect.Parameter;

/**
 * 请求参数
 */
public class APIParameter {
    /**
     * 名称
     * */
    public String name;

    /**
     * 参数名称
     */
    public String parameterName;

    /**
     * 注解参数名称
     */
    public String annotationParameterName;

    /**
     * 描述
     */
    public String description;

    /**
     * 参数类型类名
     */
    public String typeClassName;

    /**
     * 参数请求类型(text,textarea,file)
     */
    public String requestType = "text";

    /**
     * 参数位置(query,body)
     */
    public String position = "query";

    /**
     * 是否必须
     */
    public boolean required = true;

    /**
     * 默认值
     */
    public String defaultValue = "";

    /**
     * 参数
     */
    public transient Parameter parameter;

    /**
     * 设置名称
     * */
    public void setName(String name){
        if(null!=name&&!name.isEmpty()){
            this.name = name;
        }
    }

    /**
     * 设置描述
     * */
    public void setDescription(String description){
        if(null!=description&&!description.isEmpty()){
            this.description = description.replace("\"","'");
        }
    }
}
