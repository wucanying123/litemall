package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.xinda.common.BaseResp;
import com.xinda.common.Constant;
import com.xinda.screen.model.entity.Schedule;
import com.xinda.screen.service.ScheduleService;
import com.xinda.util.StringUtilsXD;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/screen/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    private static Logger logger = LoggerFactory.getLogger(ScheduleController.class);

    /**
     * @Description: 获取定时列表
     * @title selectSchedulePage
     * @param pageNum 开始页数
     * @param pageSize 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "获取定时列表")
    @GetMapping("/selectSchedulePage")
    public BaseResp<PageInfo<Schedule>> selectSchedulePage(@RequestBody Schedule schedule,@RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        logger.info("selectSchedulePage and schedule={}, pageNum={},pageSize",JSON.toJSONString(schedule), pageNum, pageSize);
        BaseResp<PageInfo<Schedule>> baseResp = new BaseResp<>();
        try {
            PageInfo<Schedule> page = scheduleService.selectSchedulePage(schedule, StringUtilsXD.checkPageNumParam(pageNum), StringUtilsXD.checkPageSizeParam(pageSize));
            baseResp.setData(page);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectSchedulePage and schedule={},pageNum={},pageSize", JSON.toJSONString(schedule), pageNum, pageSize, e);
        }
        return baseResp;
    }

    /**
     * @Description: 通过定时id查看定时详情
     * @param scheduleId 定时id
     * @Title: selectScheduleById
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "通过定时id查看定时详情")
    @GetMapping(value = "/selectScheduleById")
    public BaseResp<Schedule> selectScheduleById(@RequestParam(value = "scheduleId") String scheduleId) {
        logger.info("selectScheduleById and scheduleId={}", scheduleId);
        BaseResp<Schedule> baseResp = new BaseResp<>();
        if (StringUtilsXD.isBlank(scheduleId)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            baseResp = scheduleService.selectScheduleById(scheduleId);
        } catch (Exception e) {
            logger.error("selectScheduleById and scheduleId={}", scheduleId, e);
        }
        return baseResp;
    }

    /**
     * @Description: 添加定时
     * @Title: insertSchedule
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "添加定时")
    @PostMapping(value = "/insertSchedule")
    public BaseResp<Schedule> insertSchedule(@RequestBody Schedule schedule) {
        logger.info("insertSchedule and schedule:{}", JSON.toJSONString(schedule));
        BaseResp<Schedule> baseResp = new BaseResp<>();
        try {
            baseResp = scheduleService.insertSchedule(schedule);
        } catch (Exception e) {
            logger.error("insertSchedule and schedule:{}", JSON.toJSONString(schedule), e);
        }
        return baseResp;
    }

    /**
     * @Description: 编辑定时
     * @title updateScheduleById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "编辑定时")
    @PostMapping(value = "/updateScheduleById")
    public BaseResp<Schedule> updateScheduleById(@RequestBody Schedule schedule) {
        logger.info("updateScheduleById and schedule:{}", JSON.toJSONString(schedule));
        BaseResp<Schedule> baseResp = new BaseResp<>();
        if (StringUtilsXD.isBlank(schedule.getId())) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            baseResp = scheduleService.updateScheduleById(schedule);
        } catch (Exception e) {
            logger.error("updateScheduleById and schedule:{}", JSON.toJSONString(schedule), e);

        }
        return baseResp;
    }

    /**
     * @Description: 删除定时
     * @Title: deleteByIdBatch
     * @param ids 定时id集合
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "删除定时")
    @PostMapping(value = "/deleteByIdBatch")
    public BaseResp<Schedule> deleteByIdBatch(@RequestParam(value = "ids") String[] ids) {
        logger.info("deleteByIdBatch and ids={}", JSON.toJSONString(ids));
        BaseResp<Schedule> baseResp = new BaseResp<>();
        try {
            baseResp = scheduleService.deleteByIdBatch(ids);
        } catch (Exception e) {
            logger.error("deleteByIdBatch and ids={}", JSON.toJSONString(ids), e);
        }
        return baseResp;
    }
}
