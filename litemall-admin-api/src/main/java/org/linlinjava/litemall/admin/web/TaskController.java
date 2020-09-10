package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.domain.Task;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.Constant;
import org.linlinjava.litemall.db.service.TaskService;
import org.linlinjava.litemall.db.util.StringUtilsXD;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/screen/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    private static Logger logger = LoggerFactory.getLogger(TaskController.class);

    /**
     * @Description: 获取任务列表
     * @title selectTaskPage
     * @param page 开始页数
     * @param limit 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "获取任务列表")
    @GetMapping("/selectTaskPage")
    public Object selectTaskPage(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        Task task = null;
        logger.info("selectTaskPage and task={},page={},limit", JSON.toJSONString(task), page, limit);
        try {
            PageInfo<Task> pageList = taskService.selectTaskPage(task, StringUtilsXD.checkPageNumParam(page), StringUtilsXD.checkPageSizeParam(limit));
            return ResponseUtil.okPage(pageList);
        } catch (Exception e) {
            logger.error("selectTaskPage and task={},page={},limit", JSON.toJSONString(task), page, limit, e);
        }
        return ResponseUtil.fail();
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
    public ResponseUtil<Task> selectTaskById(@RequestParam(value = "taskId") String taskId) {
        logger.info("selectTaskById and taskId={}", taskId);
        ResponseUtil<Task> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(taskId)) {
            return responseUtil.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            responseUtil = taskService.selectTaskById(taskId);
        } catch (Exception e) {
            logger.error("selectTaskById and taskId={}", taskId, e);
        }
        return responseUtil;
    }

    /**
     * @Description: 添加任务
     * @Title: insertTask
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "添加任务")
    @PostMapping(value = "/insertTask")
    public ResponseUtil<Task> insertTask(@RequestBody Task task) {
        logger.info("insertTask and task:{}", JSON.toJSONString(task));
        ResponseUtil<Task> responseUtil = new ResponseUtil<>();
        try {
            responseUtil = taskService.insertTask(task);
        } catch (Exception e) {
            logger.error("insertTask and task:{}", JSON.toJSONString(task), e);
        }
        return responseUtil;
    }

    /**
     * @Description: 编辑任务
     * @title updateTaskById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "编辑任务")
    @PostMapping(value = "/updateTaskById")
    public ResponseUtil<Task> updateTaskById(@RequestBody Task task) {
        logger.info("updateTaskById and task:{}", JSON.toJSONString(task));
        ResponseUtil<Task> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(task.get_id())) {
            return responseUtil.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            responseUtil = taskService.updateTaskById(task);
        } catch (Exception e) {
            logger.error("updateTaskById and task:{}", JSON.toJSONString(task), e);

        }
        return responseUtil;
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
    public ResponseUtil<Task> deleteByIdBatch(@RequestParam(value = "ids") String[] ids) {
        logger.info("deleteByIdBatch and ids={}", JSON.toJSONString(ids));
        ResponseUtil<Task> responseUtil = new ResponseUtil<>();
        try {
            responseUtil = taskService.deleteByIdBatch(ids);
        } catch (Exception e) {
            logger.error("deleteByIdBatch and ids={}", JSON.toJSONString(ids), e);
        }
        return responseUtil;
    }
}
