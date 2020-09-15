package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.util.DateUtil;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.Constant;
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
    public ResponseUtil<Item> selectItemById(String itemId) {
        ResponseUtil<Item> responseUtil = new ResponseUtil<Item>();
        try {
            Item item = itemMapper.selectByPrimaryKey(itemId);
            responseUtil.setData(item);
            responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectItemById error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Item> insertItem(Item item) {
        ResponseUtil<Item> responseUtil = new ResponseUtil<Item>();
        item.set_id(UUID.randomUUID().toString().replace("-", ""));
        try {
            long cuttentTime = DateUtil.getDateline();
            item.setCreateTime(cuttentTime);
            item.setUpdateTime(cuttentTime);
            int n = itemMapper.insertSelective(item);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertItem error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Item> updateItemById(Item item) {
        ResponseUtil<Item> responseUtil = new ResponseUtil<Item>();
        try {
            item.setUpdateTime(DateUtil.getDateline());
            int n = itemMapper.updateByPrimaryKeySelective(item);
            if (n >= 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateItemById error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Item> deleteById(String id) {
        ResponseUtil<Item> responseUtil = new ResponseUtil<Item>();
        try {
            int m = itemMapper.deleteByPrimaryKey(id);
            if (m >= 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return responseUtil;
    }
}
