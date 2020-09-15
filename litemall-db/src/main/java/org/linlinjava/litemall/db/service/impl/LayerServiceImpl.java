package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.linlinjava.litemall.db.dao.LayerMapper;
import org.linlinjava.litemall.db.domain.Layer;
import org.linlinjava.litemall.db.service.LayerService;
import org.linlinjava.litemall.db.util.DateUtil;
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
            if (null != list1 && list1.size() > 0) {
                for (Layer layer1 : list1) {
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
    public Layer selectLayerById(String layerId) {
        Layer layer = null;
        try {
            layer = layerMapper.selectByPrimaryKey(layerId);
        } catch (Exception e) {
            logger.error("selectLayerById error and msg={}", e);
        }
        return layer;
    }

    @Override
    public int insertLayer(Layer layer) {
        int n = 0;
        layer.setId(UUID.randomUUID().toString().replace("-", ""));
        try {
            layer.setIsRepeat(layer.getRepeat());
            long cuttentTime = DateUtil.getDateline();
            layer.setCreateTime(cuttentTime);
            layer.setUpdateTime(cuttentTime);
            n = layerMapper.insertSelective(layer);
        } catch (Exception e) {
            logger.error("insertLayer error and msg={}", e);
        }
        return n;
    }

    @Override
    public int updateLayerById(Layer layer) {
        int n = 0;
        try {
            layer.setUpdateTime(DateUtil.getDateline());
            layer.setIsRepeat(layer.getRepeat());
            n = layerMapper.updateByPrimaryKeySelective(layer);
        } catch (Exception e) {
            logger.error("updateLayerById error and msg={}", e);
        }
        return n;
    }

    @Override
    public int deleteById(String id) {
        int n = 0;
        try {
            n = layerMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return n;
    }
}
