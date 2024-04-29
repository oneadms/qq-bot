package com.jky.qqbot.common.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户类型
 * @author oneadm
 */
@Getter
@AllArgsConstructor
public enum UserType {
    //微信
    WECHAT("Wechat"),
    //QQ
    QQ("QQ");
    private String val;
    public static Map<String, UserType> cacheMap;
    static {
        cacheMap = Arrays.stream(UserType.values()).collect(Collectors.toMap(UserType::getVal, Function.identity()));
    }

    public static UserType of(String val) {
        return cacheMap.get(val);
    }

}
