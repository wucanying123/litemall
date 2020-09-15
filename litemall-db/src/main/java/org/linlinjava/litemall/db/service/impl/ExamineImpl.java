package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.dao.ExamineMapper;
import org.linlinjava.litemall.db.domain.Examine;
import org.linlinjava.litemall.db.service.ExamineService;
import org.linlinjava.litemall.db.util.DateUtil;
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
    public PageInfo<Examine> selectExaminePage(Examine examine, Integer pageNum, Integer pageSize) {
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
    public Examine selectExamineById(String examineId) {
        Examine examine = null;
        try {
            examine = examineMapper.selectByPrimaryKey(examineId);
        } catch (Exception e) {
            logger.error("selectExamineById error and msg={}", e);
        }
        return examine;
    }

    @Override
    public Examine selecByDetailId(String detailId) {
        Examine examine = null;
        try {
            examine = examineMapper.selecByDetailId(detailId);
        } catch (Exception e) {
            logger.error("selecByDetailId error and msg={}", e);
        }
        return examine;
    }

    @Override
    public int insertExamine(Examine examine) {
        int n = 0;
        examine.setId(UUID.randomUUID().toString().replace("-", ""));
        try {
            long cuttentTime = DateUtil.getDateline();
            examine.setCreateTime(cuttentTime);
            examine.setUpdateTime(cuttentTime);
            n = examineMapper.insertSelective(examine);
        } catch (Exception e) {
            logger.error("insertExamine error and msg={}", e);
        }
        return n;
    }

    @Override
    public int updateExamineById(Examine examine) {
        int n = 0;
        try {
            examine.setUpdateTime(DateUtil.getDateline());
            if(2 == examine.getPassStatus()){
                examine.setRejectReason1("");
            }else if(4 == examine.getPassStatus()){
                examine.setRejectReason2("");
            }
            n = examineMapper.updateByPrimaryKeySelective(examine);
        } catch (Exception e) {
            logger.error("updateExamineById error and msg={}", e);
        }
        return n;
    }

    @Override
    public int deleteById(String id) {
        int n = 0;
        try {
            n = examineMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return n;
    }

    @Override
    public int updateExamineDetailName(String detailId,String detailName) {
        Examine searchExamine = new Examine();
        searchExamine.setDetailId(detailId);
        Examine examine= examineMapper.selecByDetailId(detailId);
        examine.setDetailName(detailName);
        int n = examineMapper.updateByPrimaryKeySelective(examine);
        return n;
    }
}
