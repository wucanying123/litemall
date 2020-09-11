package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.linlinjava.litemall.db.dao.LayerMapper;
import org.linlinjava.litemall.db.domain.Layer;
import org.linlinjava.litemall.db.service.LayerService;
import org.linlinjava.litemall.db.util.Constant;
import org.linlinjava.litemall.db.util.DateUtil;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LayerServiceImpl implements LayerService {

    @Autowired
    private LayerMapper layerMapper;

    private static Logger logger = LoggerFactory.getLogger(LayerServiceImpl.class);

    @Override
    public PageInfo<Layer> selectLayerPage(Layer layer, Integer pageNum, Integer pageSize) {
        PageInfo<Layer> page = null;
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Layer> list = layerMapper.selectLayerPage(layer);
            page = new PageInfo<>(list);
            List<Layer> list1 = page.getList();
            if(null != list1 && list1.size() > 0){
                for(Layer layer1:list1){
                    layer1.setRepeat(layer1.getIsRepeat());
                }
            }
            String jsonString = JSON.toJSONString(list1);
        } catch (Exception e) {
            logger.error("selectLayerPage error and msg={}", e);
        }
        return page;
    }

    @Override
    public ResponseUtil<Layer> selectLayerById(String layerId) {
        ResponseUtil<Layer> responseUtil = new ResponseUtil<Layer>();
        try {
            Layer layer = layerMapper.selectByPrimaryKey(layerId);
            responseUtil.setData(layer);
            responseUtil.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectLayerById error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Layer> insertLayer(Layer layer) {
        ResponseUtil<Layer> responseUtil = new ResponseUtil<Layer>();
        layer.setId(UUID.randomUUID().toString().replace("-", ""));
        try {
            layer.setIsRepeat(layer.getRepeat());
            layer.setCreateTime(DateUtil.getDateline());
            int n = layerMapper.insertSelective(layer);
            if (n == 1) {
                responseUtil.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertLayer error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Layer> updateLayerById(Layer layer) {
        ResponseUtil<Layer> responseUtil = new ResponseUtil<Layer>();
        try {
            layer.setUpdateTime(DateUtil.getDateline());
            layer.setIsRepeat(layer.getRepeat());
            int n = layerMapper.updateByPrimaryKeySelective(layer);
            if (n >= 1) {
                responseUtil.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateLayerById error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Layer> deleteById(String id) {
        ResponseUtil<Layer> responseUtil = new ResponseUtil<Layer>();
        try {
            int m = layerMapper.deleteByPrimaryKey(id);
            if (m >= 1) {
                responseUtil.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return responseUtil;
    }
}
