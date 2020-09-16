package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.Examine;

import java.util.List;

public interface ExamineMapper {
    int deleteByPrimaryKey(String id);

    int deleteByDetailId(@Param("detailId")String detailId);

    int insertSelective(Examine record);

    Examine selectByPrimaryKey(String id);

    Examine selecByDetailId(@Param("detailId") String detailId);

    int updateByPrimaryKeySelective(Examine record);
    
    List<Examine> selectExaminePage(@Param("examine") Examine examine);
}