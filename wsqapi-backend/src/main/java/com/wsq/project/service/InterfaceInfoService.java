package com.wsq.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wsq.wsqapicommon.model.entity.InterfaceInfo;


/**
* @author WSQ
* @description 针对表【interface_info(接口信息表)】的数据库操作Service
* @createDate 2024-02-29 13:40:19
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

}
