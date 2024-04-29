package com.jky.qqbot.service.md;

import com.jky.qqbot.common.config.ConfigCenter;
import com.jky.qqbot.mapper.DynamicSqlMapper;
import com.jky.qqbot.model.BaseMdQuery;
import com.jky.qqbot.model.MdKVModel;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MdCacheService {
    DynamicSqlMapper dynamicSqlMapper;
    public static boolean initSuccess;


    public MdCacheService(DynamicSqlMapper dynamicSqlMapper) {
        this.dynamicSqlMapper = dynamicSqlMapper;
    }

    Map<String,Map<String,Object>> allNamespaceMap = new HashMap();



    public void initCache() {
        loadMdCache("mdDic");
        initSuccess = true;
    }

    public Map<String, Object> getMdCache(String cacheName) {
        return allNamespaceMap.get(cacheName);
    }



    private void loadMdCache(String cacheName) {
        String entityName = ConfigCenter.getEntityName(cacheName);
        String metaData = ConfigCenter.getMetaData(cacheName);
        BaseMdQuery baseMdQuery = new BaseMdQuery();
        String[] metaCol = metaData.split(",");
        baseMdQuery.setColKey(metaCol[0]);
        baseMdQuery.setColVal(metaCol[1]);
        baseMdQuery.setParentCol(metaCol[2]);
        baseMdQuery.setEntityName(entityName);

        List<MdKVModel> modelList = dynamicSqlMapper.query(baseMdQuery);
        HashMap<String, Object> map = new HashMap<>();
        for (MdKVModel mdKVModel : modelList) {
            map.putIfAbsent(mdKVModel.getParentId().toString(), new HashMap<>());
            ((Map) map.get(mdKVModel.getParentId().toString())).put(mdKVModel.getNodeKey(), mdKVModel.getNodeVal());
        }
        allNamespaceMap.put(cacheName, map);
    }

    public Map getMdCache(String cacheName, Object parentId) {
        return (Map) getMdCache(cacheName).get(parentId);
    }

    public boolean isCached(String cacheName) {
        return allNamespaceMap.containsKey(cacheName);
    }
}
