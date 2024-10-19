package ua.cn.stu.plugin.Impl;

public enum OperatorType {
    BINARY("binary"),
    UNARY("unary");

    private String operatorTypeString;

    OperatorType(String operatorTypeString) {
        this.operatorTypeString = operatorTypeString;
    }

    public String getOperatorType() {
        return operatorTypeString;
    }
}
