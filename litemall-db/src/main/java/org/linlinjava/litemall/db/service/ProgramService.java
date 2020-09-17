package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.domain.Program;

import javax.validation.constraints.NotNull;

public interface ProgramService {

    /**
     * @Description: 获取节目列表
     * @title selectProgramPage
     * @param pageNum 开始页数
     * @param pageSize 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    PageInfo<Program> selectProgramPage(Program program,Integer pageNum,Integer pageSize);

    /**
     * @Description: 通过节目id查看节目详情
     * @Title: selectProgramById
     * @param programId 节目id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    Program selectProgramById(String programId);

    /**
     * @Description: 添加节目
     * @title insertProgram
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    int insertProgram(Program program);

    /**
     * @Description: 编辑节目
     * @title updateProgramById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    int updateProgramById(Program program);

    /**
     * @Description: 删除节目
     * @Title: deleteById
     * @param id 节目id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    int deleteById(String id);

    Program readProgram(String id);

    void updatePlaySources(Program program);
}
