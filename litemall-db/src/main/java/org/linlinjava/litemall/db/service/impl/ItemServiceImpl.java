package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.linlinjava.litemall.db.domain.Task;
import org.linlinjava.litemall.db.service.TaskService;
import org.linlinjava.litemall.db.util.DateUtil;
import org.linlinjava.litemall.db.dao.ItemMapper;
import org.linlinjava.litemall.db.domain.Item;
import org.linlinjava.litemall.db.service.ItemService;
import org.linlinjava.litemall.db.util.StringUtilsXD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private TaskService taskService;


    private static Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Override
    public PageInfo<Item> selectItemPage(Item item, Integer pageNum, Integer pageSize) {
        PageInfo<Item> page = null;
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Item> list = itemMapper.selectItemPage(item);
            String jsonString = JSON.toJSONString(list);
            page = new PageInfo<>(list);
        } catch (Exception e) {
            logger.error("selectItemPage error and msg={}", e);
        }
        return page;
    }

    @Override
    public Item selectItemById(String itemId) {
        Item item = null;
        try {
            item = itemMapper.selectByPrimaryKey(itemId);
        } catch (Exception e) {
            logger.error("selectItemById error and msg={}", e);
        }
        return item;
    }

    @Override
    public int insertItem(Item item) {
        int n = 0;
        item.set_id(UUID.randomUUID().toString().replace("-", ""));
        try {
            long cuttentTime = DateUtil.getDateline();
            item.setCreateTime(cuttentTime);
            item.setUpdateTime(cuttentTime);
            n = itemMapper.insertSelective(item);
        } catch (Exception e) {
            logger.error("insertItem error and msg={}", e);
        }
        return n;
    }

    @Override
    public int updateItemById(Item item) {
        int n = 0;
        try {
            item.setUpdateTime(DateUtil.getDateline());
            n = itemMapper.updateByPrimaryKeySelective(item);
        } catch (Exception e) {
            logger.error("updateItemById error and msg={}", e);
        }
        return n;
    }

    @Override
    public int deleteById(String taskId,String itemId) {
        int n = 0;
        try {
            n = itemMapper.deleteByPrimaryKey(itemId);
            Task task = taskService.selectTaskById(taskId);
            if(null != task && StringUtilsXD.isNotEmpty(task.getItemsIds())) {
                String itemIds = task.getItemsIds();
                List<String> itemIdList = Arrays.asList(itemIds.split(","));
                Set<String> deleteItemIdList = new HashSet<>();
                deleteItemIdList.add(itemId);
                Set<String> itemIdSet = new HashSet(itemIdList);
                Set<String> subItemIdSet = new HashSet<String>();
                subItemIdSet.addAll(itemIdSet);
                subItemIdSet.removeAll(deleteItemIdList);
                List<String> newItemIdList = new ArrayList<>();
                if (null != subItemIdSet && subItemIdSet.size() > 0) {
                    for (String str : subItemIdSet) {
                        newItemIdList.add(str);
                    }
                }
                String itemIdStr = StringUtils.join(newItemIdList.toArray(), ",");
                task.setItemsIds(itemIdStr);
                n = taskService.updateTaskById(task);
            }
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return n;
    }
}
