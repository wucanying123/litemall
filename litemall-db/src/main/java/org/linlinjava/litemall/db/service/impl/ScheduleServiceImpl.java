package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.Constant;
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
    public ResponseUtil<Schedule> selectScheduleById(String scheduleId) {
        ResponseUtil<Schedule> responseUtil = new ResponseUtil<Schedule>();
        try {
            Schedule schedule = scheduleMapper.selectByPrimaryKey(scheduleId);
            responseUtil.setData(schedule);
            responseUtil.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectScheduleById error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Schedule> insertSchedule(Schedule schedule) {
        ResponseUtil<Schedule> responseUtil = new ResponseUtil<Schedule>();
        schedule.setId(UUID.randomUUID().toString().replace("-", ""));
        try {
            int n = scheduleMapper.insertSelective(schedule);
            if (n == 1) {
                responseUtil.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertSchedule error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Schedule> updateScheduleById(Schedule schedule) {
        ResponseUtil<Schedule> responseUtil = new ResponseUtil<Schedule>();
        try {
            int n = scheduleMapper.updateByPrimaryKeySelective(schedule);
            if (n >= 1) {
                responseUtil.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateScheduleById error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Schedule> deleteByIdBatch(String[] ids) {
        ResponseUtil<Schedule> responseUtil = new ResponseUtil<Schedule>();
        try {
            int m = scheduleMapper.deleteByIdBatch(ids);
            if (m >= 1) {
                responseUtil.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteByIdBatch error and msg={}", e);
        }
        return responseUtil;
    }
}
