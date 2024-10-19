package ua.cn.stu.plugin.minusoperation;

import ua.cn.stu.plugin.api.BinaryOperator;
import ua.cn.stu.plugin.api.Plugin;
import ua.cn.stu.plugin.api.UnaryOperator;

public class MinusPlugin implements Plugin, UnaryOperator {
    public static final String PLUGIN_NAME = "Minus operation plugin";
    @Override
    public void invoke() {
        System.out.println(PLUGIN_NAME + " loaded");
    }

    @Override
    public double calculateUnary(Double operand) {
        return 0;
    }
}