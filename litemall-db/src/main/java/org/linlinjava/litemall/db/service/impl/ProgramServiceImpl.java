package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.dao.ProgramMapper;
import org.linlinjava.litemall.db.service.ProgramService;
import org.linlinjava.litemall.db.util.Constant;
import org.linlinjava.litemall.db.util.DateUtil;
import org.linlinjava.litemall.db.util.ResponseUtil;
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
    public ResponseUtil<Program> selectProgramById(String programId) {
        ResponseUtil<Program> responseUtil = new ResponseUtil<Program>();
        try {
            Program program = programMapper.selectByPrimaryKey(programId);
            responseUtil.setData(program);
            responseUtil.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectProgramById error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Program> insertProgram(Program program) {
        ResponseUtil<Program> responseUtil = new ResponseUtil<Program>();
        program.set_id(UUID.randomUUID().toString().replace("-", ""));
        try {
            program.setCreateTime(DateUtil.getDateline());
            int n = programMapper.insertSelective(program);
            if (n == 1) {
                responseUtil.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertProgram error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Program> updateProgramById(Program program) {
        ResponseUtil<Program> responseUtil = new ResponseUtil<Program>();
        try {
            program.setUpdateTime(DateUtil.getDateline());
            int n = programMapper.updateByPrimaryKeySelective(program);
            if (n >= 1) {
                responseUtil.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateProgramById error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Program> deleteById(String id) {
        ResponseUtil<Program> responseUtil = new ResponseUtil<Program>();
        try {
            int m = programMapper.deleteByPrimaryKey(id);
            if (m >= 1) {
                responseUtil.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return responseUtil;
    }
}
