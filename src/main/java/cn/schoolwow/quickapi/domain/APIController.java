package cn.schoolwow.quickapi.domain;

import java.util.ArrayList;
import java.util.List;

public class APIController {
    /**
     * 显示名称
     */
    public String displayName;

    /**
     * 是否被废弃
     */
    public boolean deprecated;

    /**
     * 控制器类名
     */
    public String className;

    /**
     * 注释
     * */
    public String description;

    /**
     * 控制器类
     */
    public transient Class clazz;

    /**
     * 接口
     */
    public List<API> apiList = new ArrayList<>();

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

    @Override
    public String toString() {
        return "\n{\n" +
                "是否被废弃:" + deprecated + "\n" +
                "控制器类名:" + className + "\n" +
                "控制器类:" + clazz.getName() + "\n" +
                "接口:" + apiList + "\n" +
                "}\n";
    }
}
