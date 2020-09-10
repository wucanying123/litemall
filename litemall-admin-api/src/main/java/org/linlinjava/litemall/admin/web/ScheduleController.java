package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.domain.Schedule;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.Constant;
import org.linlinjava.litemall.db.domain.Schedule;
import org.linlinjava.litemall.db.service.ScheduleService;
import io.swagger.annotations.ApiOperation;
import org.linlinjava.litemall.db.util.StringUtilsXD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/screen/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    private static Logger logger = LoggerFactory.getLogger(ScheduleController.class);

    /**
     * @Description: 获取定时列表
     * @title selectSchedulePage
     * @param page 开始页数
     * @param limit 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "获取定时列表")
    @GetMapping("/selectSchedulePage")
    public Object selectSchedulePage(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        Schedule schedule = null;
        logger.info("selectSchedulePage and schedule={},page={},limit", JSON.toJSONString(schedule), page, limit);
        try {
            PageInfo<Schedule> pageList = scheduleService.selectSchedulePage(schedule, StringUtilsXD.checkPageNumParam(page), StringUtilsXD.checkPageSizeParam(limit));
            return ResponseUtil.okPage(pageList);
        } catch (Exception e) {
            logger.error("selectSchedulePage and schedule={},page={},limit", JSON.toJSONString(schedule), page, limit, e);
        }
        return ResponseUtil.fail();
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
    public ResponseUtil<Schedule> selectScheduleById(@RequestParam(value = "scheduleId") String scheduleId) {
        logger.info("selectScheduleById and scheduleId={}", scheduleId);
        ResponseUtil<Schedule> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(scheduleId)) {
            return responseUtil.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            responseUtil = scheduleService.selectScheduleById(scheduleId);
        } catch (Exception e) {
            logger.error("selectScheduleById and scheduleId={}", scheduleId, e);
        }
        return responseUtil;
    }

    /**
     * @Description: 添加定时
     * @Title: insertSchedule
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "添加定时")
    @PostMapping(value = "/insertSchedule")
    public ResponseUtil<Schedule> insertSchedule(@RequestBody Schedule schedule) {
        logger.info("insertSchedule and schedule:{}", JSON.toJSONString(schedule));
        ResponseUtil<Schedule> responseUtil = new ResponseUtil<>();
        try {
            responseUtil = scheduleService.insertSchedule(schedule);
        } catch (Exception e) {
            logger.error("insertSchedule and schedule:{}", JSON.toJSONString(schedule), e);
        }
        return responseUtil;
    }

    /**
     * @Description: 编辑定时
     * @title updateScheduleById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "编辑定时")
    @PostMapping(value = "/updateScheduleById")
    public ResponseUtil<Schedule> updateScheduleById(@RequestBody Schedule schedule) {
        logger.info("updateScheduleById and schedule:{}", JSON.toJSONString(schedule));
        ResponseUtil<Schedule> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(schedule.getId())) {
            return responseUtil.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            responseUtil = scheduleService.updateScheduleById(schedule);
        } catch (Exception e) {
            logger.error("updateScheduleById and schedule:{}", JSON.toJSONString(schedule), e);

        }
        return responseUtil;
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
    public ResponseUtil<Schedule> deleteByIdBatch(@RequestParam(value = "ids") String[] ids) {
        logger.info("deleteByIdBatch and ids={}", JSON.toJSONString(ids));
        ResponseUtil<Schedule> responseUtil = new ResponseUtil<>();
        try {
            responseUtil = scheduleService.deleteByIdBatch(ids);
        } catch (Exception e) {
            logger.error("deleteByIdBatch and ids={}", JSON.toJSONString(ids), e);
        }
        return responseUtil;
    }
}
