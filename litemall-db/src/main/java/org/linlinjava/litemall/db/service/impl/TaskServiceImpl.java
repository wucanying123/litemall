package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.domain.Examine;
import org.linlinjava.litemall.db.service.ExamineService;
import org.linlinjava.litemall.db.util.DateUtil;
import org.linlinjava.litemall.db.dao.TaskMapper;
import org.linlinjava.litemall.db.domain.Task;
import org.linlinjava.litemall.db.service.TaskService;
import org.linlinjava.litemall.db.util.StringUtilsXD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private ExamineService examineService;


    private static Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Override
    public PageInfo<Task> selectTaskPage(Task task, Integer pageNum, Integer pageSize) {
        PageInfo<Task> page = null;
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Task> list = taskMapper.selectTaskPage(task);
            String jsonString = JSON.toJSONString(list);
            page = new PageInfo<>(list);
        } catch (Exception e) {
            logger.error("selectTaskPage error and msg={}", e);
        }
        return page;
    }

    @Override
    public Task selectTaskById(String taskId) {
        Task task = null;
        try {
            task = taskMapper.selectByPrimaryKey(taskId);
        } catch (Exception e) {
            logger.error("selectTaskById error and msg={}", e);
        }
        return task;
    }

    @Override
    public int insertTask(Task task) {
        int n = 0;
        task.set_id(UUID.randomUUID().toString().replace("-", ""));
        try {
            long cuttentTime = DateUtil.getDateline();
            task.setCreateTime(cuttentTime);
            task.setUpdateTime(cuttentTime);
            task.setCmdId(UUID.randomUUID().toString().replace("-", ""));
            n = taskMapper.insertSelective(task);
            //同步添加到审核表
            Examine examine = new Examine();
            examine.setPassStatus(1);
            examine.setType(3);
            examine.setDetailId(task.get_id());
            examine.setDetailName(task.getName());
            examineService.insertExamine(examine);
        } catch (Exception e) {
            logger.error("insertTask error and msg={}", e);
        }
        return n;
    }

    @Override
    public int updateTaskById(Task task) {
        int n = 0;
        try {
            task.setUpdateTime(DateUtil.getDateline());
            task.setCmdId(UUID.randomUUID().toString().replace("-", ""));
            n = taskMapper.updateByPrimaryKeySelective(task);
            //同步修改名称到审核表
            if (StringUtilsXD.isNotEmpty(task.get_id()) && StringUtilsXD.isNotEmpty(task.getName())) {
                examineService.updateExamineDetailName(task.get_id(),task.getName());
            }
        } catch (Exception e) {
            logger.error("updateTaskById error and msg={}", e);
        }
        return n;
    }

    @Override
    public int deleteById(String id) {
        int n = 0;
        try {
            n = taskMapper.deleteByPrimaryKey(id);
            //同步删除审核表数据
            examineService.deleteByDetailId(id);
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return n;
    }
}
