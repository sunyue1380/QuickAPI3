package cn.schoolwow.quickapi.domain;

public class APIException {
    /**
     * 抛出的异常类名称
     */
    public String className;

    /**
     * 异常描述
     */
    public String description;

    @Override
    public String toString() {
        return "\n{\n" +
                "抛出的异常类名称:" + className + "\n" +
                "异常描述:" + description + "\n" +
                "}\n";
    }
}
