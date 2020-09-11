package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.Examine;

import java.util.List;

public interface ExamineMapper {
    int deleteByPrimaryKey(String id);

    int insertSelective(Examine record);

    Examine selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Examine record);
    
    List<Examine> selectExaminePage(@Param("examine") Examine examine);
}