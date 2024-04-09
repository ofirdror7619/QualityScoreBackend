package org.qualityscore;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import util.ConfigurationGetter;

import java.util.HashMap;

@SpringBootApplication
public class Main {

    public static HashMap<String, String> configurationList;

    public static void main(String[] args) {
        String log4jConfPath = "src/main/java/config/log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
        configurationList = ConfigurationGetter.getPropsFromConf();
        SpringApplication.run(Main.class, args);
    }

}