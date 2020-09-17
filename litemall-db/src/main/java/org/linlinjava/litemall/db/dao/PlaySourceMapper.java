package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.PlaySource;

import java.util.List;

@Mapper
public interface PlaySourceMapper {

    List<PlaySource> selectPlaySourcePage(@Param("playSource") PlaySource playSource);

    PlaySource selectByPrimaryKey(String id);

    int insertSelective(PlaySource playSource);

    int updateByPrimaryKeySelective(PlaySource playSource);

    int deleteByIdBatch(String[] ids);

    int deleteByPrimaryKey(String id);
}