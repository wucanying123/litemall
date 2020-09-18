package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.Source;

import java.util.List;

@Mapper
public interface SourceMapper {

    List<Source> selectSourcePage(@Param("source") Source source);

    Source selectByPrimaryKey(String sourceId);

    int insertSelective(Source source);

    int updateByPrimaryKeySelective(Source source);

    int deleteByIdBatch(String[] ids);

    int deleteByPrimaryKey(String sourceId);
}