package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.Schedule;

import java.util.List;

@Mapper
public interface ScheduleMapper {

    List<Schedule> selectSchedulePage(@Param("schedule") Schedule schedule);

    Schedule selectByPrimaryKey(String id);

    int insertSelective(Schedule schedule);

    int updateByPrimaryKeySelective(Schedule schedule);

    int deleteByIdBatch(String[] ids);

    int deleteByPrimaryKey(String id);
}