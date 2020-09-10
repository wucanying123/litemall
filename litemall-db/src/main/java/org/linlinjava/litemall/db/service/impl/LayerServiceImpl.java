package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xinda.common.BaseResp;
import com.xinda.common.Constant;
import com.xinda.screen.dao.LayerMapper;
import com.xinda.screen.model.entity.Layer;
import com.xinda.screen.service.LayerService;
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
    public BaseResp<Layer> selectLayerById(String layerId) {
        BaseResp<Layer> baseResp = new BaseResp<Layer>();
        try {
            Layer layer = layerMapper.selectByPrimaryKey(layerId);
            baseResp.setData(layer);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectLayerById error and msg={}", e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Layer> insertLayer(Layer layer) {
        BaseResp<Layer> baseResp = new BaseResp<Layer>();
        layer.setId(UUID.randomUUID().toString().replace("-", ""));
        try {
            layer.setIsRepeat(layer.getRepeat());
            int n = layerMapper.insertSelective(layer);
            if (n == 1) {
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertLayer error and msg={}", e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Layer> updateLayerById(Layer layer) {
        BaseResp<Layer> baseResp = new BaseResp<Layer>();
        try {
            layer.setIsRepeat(layer.getRepeat());
            int n = layerMapper.updateByPrimaryKeySelective(layer);
            if (n >= 1) {
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateLayerById error and msg={}", e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Layer> deleteByIdBatch(String[] ids) {
        BaseResp<Layer> baseResp = new BaseResp<Layer>();
        try {
            int m = layerMapper.deleteByIdBatch(ids);
            if (m >= 1) {
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteByIdBatch error and msg={}", e);
        }
        return baseResp;
    }
}
