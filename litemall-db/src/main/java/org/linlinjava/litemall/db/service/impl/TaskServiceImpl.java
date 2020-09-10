package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.Constant;
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
    public ResponseUtil<Task> selectTaskById(String taskId) {
        ResponseUtil<Task> responseUtil = new ResponseUtil<Task>();
        try {
            Task task = taskMapper.selectByPrimaryKey(taskId);
            responseUtil.setData(task);
            responseUtil.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectTaskById error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Task> insertTask(Task task) {
        ResponseUtil<Task> responseUtil = new ResponseUtil<Task>();
        task.set_id(UUID.randomUUID().toString().replace("-", ""));
        try {
            int n = taskMapper.insertSelective(task);
            if (n == 1) {
                responseUtil.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertTask error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Task> updateTaskById(Task task) {
        ResponseUtil<Task> responseUtil = new ResponseUtil<Task>();
        try {
            int n = taskMapper.updateByPrimaryKeySelective(task);
            if (n >= 1) {
                responseUtil.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateTaskById error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Task> deleteById(String id) {
        ResponseUtil<Task> responseUtil = new ResponseUtil<Task>();
        try {
            int m = taskMapper.deleteByPrimaryKey(id);
            if (m >= 1) {
                responseUtil.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return responseUtil;
    }
}
