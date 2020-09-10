package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.xinda.common.BaseResp;
import com.xinda.common.Constant;
import com.xinda.screen.model.entity.Task;
import com.xinda.screen.service.TaskService;
import com.xinda.util.StringUtilsXD;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/screen/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    private static Logger logger = LoggerFactory.getLogger(TaskController.class);

    /**
     * @Description: 获取任务列表
     * @title selectTaskPage
     * @param pageNum 开始页数
     * @param pageSize 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "获取任务列表")
    @GetMapping("/selectTaskPage")
    public BaseResp<PageInfo<Task>> selectTaskPage(@RequestBody Task task,@RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        logger.info("selectTaskPage and task={}, pageNum={},pageSize",JSON.toJSONString(task), pageNum, pageSize);
        BaseResp<PageInfo<Task>> baseResp = new BaseResp<>();
        try {
            PageInfo<Task> page = taskService.selectTaskPage(task, StringUtilsXD.checkPageNumParam(pageNum), StringUtilsXD.checkPageSizeParam(pageSize));
            baseResp.setData(page);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectTaskPage and task={},pageNum={},pageSize", JSON.toJSONString(task), pageNum, pageSize, e);
        }
        return baseResp;
    }

    /**
     * @Description: 通过任务id查看任务详情
     * @param taskId 任务id
     * @Title: selectTaskById
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "通过任务id查看任务详情")
    @GetMapping(value = "/selectTaskById")
    public BaseResp<Task> selectTaskById(@RequestParam(value = "taskId") String taskId) {
        logger.info("selectTaskById and taskId={}", taskId);
        BaseResp<Task> baseResp = new BaseResp<>();
        if (StringUtilsXD.isBlank(taskId)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            baseResp = taskService.selectTaskById(taskId);
        } catch (Exception e) {
            logger.error("selectTaskById and taskId={}", taskId, e);
        }
        return baseResp;
    }

    /**
     * @Description: 添加任务
     * @Title: insertTask
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "添加任务")
    @PostMapping(value = "/insertTask")
    public BaseResp<Task> insertTask(@RequestBody Task task) {
        logger.info("insertTask and task:{}", JSON.toJSONString(task));
        BaseResp<Task> baseResp = new BaseResp<>();
        try {
            baseResp = taskService.insertTask(task);
        } catch (Exception e) {
            logger.error("insertTask and task:{}", JSON.toJSONString(task), e);
        }
        return baseResp;
    }

    /**
     * @Description: 编辑任务
     * @title updateTaskById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "编辑任务")
    @PostMapping(value = "/updateTaskById")
    public BaseResp<Task> updateTaskById(@RequestBody Task task) {
        logger.info("updateTaskById and task:{}", JSON.toJSONString(task));
        BaseResp<Task> baseResp = new BaseResp<>();
        if (StringUtilsXD.isBlank(task.get_id())) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            baseResp = taskService.updateTaskById(task);
        } catch (Exception e) {
            logger.error("updateTaskById and task:{}", JSON.toJSONString(task), e);

        }
        return baseResp;
    }

    /**
     * @Description: 删除任务
     * @Title: deleteByIdBatch
     * @param ids 任务id集合
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "删除任务")
    @PostMapping(value = "/deleteByIdBatch")
    public BaseResp<Task> deleteByIdBatch(@RequestParam(value = "ids") String[] ids) {
        logger.info("deleteByIdBatch and ids={}", JSON.toJSONString(ids));
        BaseResp<Task> baseResp = new BaseResp<>();
        try {
            baseResp = taskService.deleteByIdBatch(ids);
        } catch (Exception e) {
            logger.error("deleteByIdBatch and ids={}", JSON.toJSONString(ids), e);
        }
        return baseResp;
    }
}
