package cn.schoolwow.quickapi.handler;

import cn.schoolwow.quickapi.domain.QuickAPIOption;

public abstract class AbstractHandler implements Handler{
    protected QuickAPIOption option;

    public AbstractHandler(QuickAPIOption option) {
        this.option = option;
    }
}
