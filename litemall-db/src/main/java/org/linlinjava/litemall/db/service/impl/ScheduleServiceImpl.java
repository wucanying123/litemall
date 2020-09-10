package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xinda.common.BaseResp;
import com.xinda.common.Constant;
import com.xinda.screen.dao.ScheduleMapper;
import com.xinda.screen.model.entity.Schedule;
import com.xinda.screen.service.ScheduleService;
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
    public PageInfo<Schedule> selectSchedulePage(Schedule schedule,Integer pageNum,Integer pageSize) {
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
    public BaseResp<Schedule> selectScheduleById(String scheduleId) {
        BaseResp<Schedule> baseResp = new BaseResp<Schedule>();
        try {
            Schedule schedule = scheduleMapper.selectByPrimaryKey(scheduleId);
            baseResp.setData(schedule);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectScheduleById error and msg={}", e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Schedule> insertSchedule(Schedule schedule) {
        BaseResp<Schedule> baseResp = new BaseResp<Schedule>();
        schedule.setId(UUID.randomUUID().toString().replace("-", ""));
        try {
            int n = scheduleMapper.insertSelective(schedule);
            if (n == 1) {
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertSchedule error and msg={}", e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Schedule> updateScheduleById(Schedule schedule) {
        BaseResp<Schedule> baseResp = new BaseResp<Schedule>();
        try {
            int n = scheduleMapper.updateByPrimaryKeySelective(schedule);
            if (n >= 1) {
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateScheduleById error and msg={}", e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Schedule> deleteByIdBatch(String[] ids) {
        BaseResp<Schedule> baseResp = new BaseResp<Schedule>();
        try {
            int m = scheduleMapper.deleteByIdBatch(ids);
            if (m >= 1) {
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteByIdBatch error and msg={}", e);
        }
        return baseResp;
    }
}
