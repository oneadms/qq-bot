package com.jky.qqbot.bean;

import com.jky.qqbot.common.config.QQBotProperties;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.auth.BotAuthorization;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.beans.factory.FactoryBean;

public class BotFactoryBean implements FactoryBean<Bot> {
   private QQBotProperties qqBotProperties;
   private BotConfiguration botConfiguration;
    public static Boolean flag = true;

    public BotFactoryBean(QQBotProperties qqBotProperties, BotConfiguration botConfiguration) {
        this.qqBotProperties = qqBotProperties;
        this.botConfiguration = botConfiguration;
    }

    @Override
    public Bot getObject() {
        if (flag) {

            return BotFactory.INSTANCE.newBot(qqBotProperties.getUsername(),
                    BotAuthorization.byPassword(qqBotProperties.getPassword()),
                    botConfiguration);
        }
        return  BotFactory.INSTANCE.newBot(qqBotProperties.getUsername(),
                BotAuthorization.byQRCode(),
                botConfiguration);
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public Class<?> getObjectType() {
        return Bot.class;
    }
}
