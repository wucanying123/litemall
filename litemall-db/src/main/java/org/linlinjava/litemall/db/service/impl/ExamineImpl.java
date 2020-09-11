package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.dao.ExamineMapper;
import org.linlinjava.litemall.db.domain.Examine;
import org.linlinjava.litemall.db.service.ExamineService;
import org.linlinjava.litemall.db.util.Constant;
import org.linlinjava.litemall.db.util.DateUtil;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ExamineImpl implements ExamineService {

    @Autowired
    private ExamineMapper examineMapper;


    private static Logger logger = LoggerFactory.getLogger(ExamineImpl.class);

    @Override
    public PageInfo<Examine> selectExaminePage(Examine examine,Integer pageNum,Integer pageSize) {
        PageInfo<Examine> page = null;
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Examine> list = examineMapper.selectExaminePage(examine);
            String jsonString = JSON.toJSONString(list);
            page = new PageInfo<>(list);
        } catch (Exception e) {
            logger.error("selectExaminePage error and msg={}", e);
        }
        return page;
    }

    @Override
    public ResponseUtil<Examine> selectExamineById(String examineId) {
        ResponseUtil<Examine> responseUtil = new ResponseUtil<Examine>();
        try {
            Examine examine = examineMapper.selectByPrimaryKey(examineId);
            responseUtil.setData(examine);
            responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectExamineById error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Examine> insertExamine(Examine examine) {
        ResponseUtil<Examine> responseUtil = new ResponseUtil<Examine>();
        examine.setId(UUID.randomUUID().toString().replace("-", ""));
        try {
            examine.setCreateTime(DateUtil.getDateline());
            int n = examineMapper.insertSelective(examine);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertExamine error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Examine> updateExamineById(Examine examine) {
        ResponseUtil<Examine> responseUtil = new ResponseUtil<Examine>();
        try {
            examine.setUpdateTime(DateUtil.getDateline());
            int n = examineMapper.updateByPrimaryKeySelective(examine);
            if (n >= 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateExamineById error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Examine> deleteById(String id) {
        ResponseUtil<Examine> responseUtil = new ResponseUtil<Examine>();
        try {
            int m = examineMapper.deleteByPrimaryKey(id);
            if (m >= 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return responseUtil;
    }
}
