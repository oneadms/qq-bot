package com.jky.qqbot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 字典管理
 * </p>
 *
 * @author jky
 * @since 2024-04-28
 */
@TableName("t_md_dictonary")
@ApiModel(value = "MdDictonary对象", description = "字典管理")
public class MdDictonary implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("字典值")
    private String dicVal;

    @ApiModelProperty("字典代码")
    private String dicKey;

    @ApiModelProperty("字典名称")
    private String dicName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createDate;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateDate;

    @ApiModelProperty("逻辑删除")
    private String isDel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDicVal() {
        return dicVal;
    }

    public void setDicVal(String dicVal) {
        this.dicVal = dicVal;
    }

    public String getDicKey() {
        return dicKey;
    }

    public void setDicKey(String dicKey) {
        this.dicKey = dicKey;
    }

    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    @Override
    public String toString() {
        return "MdDictonary{" +
            "id = " + id +
            ", dicVal = " + dicVal +
            ", dicKey = " + dicKey +
            ", dicName = " + dicName +
            ", createDate = " + createDate +
            ", updateDate = " + updateDate +
            ", isDel = " + isDel +
        "}";
    }
}
