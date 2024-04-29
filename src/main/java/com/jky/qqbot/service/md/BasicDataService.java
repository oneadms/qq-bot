package com.jky.qqbot.service.md;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BasicDataService {
    MdCacheService mdCacheService;

    public BasicDataService(MdCacheService mdCacheService) {
        this.mdCacheService = mdCacheService;
    }

    public String getName(String cacheName, Object id,Object parentId) {
        try {
            if (parentId == null) {
                if (mdCacheService.isCached(cacheName)) {

                    Map<String, Object> mdCache =
                            mdCacheService.getMdCache(cacheName);
                    return (String) mdCache.get(id);
                }
            }
            if (mdCacheService.isCached(cacheName)) {

                Map<String, Object> mdCache =
                        mdCacheService.getMdCache(cacheName);
                if (mdCache.containsKey(parentId)) {
                    return (String) ((Map) mdCache.get(parentId)).get(id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

}
