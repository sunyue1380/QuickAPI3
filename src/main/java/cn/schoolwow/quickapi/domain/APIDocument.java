package cn.schoolwow.quickapi.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIDocument {
    /**
     * 文档标题
     */
    public String title = "QuickAPI";

    /**
     * 文档描述
     */
    public String description;

    /**
     * 生成时间
     */
    public LocalDateTime date = LocalDateTime.now();

    /**
     * 控制器
     */
    public List<APIController> apiControllerList = new ArrayList<>();

    /**
     * 实体类
     */
    public Map<String, APIEntity> apiEntityMap = new HashMap<>();

    /**
     * 更新历史记录
     */
    public List<APIHistory> apiHistoryList = new ArrayList<>();

    @Override
    public String toString() {
        return "\n{\n" +
                "文档标题:" + title + "\n" +
//                "文档描述:" + description + "\n" +
                "生成时间:" + date + "\n" +
                "控制器:" + apiControllerList + "\n" +
                "实体类:" + apiEntityMap + "\n" +
                "更新历史记录:" + apiHistoryList + "\n" +
                "}\n";
    }
}
