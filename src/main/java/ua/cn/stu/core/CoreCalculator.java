package ua.cn.stu.core;

import ua.cn.stu.plugin.Impl.OperatorType;
import ua.cn.stu.plugin.Impl.PluginInfo;
import ua.cn.stu.plugin.api.PluginAPI;
import ua.cn.stu.plugin.Impl.PluginApiImpl;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoreCalculator {
    private static final String PLUGIN_DIR = "plugins";

    public static void main(String[] args) throws IOException,
            InstantiationException, IllegalAccessException, URISyntaxException,
            NoSuchMethodException, SecurityException, IllegalArgumentException,
            InvocationTargetException {

        PluginAPI pluginApi = new PluginApiImpl(PLUGIN_DIR);
        String input = null;
        while (!"exit".equalsIgnoreCase(input)) {
            System.out.println("Please enter expression or type exit >>>");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            input = reader.readLine();

            if ("exit".equalsIgnoreCase(input))
                return;

            boolean isMatchToOperation = false;
            for (String operation : pluginApi.getOperations())
            {
                if (input.contains(operation))
                {

                    PluginInfo plinfo = pluginApi.getPluginInfo(operation);
                    if (plinfo.getOperatorType().equals(OperatorType.BINARY))
                    {
                        String regex = "(\\d+(\\.\\d+)?)(\\s*([+\\-*/])\\s*)(\\d+(\\.\\d+)?)";
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(input);

                        if (matcher.matches())
                        {
                            isMatchToOperation = true;
                            double firstParameter = Double.parseDouble(matcher.group(1));
                            String operator = matcher.group(4);
                            double secondParameter = Double.parseDouble(matcher.group(5));
                            Double result = (Double) pluginApi.execute(operator, firstParameter, secondParameter);

                            System.out.println("The result of operation " + result);
                        }
                    }
                    if (plinfo.getOperatorType().equals(OperatorType.UNARY))
                    {
                        String regex = "\\s+";
                        String[] parts = input.split(regex);

                        if(parts.length > 1)
                        {
                            isMatchToOperation = true;
                            String operator = parts[0];
                            double firstParameter = Double.parseDouble(parts[1]);
                            Double result = (Double) pluginApi.execute(operator, firstParameter);
                            System.out.println("The result of operation " + result);
                        }
                    }
                }
            }
            if (!isMatchToOperation) {
                System.out.println("Operation is not supported");
            }
        }
    }
}
