package ua.cn.stu.plugin.feet;

import ua.cn.stu.plugin.api.Plugin;
import ua.cn.stu.plugin.api.UnaryOperator;

public class FeetPlugin implements Plugin, UnaryOperator {
    public static final String PLUGIN_NAME = "Feet operation plugin";
    @Override
    public void invoke() {
        System.out.println(PLUGIN_NAME + "loaded");
    }

    @Override
    public double calculateUnary(Double operand) {
        return 0.3048 * operand;
    }
}
