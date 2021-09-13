package cn.schoolwow.quickapi.domain;

import java.lang.reflect.Field;

public class APIField {
    /**
     * 字段名
     */
    public String name;

    /**
     * 描述
     */
    public String description;

    /**
     * 字段类型
     */
    public String className;

    /**
     * 是否忽略
     */
    public boolean ignore;

    /**
     * 是否必须
     */
    public boolean required;

    /**
     * 字段
     */
    public transient Field field;

    /**
     * 添加描述
     * */
    public void setDescription(String description){
        if(null!=description&&!description.isEmpty()){
            this.description = description;
        }
    }
}