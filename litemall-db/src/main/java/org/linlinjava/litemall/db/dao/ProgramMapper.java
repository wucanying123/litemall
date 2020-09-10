package org.linlinjava.litemall.db.dao;

import org.linlinjava.litemall.db.domain.Program;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProgramMapper {

    List<Program> selectProgramPage(@Param("program") Program program);

    Program selectByPrimaryKey(String id);

    int insertSelective(Program program);

    int updateByPrimaryKeySelective(Program program);

    int deleteByIdBatch(String[] ids);

    int deleteByPrimaryKey(String id);
}