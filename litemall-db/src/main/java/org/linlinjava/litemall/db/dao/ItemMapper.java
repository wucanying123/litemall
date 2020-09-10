package org.linlinjava.litemall.db.dao;

import com.xinda.screen.model.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ItemMapper {

    List<Item> selectItemPage(@Param("item") Item item);

    Item selectByPrimaryKey(String id);

    int insertSelective(Item item);

    int updateByPrimaryKeySelective(Item item);

    int deleteByIdBatch(String[] ids);

    int deleteByPrimaryKey(String id);
}