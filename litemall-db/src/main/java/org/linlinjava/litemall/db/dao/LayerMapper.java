package org.linlinjava.litemall.db.dao;

import com.xinda.screen.model.entity.Layer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LayerMapper {

    List<Layer> selectLayerPage(@Param("layer") Layer layer);

    Layer selectByPrimaryKey(String id);

    int insertSelective(Layer layer);

    int updateByPrimaryKeySelective(Layer layer);

    int deleteByIdBatch(String[] ids);

    int deleteByPrimaryKey(String id);
}