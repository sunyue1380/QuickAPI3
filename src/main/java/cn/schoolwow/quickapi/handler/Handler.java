package cn.schoolwow.quickapi.handler;

import cn.schoolwow.quickapi.domain.APIDocument;

/**
 * 责任链模式-处理器
 * */
public interface Handler {
    /**
     * 处理APIDocument对象
     * @param apiDocument API文档对象
     * @return 下一个处理器
     * */
    Handler handle(APIDocument apiDocument) throws Exception;
}
