package com.agravain.filestorage.PropertiesLoader;


import com.agravain.filestorage.Exceptions.FileExceptions.PropertiesLoadFailException;
import org.springframework.stereotype.Component;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

@Component
public class PropertiesLoader {

    public PropertiesLoader() {
        Properties properties = loadProperties();
        this.typeList =
                loadTypesList(properties);
        this.maxSize =
                loadMaxSize(properties);
    }

    private final List<String> typeList;

    private final static int megabyteToByte = 1048576;

    private final static int kilobyteToByte = 1024;

    private final long maxSize;

    private static Properties loadProperties() {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(
                    "src/main/resources/FileTypesAndSize.properties"));
        }
        catch (IOException e) {

            throw new
                    PropertiesLoadFailException("Properties loading collapsed!");
        }
        return properties;
    }

    private static List<String> loadTypesList(Properties properties) {

        Set<String> allInOne = properties.stringPropertyNames();

        List<String> typesList = new ArrayList<>();

        for (String potentialType : allInOne) {

            if (potentialType.startsWith("content.type")) {

                String contentType =
                        properties
                                .getProperty(potentialType);

                typesList
                        .add(contentType);
            }
        }

        return typesList;
    }


    private static long loadMaxSize(Properties properties) {

        String stringMaxSize = properties.getProperty("max.file.size");

        long result = 0L;

        if (stringMaxSize.length() >= 3 && stringMaxSize.endsWith("MB")
                || stringMaxSize.endsWith("KB")) {

            long longMaxSize = Long
                    .parseLong(stringMaxSize, 0,
                            stringMaxSize.length() - 2,
                            10);

            if (stringMaxSize.endsWith("MB"))
                result = longMaxSize * megabyteToByte;

            if (stringMaxSize.endsWith("KB"))
                result = longMaxSize * kilobyteToByte;
        }
        return result;
    }

    public long getMaxSize() {

        return maxSize;
    }

    public List<String> getTypeList() {

        return typeList;
    }
}