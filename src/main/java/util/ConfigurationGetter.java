package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class ConfigurationGetter {

    public static HashMap<String, String> getPropsFromConf() {
        HashMap<String, String> propsList = new HashMap<>();
        try {
            String configFilePath = "src/main/java/config/config.properties";
            FileInputStream propsInput = new FileInputStream(configFilePath);
            Properties prop = new Properties();
            prop.load(propsInput);
            propsList.put("URL", prop.getProperty("URL"));
            propsList.put("cacheTTL", prop.getProperty("cacheTTL"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return propsList;
    }
}
