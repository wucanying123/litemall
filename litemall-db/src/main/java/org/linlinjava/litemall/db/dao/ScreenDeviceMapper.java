package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.ScreenDevice;

import java.util.List;

public interface ScreenDeviceMapper {
    int deleteByPrimaryKey(String id);

    int insertSelective(ScreenDevice record);

    ScreenDevice selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ScreenDevice record);

    List<ScreenDevice> selectScreenDevicePage(@Param("screenDevice") ScreenDevice screenDevice);
}