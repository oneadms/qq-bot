package com.jky.qqbot.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.mamoe.mirai.utils.BotConfiguration;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum ProtocolType {

    ANDROID_WATCH("android_watch") {
        @Override
        public BotConfiguration.MiraiProtocol getProtocol() {
            return BotConfiguration.MiraiProtocol.ANDROID_WATCH;
        }
    },
    MAC_OS("macos") {
        @Override
        public BotConfiguration.MiraiProtocol getProtocol() {
            return BotConfiguration.MiraiProtocol.MACOS;
        }
    },
    IPAD("ipad") {
        @Override
        public BotConfiguration.MiraiProtocol getProtocol() {
            return BotConfiguration.MiraiProtocol.IPAD;
        }
    },
    ANDROID_PHONE("android_phone") {
        @Override
        public BotConfiguration.MiraiProtocol getProtocol() {
            return BotConfiguration.MiraiProtocol.ANDROID_PHONE;
        }
    };
    String val;

    private static Map<String, ProtocolType> cacheMap;
    static {
        cacheMap = Arrays.stream(ProtocolType.values()).collect(Collectors.toMap(ProtocolType::getVal, Function.identity()));
    }

    public static ProtocolType of(String type) {
        return cacheMap.get(type);
    }


    public abstract net.mamoe.mirai.utils.BotConfiguration.MiraiProtocol getProtocol();
}
