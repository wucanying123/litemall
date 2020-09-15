package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.domain.Layer;

public interface LayerService {

    /**
     * @param pageNum  开始页数
     * @param pageSize 每页条数
     * @Description: 获取层列表
     * @title selectLayerPage
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    PageInfo<Layer> selectLayerPage(Layer layer, Integer pageNum, Integer pageSize);

    /**
     * @param layerId 层id
     * @Description: 通过层id查看层详情
     * @Title: selectLayerById
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    Layer selectLayerById(String layerId);

    /**
     * @Description: 添加层
     * @title insertLayer
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    int insertLayer(Layer layer);

    /**
     * @Description: 编辑层
     * @title updateLayerById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    int updateLayerById(Layer layer);

    /**
     * @param id 层id
     * @Description: 删除层
     * @Title: deleteById
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    int deleteById(String id);
}
