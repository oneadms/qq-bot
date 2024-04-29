package com.jky.qqbot.mapper;

import com.jky.qqbot.model.BaseMdQuery;
import com.jky.qqbot.model.MdKVModel;

import java.util.List;

public interface DynamicSqlMapper {
     List<MdKVModel> query(BaseMdQuery baseMdQuery) ;
}
