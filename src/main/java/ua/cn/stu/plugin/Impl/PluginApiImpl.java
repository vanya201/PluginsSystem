package ua.cn.stu.plugin.Impl;


import ua.cn.stu.plugin.api.PluginAPI;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class PluginApiImpl implements PluginAPI {
    private Map<String, PluginInfo> plugins;
    PluginLoader loader;

    public PluginApiImpl(String url) throws RuntimeException {
        init(url);
    }

    private void init(String url) {
        loader = new PluginLoader(url);
        try {
            plugins = loader.loadPlugins();
            System.out.println("Supported operations:");
            for (PluginInfo pluginInfo : plugins.values())
                System.out.println(pluginInfo.getDescription());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private static Object executeMethod(Class<?> pluginClass, String methodName, Class<?>[] methodParameterTypes, Object[] methodArguments) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Method method = pluginClass.getMethod(methodName, methodParameterTypes);
        return method.invoke(pluginClass.newInstance(), methodArguments);
    }

    @Override
    public Object execute(String operation, Object... parametres) {
        try {
            Class<?>[] clazzes = Arrays.stream(parametres)
                    .map(Object::getClass)
                    .toArray(size -> new Class<?>[size]);
            return executeMethod(plugins.get(operation).getClassReference(), plugins.get(operation).getMethod(), clazzes, parametres);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Set<String> getOperations() {
        return plugins.keySet();
    }

    @Override
    public PluginInfo getPluginInfo(String operation) {
        return plugins.get(operation);
    }
}
