package com.jky.qqbot.init;

import com.github.yulichang.toolkit.SpringContentUtils;
import com.jky.qqbot.event.BotStartedEvent;
import com.jky.qqbot.listener.BotProcessListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class QQBotInit implements ApplicationListener<BotStartedEvent>  {

    @Override
    public void onApplicationEvent(BotStartedEvent event) {
        BotProcessListener botProcessListener = SpringContentUtils.getBean(BotProcessListener.class);
        log.info(event.getSource().toString());
        CompletableFuture.runAsync(botProcessListener, Executors.newFixedThreadPool(1));
    }


}
