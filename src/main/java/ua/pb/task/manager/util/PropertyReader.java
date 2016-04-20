package ua.pb.task.manager.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class PropertyReader {
    public static Properties getProperties(String path) {
        //Resource resource = new ClassPathResource(path);
        InputStreamReader stream = null;
        try {
            stream = new InputStreamReader(PropertyReader.class.getClassLoader().getResourceAsStream(path), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Properties props = new Properties();
        try {
            props.load(stream); //PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }

}