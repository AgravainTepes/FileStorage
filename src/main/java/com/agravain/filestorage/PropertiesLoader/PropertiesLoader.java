package com.agravain.filestorage.PropertiesLoader;


import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PropertiesLoader {
    public PropertiesLoader() {
        Properties properties = loadProperties();
        this.types =
                loadTypesList(properties, "types.array");
        this.maxSize =
                loadMaxSize(properties, "max.file.size");
    }

    private static String propertiesPath = "src/main/resources/FileTypesAndSize.properties";
    private final ArrayList<String> types;
    private final static int megabyteToByte = 1048576;
    private final static int kilobyteToByte = 1024;
    private final long maxSize;

    private static Properties loadProperties() {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(propertiesPath));
        } catch (IOException e) {
        }
        return properties;
    }

    private static ArrayList<String> loadTypesList(Properties properties, String key) {
        String allInOne = properties.getProperty(key);
        ArrayList<String> types = new ArrayList<>();
        Pattern pattern = Pattern.compile("[[.]\\w+]+");
        Matcher matcher = pattern.matcher(allInOne);
        for (int i = 0; matcher.find(); i++) {
            types.add(matcher.group().trim());
        }
        return types;
    }

    public long getMaxSize() {
        return maxSize;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    private static long loadMaxSize(Properties properties, String key) {
        String stringMaxSize = properties.getProperty(key);
        CharSequence sequenceMaxSize = stringMaxSize;
        Long result = 0L;
        if (sequenceMaxSize.length() >= 3) {
            Long longMaxSize = Long
                    .parseLong(sequenceMaxSize, 0,
                            sequenceMaxSize.length() - 3,
                            10);
            if (stringMaxSize.endsWith("MB"))
                result = longMaxSize * megabyteToByte;
            if (stringMaxSize.endsWith("KB"))
                result = longMaxSize * kilobyteToByte;
        }
        return result;
    }
}