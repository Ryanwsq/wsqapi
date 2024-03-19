package com.wsq.project.service.impl.inner;

import com.wsq.project.service.UserInterfaceInfoService;
import com.wsq.wsqapicommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @Author: wsq
 * @Date: 2024/3/13 12:52
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        //调用注入的UserInterfaceInfoService
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }
}
