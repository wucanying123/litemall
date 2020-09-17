package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.linlinjava.litemall.db.domain.LitemallAdmin;
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
    public ResponseUtil selectTaskPage(String name, String _department, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        ResponseUtil responseUtil = new ResponseUtil();
        Task task = new Task();
        task.setName(name);
        task.set_department(_department);
        logger.info("selectTaskPage and task={},page={},limit", JSON.toJSONString(task), page, limit);
        try {
            PageInfo<Task> pageList = taskService.selectTaskPage(task, StringUtilsXD.checkPageNumParam(page), StringUtilsXD.checkPageSizeParam(limit));
            return responseUtil.succeedPage(pageList);
        } catch (Exception e) {
            logger.error("selectTaskPage and task={},page={},limit", JSON.toJSONString(task), page, limit, e);
        }
        return responseUtil;
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
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            Task task = taskService.selectTaskById(taskId);
            responseUtil.setData(task);
            responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
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
            Subject currentUser = SecurityUtils.getSubject();
            LitemallAdmin admin = (LitemallAdmin) currentUser.getPrincipal();
            task.setUserid(admin.getId());
            int n = taskService.insertTask(task);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
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
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            int n = taskService.updateTaskById(task);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateTaskById and task:{}", JSON.toJSONString(task), e);

        }
        return responseUtil;
    }

    /**
     * @Description: 删除任务
     * @Title: deleteById
     * @param id 任务id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "删除任务")
    @PostMapping(value = "/deleteById")
    public ResponseUtil<Task> deleteById(@RequestParam(value = "id") String id) {
        logger.info("deleteById and id={}", JSON.toJSONString(id));
        ResponseUtil<Task> responseUtil = new ResponseUtil<>();
        try {
            int n = taskService.deleteById(id);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteById and id={}", JSON.toJSONString(id), e);
        }
        return responseUtil;
    }
}
