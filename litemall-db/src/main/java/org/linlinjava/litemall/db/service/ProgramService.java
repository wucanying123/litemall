package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.domain.Program;

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

    /**
     * @Description: 查看节目详情
     * @Title: readProgram
     * @param id 节目id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    Program readProgram(String id);

    /**
     * @Description: 编辑高级节目
     * @title updateComplexProgramById
     * @param type 0简易节目 1高级节目
     * @author IngaWu
     * @currentdate:2020年9月25日
     */
    void updateComplexProgramById(Program program,int type);

    /**
     * @Description: 获取层中资源总时长最大值
     * @title updateComplexProgramById
     * @param program 节目
     * @author IngaWu
     * @currentdate:2020年10月23日
     */
    Integer getMaxLayerPlaytime(Program program);
}
