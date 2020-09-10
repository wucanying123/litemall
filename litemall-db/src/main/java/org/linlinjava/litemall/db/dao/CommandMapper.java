package org.linlinjava.litemall.db.dao;

import org.linlinjava.litemall.db.domain.Command;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommandMapper {

    List<Command> selectCommandPage(@Param("command") Command command);

    Command selectByPrimaryKey(String id);

    int insertSelective(Command command);

    int updateByPrimaryKeySelective(Command command);

    int deleteByIdBatch(String[] ids);

    int deleteByPrimaryKey(String id);
}