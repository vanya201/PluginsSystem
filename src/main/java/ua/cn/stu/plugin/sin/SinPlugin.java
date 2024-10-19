package ua.cn.stu.plugin.sin;

import ua.cn.stu.plugin.api.Plugin;
import ua.cn.stu.plugin.api.UnaryOperator;

public class SinPlugin implements Plugin, UnaryOperator {
    public static final String PLUGIN_NAME = "Sin operation plugin";
    @Override
    public void invoke() {
        System.out.println(PLUGIN_NAME + "loaded");
    }

    @Override
    public double calculateUnary(Double operand) {
        return Math.sin(operand);
    }
}
