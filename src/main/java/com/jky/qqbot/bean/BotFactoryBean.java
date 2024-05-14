package com.jky.qqbot.bean;

import com.jky.qqbot.common.config.QQBotProperties;
import com.jky.qqbot.common.enums.LoginType;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.auth.BotAuthorization;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.beans.factory.FactoryBean;

import java.util.Objects;

public class BotFactoryBean implements FactoryBean<Bot> {
    private QQBotProperties qqBotProperties;
    private BotConfiguration botConfiguration;

    public BotFactoryBean(QQBotProperties qqBotProperties, BotConfiguration botConfiguration) {
        this.qqBotProperties = qqBotProperties;
        this.botConfiguration = botConfiguration;
    }

    @Override
    public Bot getObject() {
        if (Objects.equals(qqBotProperties.getLoginType(), LoginType.QRCODE)) {
            return BotFactory.INSTANCE.newBot(qqBotProperties.getUsername(),
                    BotAuthorization.byQRCode());
        }


        return BotFactory.INSTANCE.newBot(qqBotProperties.getUsername(),
                BotAuthorization.byPassword(qqBotProperties.getPassword()),
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
