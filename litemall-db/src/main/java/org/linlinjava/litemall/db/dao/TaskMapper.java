package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.Task;

import java.util.List;

@Mapper
public interface TaskMapper {

    List<Task> selectTaskPage(@Param("task") Task task);

    Task selectByPrimaryKey(String id);

    int insertSelective(Task task);

    int updateByPrimaryKeySelective(Task task);

    int deleteByIdBatch(String[] ids);

    int deleteByPrimaryKey(String id);

    Task selectTaskByName(@Param("taskName") String taskName);
}