package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.ScreenDevice;

import java.util.List;

public interface ScreenDeviceMapper {
    int deleteByPrimaryKey(String id);

    int insertSelective(ScreenDevice record);

    ScreenDevice selectByPrimaryKey(String id);

    ScreenDevice selectByCardId(@Param("cardId") String cardId);

    int updateByPrimaryKeySelective(ScreenDevice record);

    int updateOffLineByCardId(@Param("cardId") String cardId);

    List<ScreenDevice> selectScreenDevicePage(@Param("screenDevice") ScreenDevice screenDevice);
}