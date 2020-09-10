package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageInfo;
import com.xinda.common.BaseResp;
import com.xinda.screen.model.entity.Program;

public interface ProgramService {

    /**
     * @Description: 获取命令列表
     * @title selectProgramPage
     * @param pageNum 开始页数
     * @param pageSize 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    PageInfo<Program> selectProgramPage(Program program,Integer pageNum,Integer pageSize);

    /**
     * @Description: 通过命令id查看命令详情
     * @Title: selectProgramById
     * @param programId 命令id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    BaseResp<Program> selectProgramById(String programId);

    /**
     * @Description: 添加命令
     * @title insertProgram
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    BaseResp<Program> insertProgram(Program program);

    /**
     * @Description: 编辑命令
     * @title updateProgramById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    BaseResp<Program> updateProgramById(Program program);

    /**
     * @Description: 删除命令
     * @Title: deleteByIdBatch
     * @param ids 命令id集合
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    BaseResp<Program> deleteByIdBatch(String[] ids);
}
