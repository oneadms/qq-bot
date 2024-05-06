package com.jky.qqbot.common.config;

import lombok.Data;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "qq.bot")
public class QQBotProperties {
    private Long username;
    private String password;
    private BotConfiguration.MiraiProtocol type;
    private String deviceInfo;
}
