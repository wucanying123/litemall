package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.linlinjava.litemall.db.dao.ItemMapper;
import org.linlinjava.litemall.db.dao.ScheduleMapper;
import org.linlinjava.litemall.db.domain.*;
import org.linlinjava.litemall.db.service.*;
import org.linlinjava.litemall.db.util.DateUtil;
import org.linlinjava.litemall.db.dao.TaskMapper;
import org.linlinjava.litemall.db.util.StringUtilsXD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private ExamineService examineService;
    @Autowired
    private LitemallAdminService adminService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ScheduleMapper scheduleMapper;


    private static Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Override
    public PageInfo<Task> selectTaskPage(Task task, Integer pageNum, Integer pageSize) {
        PageInfo<Task> page = null;
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Task> list = taskMapper.selectTaskPage(task);
            String jsonString = JSON.toJSONString(list);
            page = new PageInfo<>(list);
            List<Task> taskList = page.getList();
            if (null != taskList && taskList.size() > 0) {
                for (Task task1 : taskList) {
                    LitemallAdmin admin = adminService.findById(task1.getUserid());
                    task1.setUserName(admin.getUsername());
                }
            }
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
            examine.setType(1);
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
                examineService.updateExamineDetailName(task.get_id(), task.getName());
            }
        } catch (Exception e) {
            logger.error("updateTaskById error and msg={}", e);
        }
        return n;
    }

    @Override
    public int updateTaskTotalById(Task task) {
        int n = 0;
        try {
            task.setUpdateTime(DateUtil.getDateline());
            task.setCmdId(UUID.randomUUID().toString().replace("-", ""));
            List<Item> itemList = task.getItems();
            List<String> itemIdList = new ArrayList<>();
            if (null != itemList && itemList.size() > 0) {
                for (Item item : itemList) {
                    String itemId = item.get_id();
                    if (null == item.get_id()) {
                        Program program = item.get_program();
                        Integer itemVersion = item.getVersion();
                        item = createItemByProgramId(program.get_id(),itemVersion);
                        itemId = item.get_id();
                    }
                    itemIdList.add(itemId);
                }
            }
            String itemIdStr = StringUtils.join(itemIdList.toArray(), ",");
            task.setItemsIds(itemIdStr);
            n = taskMapper.updateByPrimaryKeySelective(task);
            //同步修改名称到审核表
            if (StringUtilsXD.isNotEmpty(task.get_id()) && StringUtilsXD.isNotEmpty(task.getName())) {
                examineService.updateExamineDetailName(task.get_id(), task.getName());
            }
        } catch (Exception e) {
            logger.error("updateTaskById error and msg={}", e);
        }
        return n;
    }

    private Item createItemByProgramId(String programId,Integer itemVersion) {
        Item item = new Item();
        item.setProgramId(programId);
        if(null == itemVersion) {
            item.setVersion(2);
        }else {
            item.setVersion(itemVersion);
        }
        item.setRepeatTimes(1);
        item.setPriority(0);
        item.set_id(UUID.randomUUID().toString().replace("-", ""));
        long cuttentTime = DateUtil.getDateline();
        item.setCreateTime(cuttentTime);
        item.setUpdateTime(cuttentTime);
        itemMapper.insertSelective(item);
        return item;
    }

    @Override
    public int deleteById(String id) {
        int n = 0;
        try {
            Task task = selectTaskById(id);
            if(null != task && StringUtilsXD.isNotEmpty(task.getItemsIds())) {
                String itemIds = task.getItemsIds();
                List<String> itemIdList = Arrays.asList(itemIds.split(","));
                for(String itemId:itemIdList){
                    Item item = itemService.selectItemById(itemId);
                    String scheduleIds = item.getSchedulesIds();
                    if (null != scheduleIds && scheduleIds.length() > 0) {
                        List<String> scheduleIdsList = Arrays.asList(scheduleIds.split(","));
                        for (String scheduleId : scheduleIdsList) {
                              scheduleMapper.deleteByPrimaryKey(scheduleId);
                        }
                    }
                    itemService.deleteById(id,itemId);
                }
            }
            n = taskMapper.deleteByPrimaryKey(id);
            //同步删除审核表数据
            examineService.deleteByDetailId(id);
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return n;
    }

    @Override
    public int insertQuickTask(String taskName,String programId, Integer userId,Integer itemVersion) {
        int n = 0;
        try {
            Task task = new Task();
            task.setUserid(userId);
            task.setName(taskName);
            Item item = createItemByProgramId(programId,itemVersion);
            String itemIdStr = item.get_id();
            task.setItemsIds(itemIdStr);
            n = insertTask(task);
        } catch (Exception e) {
            logger.error("insertQuickTask error and msg={}", e);
        }
        return n;
    }

    @Override
    public Task selectTaskByName(String taskName) {
        Task task = null;
        try {
            task = taskMapper.selectTaskByName(taskName);
        } catch (Exception e) {
            logger.error("selectTaskByName error and msg={}", e);
        }
        return task;
    }
}
