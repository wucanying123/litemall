package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.domain.Schedule;

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
    Schedule selectScheduleById(String scheduleId);

    /**
     * @Description: 添加定时
     * @title insertSchedule
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    int insertSchedule(Schedule schedule,String itemId);

    /**
     * @Description: 编辑定时
     * @title updateScheduleById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    int updateScheduleById(Schedule schedule);

    /**
     * @Description: 删除定时
     * @Title: deleteById
     * @param id 定时id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    int deleteById(String id, String itemId);
}
