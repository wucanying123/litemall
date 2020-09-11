package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.Live;

import java.util.List;

public interface LiveMapper {
    int deleteByPrimaryKey(String id);

    int insertSelective(Live record);

    Live selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Live record);

    List<Live> selectLivePage(@Param("live") Live live);
}