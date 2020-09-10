package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.Source;

import java.util.List;

@Mapper
public interface SourceMapper {

    List<Source> selectSourcePage(@Param("source") Source source);

    Source selectByPrimaryKey(String id);

    int insertSelective(Source source);

    int updateByPrimaryKeySelective(Source source);

    int deleteById(String ids);

    int deleteByPrimaryKey(String id);
}