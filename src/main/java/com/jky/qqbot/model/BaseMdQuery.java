package com.jky.qqbot.model;

import lombok.Data;

@Data
public class BaseMdQuery {
    private String colVal;
    private String colKey;
    private String parentCol;
    private Object parentId;
    private String entityName;
}
