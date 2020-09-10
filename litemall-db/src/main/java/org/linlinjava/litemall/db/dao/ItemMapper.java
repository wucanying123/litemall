package org.linlinjava.litemall.db.dao;

import org.linlinjava.litemall.db.domain.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.Item;

import java.util.List;

@Mapper
public interface ItemMapper {

    List<Item> selectItemPage(@Param("item") Item item);

    Item selectByPrimaryKey(String id);

    int insertSelective(Item item);

    int updateByPrimaryKeySelective(Item item);

    int deleteById(String ids);

    int deleteByPrimaryKey(String id);
}