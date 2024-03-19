package com.wsq.project.model.vo;

import com.wsq.wsqapicommon.model.entity.InterfaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 接口信息
 *
 * @author wsq
 * @TableName product
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoVO extends InterfaceInfo {

    /**
     * 是否已点赞
     */
    private Integer totalNum;

    private static final long serialVersionUID = 1L;
}