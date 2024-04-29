package com.jky.qqbot.common.config;


import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.HashMap;
import java.util.Map;

public class ConfigCenter {


    private static Map<String,String> entityMap =new HashMap(){{
        put("mdDic", "t_md_dictonary");
    }};
    private static Map<String, String> metaDataMap=new HashMap(){{
        put("mdDic", "dic_key,dic_val,dic_name");
    }};

    public static String getEntityName(String cacheName) {
        return entityMap.get(cacheName);
    }

    public static String getMetaData(String cacheName) {
        return metaDataMap.get(cacheName);
    }



}
