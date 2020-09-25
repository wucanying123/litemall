package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.linlinjava.litemall.db.domain.*;
import org.linlinjava.litemall.db.service.ItemService;
import org.linlinjava.litemall.db.util.DateUtil;
import org.linlinjava.litemall.db.dao.ScheduleMapper;
import org.linlinjava.litemall.db.service.ScheduleService;
import org.linlinjava.litemall.db.util.StringUtilsXD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private ItemService itemService;


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
    public int insertSchedule(Schedule schedule, String itemId) {
        int n = 0;
        schedule.setId(UUID.randomUUID().toString().replace("-", ""));
        try {
            schedule = setScheduleOption(schedule);
            long cuttentTime = DateUtil.getDateline();
            schedule.setCreateTime(cuttentTime);
            schedule.setUpdateTime(cuttentTime);
            n = scheduleMapper.insertSelective(schedule);
            updateItemScheduleIds(schedule.getId(), itemId,"add");
        } catch (Exception e) {
            logger.error("insertSchedule error and msg={}", e);
        }
        return n;
    }

    private Schedule setScheduleOption(Schedule schedule){
        if(StringUtilsXD.isEmpty(schedule.getDateType())){
            schedule.setDateType(DateType.All.toString());
        }
        if(null != schedule.getDateType()){
            if(schedule.getDateType().equals(DateType.All.toString())){
                schedule.setStartDate("");
                schedule.setEndDate("");
            }
        }
        if(null != schedule.getTimeType()){
            if(schedule.getTimeType().equals(TimeType.All.toString())){
                schedule.setStartTime("");
                schedule.setEndTime("");
            }
        }
        if(null != schedule.getFilterType()){
            if(schedule.getFilterType().equals(FilterType.None.toString())){
                schedule.setWeekFilter("");
                schedule.setWeekFilterArray(null);
            }
        }
        List<String> weekFilterArray = schedule.getWeekFilterArray();
        if(null != weekFilterArray && weekFilterArray.size() >0){
            String weekFilter = weekFilterArray.toString();
            weekFilter = StringUtilsXD.replaceBlank(weekFilter);
            schedule.setWeekFilter(weekFilter);
        }
        return schedule;
    }

    private void updateItemScheduleIds(String scheduleId, String itemId, String addOrSub){
        Item item = itemService.selectItemById(itemId);
        String scheduleIds = item.getSchedulesIds();
        List<String> newScheduleIds = new ArrayList<>();
        if (null != scheduleIds && scheduleIds.length() > 0) {
            List<String> scheduleIdsList = Arrays.asList(scheduleIds.split(","));
            for (String scheduleId1 : scheduleIdsList) {
                newScheduleIds.add(scheduleId1);
            }
        }
        if(addOrSub.equals("add")) {
            newScheduleIds.add(scheduleId);
        }else if(addOrSub.equals("sub")) {
            newScheduleIds.remove(scheduleId);
        }
        String scheduleIdsStr = "";
        if(null != newScheduleIds && newScheduleIds.size()>0) {
            scheduleIdsStr = StringUtils.join(newScheduleIds.toArray(), ",");
        }
        item.setSchedulesIds(scheduleIdsStr);
        itemService.updateItemById(item);
    }

    @Override
    public int updateScheduleById(Schedule schedule) {
        int n = 0;
        try {
            schedule = setScheduleOption(schedule);
            schedule.setUpdateTime(DateUtil.getDateline());
            n = scheduleMapper.updateByPrimaryKeySelective(schedule);
        } catch (Exception e) {
            logger.error("updateScheduleById error and msg={}", e);
        }
        return n;
    }

    @Override
    public int deleteById(String id, String itemId) {
        int n = 0;
        try {
            n = scheduleMapper.deleteByPrimaryKey(id);
            updateItemScheduleIds(id, itemId,"sub");
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return n;
    }
}
