package com.wsq.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wsq.wsqapicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author WSQ
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Mapper
* @createDate 2024-03-06 11:14:30
* @Entity com.wsq.project.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {
    //select interfaceInfoId,sum(totalNum) as totalNum from user_interface_info group by interfaceInfoId order by totalNum desc limit 3;
    //查询用户接口信息表中，按照指定的limit参数进行筛选
    //返回前limit条记录的接口信息列表
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}




