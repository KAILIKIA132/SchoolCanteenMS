package com.zk.util;

import com.zk.pushsdk.util.Constants;
import org.apache.log4j.Logger;

public class ConfigUtil extends XMLUtil{

    private static Logger logger = Logger.getLogger(ConfigUtil.class);
    private static ConfigUtil instance=new ConfigUtil();

    public static ConfigUtil getInstance(){
        if(instance==null)
            instance = new ConfigUtil();
        return instance;
    }

    private ConfigUtil() {
        super(getRealPath()+Constants.CONFIG_FILE_NAME);
        String configPath = getRealPath()+Constants.CONFIG_FILE_NAME;
        logger.info("Loading config from: " + configPath);
        // Verify config was loaded
        if (getRoot() == null) {
            logger.error("Failed to load config.xml from: " + configPath);
        } else {
            logger.info("Config.xml loaded successfully");
        }
    }

    private static String getRealPath(){
        String path = ConfigUtil.class.getResource("").toString();
        // Handle file:// URLs
        if (path.startsWith("file:")) {
            path = path.substring(5); // Remove "file:" prefix
        }
        // Handle jar:file:// URLs
        if (path.startsWith("jar:file:")) {
            path = path.substring(9); // Remove "jar:file:" prefix
            int exclamation = path.indexOf("!");
            if (exclamation > 0) {
                path = path.substring(0, exclamation);
            }
        }
        path = path.substring(0, path.lastIndexOf("/com/zk/util") + 1);
        return path;
    }
}
