package com.jky.qqbot.common.config;

import com.jky.qqbot.bean.BotFactoryBean;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.auth.BotAuthorization;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.DeviceInfo;
import net.mamoe.mirai.utils.DeviceInfoBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@EnableConfigurationProperties(QQBotProperties.class)
@Slf4j
public class QQBotConfiguration   {

    @Bean
    public BotConfiguration botConfiguration(QQBotProperties qqBotProperties) {
        BotConfiguration botConfiguration = BotConfiguration.getDefault();
        botConfiguration.setProtocol(qqBotProperties.getType());
        botConfiguration.setDeviceInfo(bot -> DeviceInfo.random());
        return botConfiguration;
    }

    @Bean
    public BotFactoryBean botFactoryBean(QQBotProperties qqBotProperties,BotConfiguration botConfiguration) {
        log.info("QQ Bot Config:{}",qqBotProperties.toString());
        return new BotFactoryBean(qqBotProperties,botConfiguration);
    }



}
