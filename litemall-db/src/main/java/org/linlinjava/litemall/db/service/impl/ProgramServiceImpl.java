package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.dao.ProgramMapper;
import org.linlinjava.litemall.db.service.ProgramService;
import org.linlinjava.litemall.db.util.DateUtil;
import org.linlinjava.litemall.db.domain.Program;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProgramServiceImpl implements ProgramService {

    @Autowired
    private ProgramMapper programMapper;


    private static Logger logger = LoggerFactory.getLogger(ProgramServiceImpl.class);

    @Override
    public PageInfo<Program> selectProgramPage(Program program, Integer pageNum, Integer pageSize) {
        PageInfo<Program> page = null;
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Program> list = programMapper.selectProgramPage(program);
            String jsonString = JSON.toJSONString(list);
            page = new PageInfo<>(list);
        } catch (Exception e) {
            logger.error("selectProgramPage error and msg={}", e);
        }
        return page;
    }

    @Override
    public Program selectProgramById(String programId) {
        Program program = null;
        try {
            program = programMapper.selectByPrimaryKey(programId);
        } catch (Exception e) {
            logger.error("selectProgramById error and msg={}", e);
        }
        return program;
    }

    @Override
    public int insertProgram(Program program) {
        int n = 0;
        program.set_id(UUID.randomUUID().toString().replace("-", ""));
        try {
            long cuttentTime = DateUtil.getDateline();
            program.setCreateTime(cuttentTime);
            program.setUpdateTime(cuttentTime);
            n = programMapper.insertSelective(program);
        } catch (Exception e) {
            logger.error("insertProgram error and msg={}", e);
        }
        return n;
    }

    @Override
    public int updateProgramById(Program program) {
        int n = 0;
        try {
            program.setUpdateTime(DateUtil.getDateline());
            n = programMapper.updateByPrimaryKeySelective(program);
        } catch (Exception e) {
            logger.error("updateProgramById error and msg={}", e);
        }
        return n;
    }

    @Override
    public int deleteById(String id) {
        int n = 0;
        try {
            n = programMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return n;
    }
}
