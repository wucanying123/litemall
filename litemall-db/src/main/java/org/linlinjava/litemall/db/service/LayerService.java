package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageInfo;
import com.xinda.common.BaseResp;
import com.xinda.screen.model.entity.Layer;

public interface LayerService {

    /**
     * @Description: 获取层列表
     * @title selectLayerPage
     * @param pageNum 开始页数
     * @param pageSize 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    PageInfo<Layer> selectLayerPage(Layer layer, Integer pageNum, Integer pageSize);

    /**
     * @Description: 通过层id查看层详情
     * @Title: selectLayerById
     * @param layerId 层id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    BaseResp<Layer> selectLayerById(String layerId);

    /**
     * @Description: 添加层
     * @title insertLayer
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    BaseResp<Layer> insertLayer(Layer layer);

    /**
     * @Description: 编辑层
     * @title updateLayerById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    BaseResp<Layer> updateLayerById(Layer layer);

    /**
     * @Description: 删除层
     * @Title: deleteByIdBatch
     * @param ids 层id集合
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    BaseResp<Layer> deleteByIdBatch(String[] ids);
}
