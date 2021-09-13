package cn.schoolwow.quickapi.handler;

import cn.schoolwow.quickapi.domain.APIController;
import cn.schoolwow.quickapi.domain.APIDocument;
import cn.schoolwow.quickapi.domain.APIEntity;
import cn.schoolwow.quickapi.domain.QuickAPIOption;

/**
 * 用户自定义处理器
 * */
public class ConsumerHandler extends AbstractHandler{

    public ConsumerHandler(QuickAPIOption option) {
        super(option);
    }

    @Override
    public Handler handle(APIDocument apiDocument) throws Exception {
        if(null!=option.apiControllerConsumer){
            for(APIController apiController:apiDocument.apiControllerList){
                option.apiControllerConsumer.accept(apiController);
            }
        }
        if(null!=option.apiEntityConsumer){
            for(APIEntity apiEntity:apiDocument.apiEntityMap.values()){
                option.apiEntityConsumer.accept(apiEntity);
            }
        }
        return null;
    }
}
