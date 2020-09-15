package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.util.DateUtil;
import org.linlinjava.litemall.db.dao.ScheduleMapper;
import org.linlinjava.litemall.db.domain.Schedule;
import org.linlinjava.litemall.db.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;


    private static Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    @Override
    public PageInfo<Schedule> selectSchedulePage(Schedule schedule, Integer pageNum, Integer pageSize) {
        PageInfo<Schedule> page = null;
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Schedule> list = scheduleMapper.selectSchedulePage(schedule);
            String jsonString = JSON.toJSONString(list);
            page = new PageInfo<>(list);
        } catch (Exception e) {
            logger.error("selectSchedulePage error and msg={}", e);
        }
        return page;
    }

    @Override
    public Schedule selectScheduleById(String scheduleId) {
        Schedule schedule = null;
        try {
            schedule = scheduleMapper.selectByPrimaryKey(scheduleId);
        } catch (Exception e) {
            logger.error("selectScheduleById error and msg={}", e);
        }
        return schedule;
    }

    @Override
    public int insertSchedule(Schedule schedule) {
        int n = 0;
        schedule.setId(UUID.randomUUID().toString().replace("-", ""));
        try {
            long cuttentTime = DateUtil.getDateline();
            schedule.setCreateTime(cuttentTime);
            schedule.setUpdateTime(cuttentTime);
            n = scheduleMapper.insertSelective(schedule);
        } catch (Exception e) {
            logger.error("insertSchedule error and msg={}", e);
        }
        return n;
    }

    @Override
    public int updateScheduleById(Schedule schedule) {
        int n = 0;
        try {
            schedule.setUpdateTime(DateUtil.getDateline());
            n = scheduleMapper.updateByPrimaryKeySelective(schedule);

        } catch (Exception e) {
            logger.error("updateScheduleById error and msg={}", e);
        }
        return n;
    }

    @Override
    public int deleteById(String id) {
        int n = 0;
        try {
            n = scheduleMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return n;
    }
}
