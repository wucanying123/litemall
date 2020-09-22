package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.dao.ExamineMapper;
import org.linlinjava.litemall.db.domain.*;
import org.linlinjava.litemall.db.service.*;
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
    @Autowired
    private TaskService taskService;
    @Autowired
    private SourceService sourceService;
    @Autowired
    private LiveService liveService;
    @Autowired
    private LitemallAdminService adminService;


    private static Logger logger = LoggerFactory.getLogger(ExamineImpl.class);

    @Override
    public PageInfo<Examine> selectExaminePage(Examine examine, Integer pageNum, Integer pageSize) {
        PageInfo<Examine> page = null;
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Examine> examineList = examineMapper.selectExaminePage(examine);
            String jsonString = JSON.toJSONString(examineList);
            page = new PageInfo<>(examineList);
            List<Examine> list = page.getList();
            if(null != list && list.size() >0){
                for(Examine examine1 : list){
                    if(examine1.getPassStatus() == 1 || examine1.getPassStatus() == 2 || examine1.getPassStatus() == 3){
                        examine1.setPassStatus1(examine1.getPassStatus());
                        examine1.setPassStatus2(null);
                    }else if(examine1.getPassStatus() == 4 || examine1.getPassStatus() == 5){
                        examine1.setPassStatus2(examine1.getPassStatus());
                        examine1.setPassStatus1(null);
                    }
                    //人员名称 1节目，2直播，3资源
                    if(examine1.getType() == 1){
                        Task task = taskService.selectTaskById(examine1.getDetailId());
                        if(null != task && null != task.getUserid()) {
                            LitemallAdmin admin = adminService.findById(task.getUserid());
                            examine1.setAddUserName(admin.getUsername());
                        }
                    }else if(examine1.getType() == 2){
                        Live live = liveService.selectLiveById(examine1.getDetailId());
                        if(null != live && null != live.getUserid()) {
                            LitemallAdmin admin = adminService.findById(live.getUserid());
                            examine1.setAddUserName(admin.getUsername());
                        }
                    }else if(examine1.getType() == 3){
                        Source source = sourceService.selectSourceById(examine1.getDetailId());
                        if(null != source && null != source.getUserid()) {
                            LitemallAdmin admin = adminService.findById(source.getUserid());
                            examine1.setAddUserName(admin.getUsername());
                        }
                    }
                    if(null != examine1.getCheckUserid1()) {
                        LitemallAdmin admin = adminService.findById(examine1.getCheckUserid1());
                        examine1.setCheckUserName1(admin.getUsername());
                    }
                    if(null != examine1.getCheckUserid2()) {
                        LitemallAdmin admin = adminService.findById(examine1.getCheckUserid2());
                        examine1.setCheckUserName2(admin.getUsername());
                    }

                }

            }
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
    public int deleteByDetailId(String detailId){
        int n = 0;
        try {
            n = examineMapper.deleteByDetailId(detailId);
        } catch (Exception e) {
            logger.error("deleteByDetailId error and msg={}", e);
        }
        return n;
    }

    @Override
    public int updateExamineDetailName(String detailId,String detailName) {
        Examine searchExamine = new Examine();
        searchExamine.setDetailId(detailId);
        Examine examine= examineMapper.selecByDetailId(detailId);
        int n = 0;
        if(null != examine) {
            examine.setDetailName(detailName);
            n = examineMapper.updateByPrimaryKeySelective(examine);
        }
        return n;
    }
}
