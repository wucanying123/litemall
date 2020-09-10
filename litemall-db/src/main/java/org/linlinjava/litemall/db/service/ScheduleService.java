package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageInfo;
import com.xinda.common.BaseResp;
import com.xinda.screen.model.entity.Schedule;

public interface ScheduleService {

    /**
     * @Description: 获取定时列表
     * @title selectSchedulePage
     * @param pageNum 开始页数
     * @param pageSize 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    PageInfo<Schedule> selectSchedulePage(Schedule schedule,Integer pageNum,Integer pageSize);

    /**
     * @Description: 通过定时id查看定时详情
     * @Title: selectScheduleById
     * @param scheduleId 定时id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    BaseResp<Schedule> selectScheduleById(String scheduleId);

    /**
     * @Description: 添加定时
     * @title insertSchedule
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    BaseResp<Schedule> insertSchedule(Schedule schedule);

    /**
     * @Description: 编辑定时
     * @title updateScheduleById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    BaseResp<Schedule> updateScheduleById(Schedule schedule);

    /**
     * @Description: 删除定时
     * @Title: deleteByIdBatch
     * @param ids 定时id集合
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    BaseResp<Schedule> deleteByIdBatch(String[] ids);
}
