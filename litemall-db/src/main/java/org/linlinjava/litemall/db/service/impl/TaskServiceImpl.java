package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xinda.common.BaseResp;
import com.xinda.common.Constant;
import com.xinda.screen.dao.TaskMapper;
import com.xinda.screen.model.entity.Task;
import com.xinda.screen.service.TaskService;
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
    public PageInfo<Task> selectTaskPage(Task task,Integer pageNum,Integer pageSize) {
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
    public BaseResp<Task> selectTaskById(String taskId) {
        BaseResp<Task> baseResp = new BaseResp<Task>();
        try {
            Task task = taskMapper.selectByPrimaryKey(taskId);
            baseResp.setData(task);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectTaskById error and msg={}", e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Task> insertTask(Task task) {
        BaseResp<Task> baseResp = new BaseResp<Task>();
        task.set_id(UUID.randomUUID().toString().replace("-", ""));
        try {
            int n = taskMapper.insertSelective(task);
            if (n == 1) {
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertTask error and msg={}", e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Task> updateTaskById(Task task) {
        BaseResp<Task> baseResp = new BaseResp<Task>();
        try {
            int n = taskMapper.updateByPrimaryKeySelective(task);
            if (n >= 1) {
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateTaskById error and msg={}", e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Task> deleteByIdBatch(String[] ids) {
        BaseResp<Task> baseResp = new BaseResp<Task>();
        try {
            int m = taskMapper.deleteByIdBatch(ids);
            if (m >= 1) {
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteByIdBatch error and msg={}", e);
        }
        return baseResp;
    }
}
