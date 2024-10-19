package ua.cn.stu.plugin.api;

import ua.cn.stu.plugin.Impl.PluginInfo;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public interface PluginAPI {
    Object execute(String operation, Object... parametres);
    Set<String> getOperations();
    PluginInfo getPluginInfo(String operation);
}

