package com.jky.qqbot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 黑名单
 * </p>
 *
 * @author jky
 * @since 2024-05-10
 */
@TableName("t_md_black_list")
@ApiModel(value = "MdBlackList对象", description = "黑名单")
public class MdBlackList implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("创建时间")
    private LocalDateTime createDate;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateDate;

    @ApiModelProperty("逻辑删除")
    private String isDel;

    @ApiModelProperty("拉黑原因")
    private String reason;

    @ApiModelProperty("用户id")
    private String userId;

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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "MdBlackList{" +
            "id = " + id +
            ", createDate = " + createDate +
            ", updateDate = " + updateDate +
            ", isDel = " + isDel +
            ", reason = " + reason +
            ", userId = " + userId +
        "}";
    }
}
