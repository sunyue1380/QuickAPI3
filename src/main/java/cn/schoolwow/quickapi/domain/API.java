package cn.schoolwow.quickapi.domain;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class API {
    /**
     * 显示名称
     */
    public String displayName;

    /**
     * 方法名称
     */
    public String methodName;

    /**
     * 描述
     */
    public String description;

    /**
     * 是否被废弃
     */
    public boolean deprecated;

    /**
     * 请求方法
     */
    public String requestMethod = "POST";

    /**
     * 请求地址
     */
    public String url;

    /**
     * 作者
     */
    public String author;

    /**
     * 日期
     */
    public String since;

    /**
     * 请求编码
     */
    public String contentType = "application/x-www-form-urlencoded";

    /**
     * 请求参数
     */
    public List<APIParameter> apiParameterList = new ArrayList<APIParameter>();

    /**
     * 返回值类名
     */
    public String returnClassName;

    /**
     * 关联请求参数实体类
     */
    public List<String> parameterEntityNameList = new ArrayList<>();

    /**
     * 关联返回值实体类
     */
    public List<String> returnEntityNameList = new ArrayList<>();

    /**
     * 抛出异常
     */
    public APIException[] apiExceptions;

    /**
     * 方法
     */
    public transient Method method;

    /**
     * 设置显示名称
     * */
    public void setDisplayName(String displayName){
        if(null==this.displayName&&null!=displayName&&!displayName.isEmpty()){
            this.displayName = displayName;
        }
    }

    /**
     * 设置描述
     * */
    public void setDescription(String description){
        if(null==this.description&&null!=description&&!description.isEmpty()){
            this.description = description;
        }
    }
}