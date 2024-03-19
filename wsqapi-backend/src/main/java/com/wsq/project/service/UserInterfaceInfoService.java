package com.wsq.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wsq.wsqapicommon.model.entity.UserInterfaceInfo;

/**
 * @Author: wsq
 * @Date: 2024/3/12 16:14
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);

}
