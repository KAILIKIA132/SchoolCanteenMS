package com.zk.util;

import com.zk.pushsdk.util.Constants;

public class ConfigUtil extends XMLUtil{

    private static ConfigUtil instance=new ConfigUtil();

    public static ConfigUtil getInstance(){
        if(instance==null)
            instance = new ConfigUtil();
        return instance;
    }

    private ConfigUtil() {
        super(getRealPath()+Constants.CONFIG_FILE_NAME);
    }

    private static String getRealPath(){
        String path = ConfigUtil.class.getResource("").toString();
        path = path.substring(0, path.lastIndexOf("/com/zk/util") +1 );
        return path;
    }
}
