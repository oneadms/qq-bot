package com.jky.qqbot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 自动回复模版
 * </p>
 *
 * @author jky
 * @since 2024-05-06
 */
@TableName("t_md_reply_message")
@ApiModel(value = "MdReplyMessage对象", description = "自动回复模版")
public class MdReplyMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("创建时间")
    private LocalDateTime createDate;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateDate;

    @ApiModelProperty("逻辑删除")
    private String isDel;

    @ApiModelProperty("回复内容 Code类型")
    private String message;

    @ApiModelProperty("排序")
    private Integer seq;

    @ApiModelProperty("类型")
    private String messageType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return "MdReplyMessage{" +
            "id = " + id +
            ", createDate = " + createDate +
            ", updateDate = " + updateDate +
            ", isDel = " + isDel +
            ", message = " + message +
            ", seq = " + seq +
            ", messageType = " + messageType +
        "}";
    }
}
