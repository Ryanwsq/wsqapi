package com.wsq.wsqapicommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wsq.wsqapicommon.model.entity.User;
import com.wsq.wsqapicommon.model.entity.UserInterfaceInfo;

/**
* @author WSQ
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service
* @createDate 2024-03-06 11:14:30
*/
public interface InnerUserInterfaceInfoService  {
    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId,long userId);
}
