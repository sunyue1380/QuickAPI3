package cn.schoolwow.quickapi.controller;

import cn.schoolwow.quickapi.domain.APIDocument;
import cn.schoolwow.quickapi.domain.QuickAPIOption;
import cn.schoolwow.quickapi.handler.APIControllerHandler;
import cn.schoolwow.quickapi.handler.Handler;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quickapi")
public class QuickAPIController {
    @Autowired(required = false)
    private QuickAPIOption quickAPIOption;

    private volatile APIDocument apiDocument;

    /**
     * 解析并返回接口类信息
     * */
    @RequestMapping("/apiDocument.js")
    public synchronized String getApiDocument() throws Exception {
        if(null==quickAPIOption){
            quickAPIOption = new QuickAPIOption();
        }
        if(null==quickAPIOption.sourcePath){
            quickAPIOption.sourcePath = System.getProperty("user.dir") + "/src/main/java";
        }
        if(null==apiDocument){
            apiDocument = new APIDocument();
            apiDocument.title = quickAPIOption.title;
            apiDocument.description = quickAPIOption.description;
        }
        Handler handle = new APIControllerHandler(quickAPIOption);
        while(null!=handle){
            handle = handle.handle(apiDocument);
        }
        return "let apiDocument = " + JSON.toJSONString(apiDocument)+";";
    }
}