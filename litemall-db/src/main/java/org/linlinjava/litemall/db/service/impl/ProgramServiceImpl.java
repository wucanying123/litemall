package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xinda.common.BaseResp;
import com.xinda.common.Constant;
import com.xinda.screen.dao.ProgramMapper;
import com.xinda.screen.model.entity.Program;
import com.xinda.screen.service.ProgramService;
import com.xinda.util.DateUtil;
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
    public PageInfo<Program> selectProgramPage(Program program,Integer pageNum,Integer pageSize) {
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
    public BaseResp<Program> selectProgramById(String programId) {
        BaseResp<Program> baseResp = new BaseResp<Program>();
        try {
            Program program = programMapper.selectByPrimaryKey(programId);
            baseResp.setData(program);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectProgramById error and msg={}", e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Program> insertProgram(Program program) {
        BaseResp<Program> baseResp = new BaseResp<Program>();
        program.set_id(UUID.randomUUID().toString().replace("-", ""));
        try {
            program.setCreateTime(DateUtil.getDateline());
            int n = programMapper.insertSelective(program);
            if (n == 1) {
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertProgram error and msg={}", e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Program> updateProgramById(Program program) {
        BaseResp<Program> baseResp = new BaseResp<Program>();
        try {
            int n = programMapper.updateByPrimaryKeySelective(program);
            if (n >= 1) {
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateProgramById error and msg={}", e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Program> deleteByIdBatch(String[] ids) {
        BaseResp<Program> baseResp = new BaseResp<Program>();
        try {
            int m = programMapper.deleteByIdBatch(ids);
            if (m >= 1) {
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteByIdBatch error and msg={}", e);
        }
        return baseResp;
    }
}
