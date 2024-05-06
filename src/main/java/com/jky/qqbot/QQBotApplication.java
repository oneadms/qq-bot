package com.jky.qqbot;

import com.jky.qqbot.event.BotStartedEvent;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan("com.jky.qqbot.mapper")
@EnableAsync

public class QQBotApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(QQBotApplication.class, args);
        applicationContext.publishEvent(new BotStartedEvent("机器人初始化完成"));
    }


}
