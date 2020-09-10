package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.Layer;
import org.linlinjava.litemall.db.domain.Layer;

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
    ResponseUtil<Layer> selectLayerById(String layerId);

    /**
     * @Description: 添加层
     * @title insertLayer
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    ResponseUtil<Layer> insertLayer(Layer layer);

    /**
     * @Description: 编辑层
     * @title updateLayerById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    ResponseUtil<Layer> updateLayerById(Layer layer);

    /**
     * @Description: 删除层
     * @Title: deleteById
     * @param id 层id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    ResponseUtil<Layer> deleteById(String id);
}
