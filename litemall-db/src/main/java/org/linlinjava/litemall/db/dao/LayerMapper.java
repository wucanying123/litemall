package org.linlinjava.litemall.db.dao;

import org.linlinjava.litemall.db.domain.Layer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LayerMapper {

    List<Layer> selectLayerPage(@Param("layer") Layer layer);

    Layer selectByPrimaryKey(String id);

    int insertSelective(Layer layer);

    int updateByPrimaryKeySelective(Layer layer);

    int deleteById(String ids);

    int deleteByPrimaryKey(String id);
}