package com.jky.qqbot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 技术人员
 * </p>
 *
 * @author jky
 * @since 2024-04-28
 */
@TableName("t_md_user")
@ApiModel(value = "MdUser对象", description = "技术人员")
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class MdUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("用户类型")
    private String userType;

    @ApiModelProperty("用户标识")
    private String userId;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("技术栈")
    private String technologyStack;

    @ApiModelProperty("创建时间")
    private LocalDateTime createDate;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateDate;

    @ApiModelProperty("逻辑删除")
    private String isDel;


}
