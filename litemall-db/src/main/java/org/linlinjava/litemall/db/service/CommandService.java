package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.Command;

public interface CommandService {

    /**
     * @Description: 获取命令列表
     * @title selectCommandPage
     * @param pageNum 开始页数
     * @param pageSize 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    PageInfo<Command> selectCommandPage(Command command,Integer pageNum,Integer pageSize);

    /**
     * @Description: 通过命令id查看命令详情
     * @Title: selectCommandById
     * @param commandId 命令id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    ResponseUtil<Command> selectCommandById(String commandId);

    /**
     * @Description: 添加命令
     * @title insertCommand
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    ResponseUtil<Command> insertCommand(Command command);

    /**
     * @Description: 编辑命令
     * @title updateCommandById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    ResponseUtil<Command> updateCommandById(Command command);

    /**
     * @Description: 删除命令
     * @Title: deleteByIdBatch
     * @param ids 命令id集合
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    ResponseUtil<Command> deleteByIdBatch(String[] ids);
}
