package com.jky.qqbot.init;

import com.jky.qqbot.service.md.MdCacheService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Async
public class CacheInit implements ApplicationListener<ApplicationReadyEvent> {
    MdCacheService mdCacheService;

    public CacheInit(MdCacheService mdCacheService) {
        this.mdCacheService = mdCacheService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        mdCacheService.initCache();
    }
}
