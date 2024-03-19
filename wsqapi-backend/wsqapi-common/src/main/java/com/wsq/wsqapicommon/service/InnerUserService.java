package com.wsq.wsqapicommon.service;


import com.wsq.wsqapicommon.model.entity.User;

/**
 * 用户服务
 *
 * @author wsq
 */
public interface InnerUserService{
    /**
     * 数据库中查是否已分配给用户密钥
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);
}
