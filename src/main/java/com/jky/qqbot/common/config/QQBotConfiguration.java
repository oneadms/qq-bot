package com.jky.qqbot.common.config;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.auth.BotAuthorization;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@EnableConfigurationProperties(QQBotProperties.class)
public class QQBotConfiguration   {

    @Bean
    @Lazy
    public BotConfiguration botConfiguration(QQBotProperties qqBotProperties) {
        BotConfiguration botConfiguration = BotConfiguration.getDefault();
        botConfiguration.setProtocol(qqBotProperties.getType());
        return botConfiguration;
    }

    @Bean
    public Bot bot(QQBotProperties qqBotProperties,BotConfiguration botConfiguration) {
        return BotFactory.INSTANCE.newBot(qqBotProperties.getUsername(), BotAuthorization.Companion.byQRCode(), botConfiguration);
    }



}
