package be.howest.ti.mars.logic.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {

    private static final String CONFIG_FILE = "/config/config.properties";
    private static final Config INSTANCE = new Config();
    private Properties properties = new Properties();
    private static final Logger LOGGER = Logger.getLogger(Config.class.getName());

    private Config(){
        try(InputStream ris = getClass().getResourceAsStream(CONFIG_FILE)){
            properties.load(ris);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,"Unable to read config file");
            throw new NullPointerException("Unable to read config file");
        }
    }

    public String readSetting(String key, String defaultValue){
        return properties.getProperty(key,defaultValue);
    }

    public String readSetting(String key){
        return properties.getProperty(key,null);
    }

    public void writeSetting(String key, String value){
        properties.setProperty(key,value);
        storeSettingsToFile();
    }

    public static Config getInstance(){
        return INSTANCE;
    }

    private void storeSettingsToFile(){
        String path = getClass().getResource(CONFIG_FILE).getPath();
        try(FileOutputStream fos = new FileOutputStream(path)){
            properties.store(fos,null);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,"Unable to write config file");
            throw new NullPointerException("Unable to write config file");
        }
    }
}

