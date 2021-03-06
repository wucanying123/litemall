package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.linlinjava.litemall.db.domain.*;
import org.linlinjava.litemall.db.service.*;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.Constant;
import io.swagger.annotations.ApiOperation;
import org.linlinjava.litemall.db.util.StringUtilsXD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/admin/screen/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ScreenService screenService;
    @Autowired
    private ItemService itemService;

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
    public ResponseUtil selectSchedulePage(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        ResponseUtil responseUtil = new ResponseUtil();
        Schedule schedule = null;
        logger.info("selectSchedulePage and schedule={},page={},limit={}", JSON.toJSONString(schedule), page, limit);
        try {
            PageInfo<Schedule> pageList = scheduleService.selectSchedulePage(schedule, StringUtilsXD.checkPageNumParam(page), StringUtilsXD.checkPageSizeParam(limit));
            return responseUtil.succeedPage(pageList);
        } catch (Exception e) {
            logger.error("selectSchedulePage and schedule={},page={},limit={}", JSON.toJSONString(schedule), page, limit, e);
        }
        return responseUtil;
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
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            Schedule schedule = scheduleService.selectScheduleById(scheduleId);
            if(null != schedule && null != schedule.getDateType()){
                if(schedule.getDateType().equals(DateType.All.toString())){
                    schedule.setDateType("1");
                }else if(schedule.getDateType().equals(DateType.Range.toString())){
                    schedule.setDateType("2");
                }
            }
            responseUtil.setData(schedule);
            responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
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
    public ResponseUtil<Schedule> insertSchedule(@RequestBody Schedule schedule,@RequestParam(value = "itemId") String itemId) {
        logger.info("insertSchedule and schedule:{}", JSON.toJSONString(schedule));
        ResponseUtil<Schedule> responseUtil = new ResponseUtil<>();
        try {
            Subject currentUser = SecurityUtils.getSubject();
            LitemallAdmin admin = (LitemallAdmin) currentUser.getPrincipal();
            schedule.setUserid(admin.getId());
            int n = scheduleService.insertSchedule(schedule,itemId);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
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
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            int n = scheduleService.updateScheduleById(schedule);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateScheduleById and schedule:{}", JSON.toJSONString(schedule), e);

        }
        return responseUtil;
    }

    /**
     * @Description: 删除定时
     * @Title: deleteById
     * @param id 定时id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "删除定时")
    @PostMapping(value = "/deleteById")
    public ResponseUtil<Schedule> deleteById(@RequestParam(value = "id") String id,@RequestParam(value = "itemId") String itemId) {
        logger.info("deleteById and id={}", JSON.toJSONString(id));
        ResponseUtil<Schedule> responseUtil = new ResponseUtil<>();
        try {
            int n = scheduleService.deleteById(id,itemId);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteById and id={}", JSON.toJSONString(id), e);
        }
        return responseUtil;
    }

    /**
     * @Description: 通过项目id获取定时列表
     * @title selectListByItemId
     * @auther IngaWu
     * @currentdate:2020年9月22日
     */
    @ApiOperation(value = "通过项目id获取定时列表")
    @GetMapping("/selectListByItemId")
    public Object selectListByItemId(@RequestParam(value = "itemId") String itemId) {
        logger.info("selectListByItemId and itemId={}", itemId);
        List<ScheduleVO> scheduleVOList = new ArrayList<>();
        try {
            Item item = itemService.selectItemById(itemId);
            scheduleVOList = screenService.readItemSchedule(item);
            item.setSchedules(scheduleVOList);
        } catch (Exception e) {
            logger.error("selectListByItemId and itemId={}", itemId, e);
        }
        return ResponseUtil.okList(scheduleVOList);
    }
}
