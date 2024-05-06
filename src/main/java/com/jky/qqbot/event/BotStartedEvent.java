package com.jky.qqbot.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.time.Clock;

public class BotStartedEvent extends ApplicationEvent {
    public BotStartedEvent(Object source) {
        super(source);
    }

    public BotStartedEvent(Object source, Clock clock) {
        super(source, clock);
    }
}
