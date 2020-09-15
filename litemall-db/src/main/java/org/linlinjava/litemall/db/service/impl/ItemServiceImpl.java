package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.util.DateUtil;
import org.linlinjava.litemall.db.dao.ItemMapper;
import org.linlinjava.litemall.db.domain.Item;
import org.linlinjava.litemall.db.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;


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
    public int deleteById(String id) {
        int n = 0;
        try {
            n = itemMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return n;
    }
}
