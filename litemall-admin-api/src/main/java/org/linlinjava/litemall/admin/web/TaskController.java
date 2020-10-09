package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.linlinjava.litemall.core.util.JacksonUtil;
import org.linlinjava.litemall.db.domain.Item;
import org.linlinjava.litemall.db.domain.LitemallAdmin;
import org.linlinjava.litemall.db.domain.Task;
import org.linlinjava.litemall.db.service.ScreenService;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.Constant;
import org.linlinjava.litemall.db.service.TaskService;
import org.linlinjava.litemall.db.util.StringUtilsXD;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/screen/task")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private ScreenService screenService;

    private static Logger logger = LoggerFactory.getLogger(TaskController.class);

    /**
     * @param page  开始页数
     * @param limit 每页条数
     * @Description: 获取任务列表
     * @title selectTaskPage
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "获取任务列表")
    @GetMapping("/selectTaskPage")
    public ResponseUtil selectTaskPage(String name, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        ResponseUtil responseUtil = new ResponseUtil();
        Task task = new Task();
        task.setName(name);
        logger.info("selectTaskPage and task={},page={},limit={}", JSON.toJSONString(task), page, limit);
        try {
            PageInfo<Task> pageList = taskService.selectTaskPage(task, StringUtilsXD.checkPageNumParam(page), StringUtilsXD.checkPageSizeParam(limit));
            return responseUtil.succeedPage(pageList);
        } catch (Exception e) {
            logger.error("selectTaskPage and task={},page={},limit={}", JSON.toJSONString(task), page, limit, e);
        }
        return responseUtil;
    }

    /**
     * @param taskId 任务id
     * @Description: 通过任务id查看任务详情
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

    @ApiOperation(value = "编辑完整任务")
    @PostMapping(value = "/updateTaskTotalById")
    public ResponseUtil<Task> updateTaskTotalById(@RequestBody Task task) {
        logger.info("updateTaskTotalById and task:{}", JSON.toJSONString(task));
        ResponseUtil<Task> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(task.get_id())) {
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            int n = taskService.updateTaskTotalById(task);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateTaskTotalById and task:{}", JSON.toJSONString(task), e);

        }
        return responseUtil;
    }

    /**
     * @param id 任务id
     * @Description: 删除任务
     * @Title: deleteById
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

    /**
     * @Description: 播放任务
     * @Title: playTask
     * @auther IngaWu
     * @currentdate:2020年9月22日
     */
    @ApiOperation(value = "播放任务")
    @PostMapping(value = "/playTask")
    public ResponseUtil<Object> playTask(@RequestBody String body) {
        String id = JacksonUtil.parseString(body, "id");
        List<String> selectCardIds = JacksonUtil.parseStringList(body, "selectCardIds");
        logger.info("playTask and id ={},selectCardIds={}", id,JSON.toJSONString(selectCardIds));
        if (null == selectCardIds || selectCardIds.size() < 1) {
            ResponseUtil<Object> responseUtil = new ResponseUtil<>();
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_03, Constant.RTNINFO_SYS_03);
        }
        return screenService.playXixunTask(id, selectCardIds);
    }

    /**
     * @Description: 快速创建任务
     * @Title: insertQuickTask
     * @auther IngaWu
     * @currentdate:2020年9月22日
     */
    @ApiOperation(value = "快速创建任务")
    @PostMapping(value = "/insertQuickTask")
    public ResponseUtil<Task> insertQuickTask(@RequestParam(value = "programName") String programName,
                                              @RequestParam(value = "programId") String programId,
                                              @RequestParam(value = "itemVersion") Integer itemVersion) {
        logger.info("insertQuickTask and programId:{}", programId);
        ResponseUtil<Task> responseUtil = new ResponseUtil<>();
        try {
            String taskName = programName + "_Task";
            Task existTask = taskService.selectTaskByName(taskName);
            if (null != existTask) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_04, Constant.RTNINFO_SYS_04);
                return responseUtil;
            }
            Subject currentUser = SecurityUtils.getSubject();
            LitemallAdmin admin = (LitemallAdmin) currentUser.getPrincipal();
            Integer userId = admin.getId();
            int n = taskService.insertQuickTask(taskName, programId, userId, itemVersion);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertQuickTask and programId:{}", programId, e);
        }
        return responseUtil;
    }

    @ApiOperation(value = "任务进度")
    @RequestMapping(value = "/nocice",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public Object demo(@RequestBody JSONObject json) throws Exception{
        try {
            System.out.println("输出："+json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseUtil.ok(json);
    }
}
