package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.Item;

public interface ItemService {

    /**
     * @Description: 获取项目列表
     * @title selectItemPage
     * @param pageNum 开始页数
     * @param pageSize 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    PageInfo<Item> selectItemPage(Item item,Integer pageNum,Integer pageSize);

    /**
     * @Description: 通过项目id查看项目详情
     * @Title: selectItemById
     * @param itemId 项目id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    ResponseUtil<Item> selectItemById(String itemId);

    /**
     * @Description: 添加项目
     * @title insertItem
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    ResponseUtil<Item> insertItem(Item item);

    /**
     * @Description: 编辑项目
     * @title updateItemById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    ResponseUtil<Item> updateItemById(Item item);

    /**
     * @Description: 删除项目
     * @Title: deleteById
     * @param id 项目id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    ResponseUtil<Item> deleteById(String id);
}
