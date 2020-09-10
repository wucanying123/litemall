package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xinda.common.BaseResp;
import com.xinda.common.Constant;
import com.xinda.screen.dao.ItemMapper;
import com.xinda.screen.model.entity.Item;
import com.xinda.screen.service.ItemService;
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
    public PageInfo<Item> selectItemPage(Item item,Integer pageNum,Integer pageSize) {
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
    public BaseResp<Item> selectItemById(String itemId) {
        BaseResp<Item> baseResp = new BaseResp<Item>();
        try {
            Item item = itemMapper.selectByPrimaryKey(itemId);
            baseResp.setData(item);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectItemById error and msg={}", e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Item> insertItem(Item item) {
        BaseResp<Item> baseResp = new BaseResp<Item>();
        item.set_id(UUID.randomUUID().toString().replace("-", ""));
        try {
            int n = itemMapper.insertSelective(item);
            if (n == 1) {
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertItem error and msg={}", e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Item> updateItemById(Item item) {
        BaseResp<Item> baseResp = new BaseResp<Item>();
        try {
            int n = itemMapper.updateByPrimaryKeySelective(item);
            if (n >= 1) {
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateItemById error and msg={}", e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Item> deleteByIdBatch(String[] ids) {
        BaseResp<Item> baseResp = new BaseResp<Item>();
        try {
            int m = itemMapper.deleteByIdBatch(ids);
            if (m >= 1) {
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteByIdBatch error and msg={}", e);
        }
        return baseResp;
    }
}
