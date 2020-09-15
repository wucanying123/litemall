package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.util.DateUtil;
import org.linlinjava.litemall.db.dao.TaskMapper;
import org.linlinjava.litemall.db.domain.Task;
import org.linlinjava.litemall.db.service.TaskService;
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
            n = taskMapper.insertSelective(task);
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
            n = taskMapper.updateByPrimaryKeySelective(task);
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

        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return n;
    }
}
