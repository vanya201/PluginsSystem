package ua.cn.stu.plugin.Impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class PluginLoader {

    private final String DESCRIPTION_PROPERTY = "description";
    private final String MAIN_METHOD = "method";
    private final String DESCRIPTOR_NAME_PART = "descriptor";
    private final String OPERATOR_PROPERTY = "operator";
    private final String TYPE_PROPERTY = "type";
    private final String MAIN_CLASS_PROPERTY = "main.class";
    private final String PROPERTIES_EXTENTION = ".properties";
    private final String JAR_EXTENTION = ".jar";
    private final String PLUGIN_DIR;

    public PluginLoader(String url) {
        PLUGIN_DIR = url;
    }

    private File[] getAllJarsFromPluginDir() {
        File pluginDir = new File(PLUGIN_DIR);
        return pluginDir.listFiles((File pathname) -> {
            return pathname.isFile() && pathname.getName().endsWith(JAR_EXTENTION);
        });
    }

    private  boolean isPluginClass(Class<?> pluginClass) {
        Class<?>[] implementedInterfaces = pluginClass.getInterfaces();
        return Arrays.stream(implementedInterfaces).anyMatch((Class<?> clazz) -> "ua.cn.stu.plugin.api.Plugin"
                .equalsIgnoreCase(clazz.getName()));
    }

    private Properties getProperties(URL jarURL) throws IOException {
        try (ZipInputStream zip = new ZipInputStream(jarURL.openStream())) {
            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                if (entry.getName().endsWith(DESCRIPTOR_NAME_PART + PROPERTIES_EXTENTION)) {
                    Properties properties = new Properties();
                    properties.load(zip);
                    return properties;
                }
            }
        }
        return null;
    }


    public Map<String, PluginInfo> loadPlugins() throws MalformedURLException, URISyntaxException, InvocationTargetException, NoSuchMethodException {
        File[] jars = getAllJarsFromPluginDir();
        return loadPlugins(jars);
    }


    private  Map<String, PluginInfo> loadPlugins(File[] jars) {
        Map<String, PluginInfo> pluginClasses = new HashMap<String, PluginInfo>();

        for (File jar : jars) {

            try {
                URL jarURL = jar.toURI().toURL();
                PluginClassLoader pluginClassloader = new PluginClassLoader(jar.getPath());
                Properties properties = getProperties(jarURL);
                if(properties == null)
                    return null;
                String className = properties.getProperty(MAIN_CLASS_PROPERTY);
                String operationType = properties.getProperty(TYPE_PROPERTY);
                String operator = properties.getProperty(OPERATOR_PROPERTY);
                String description = properties.getProperty(DESCRIPTION_PROPERTY);
                String method = properties.getProperty(MAIN_METHOD);
                Class<?> pluginClass = Class
                        .forName(className, false, pluginClassloader);

                boolean isPlugin = isPluginClass(pluginClass);
                if (isPlugin && operationType != null && operator != null
                        && description != null) {

                    PluginInfo pluginInfo = new PluginInfo();
                    pluginInfo.setClassReference(pluginClass);
                    if (OperatorType.UNARY.getOperatorType()
                            .equalsIgnoreCase(operationType)) {
                        pluginInfo.setOperatorType(OperatorType.UNARY);
                    } else if (OperatorType.BINARY.getOperatorType()
                            .equalsIgnoreCase(operationType)) {
                        pluginInfo.setOperatorType(OperatorType.BINARY);
                    }
                    pluginInfo.setOperator(operator);
                    pluginInfo.setDescription(description);
                    pluginInfo.setMethod(method);
                    pluginClasses.put(pluginInfo.getOperator().toString(), pluginInfo);
                }
            } catch (Exception ex) {
                return null;
            }
        }
        return pluginClasses;
    }
}
