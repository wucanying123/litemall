package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.dao.LiveMapper;
import org.linlinjava.litemall.db.domain.Live;
import org.linlinjava.litemall.db.service.LiveService;
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
public class LiveServiceImpl implements LiveService {

    @Autowired
    private LiveMapper liveMapper;


    private static Logger logger = LoggerFactory.getLogger(LiveServiceImpl.class);

    @Override
    public PageInfo<Live> selectLivePage(Live live,Integer pageNum,Integer pageSize) {
        PageInfo<Live> page = null;
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Live> list = liveMapper.selectLivePage(live);
            String jsonString = JSON.toJSONString(list);
            page = new PageInfo<>(list);
        } catch (Exception e) {
            logger.error("selectLivePage error and msg={}", e);
        }
        return page;
    }

    @Override
    public ResponseUtil<Live> selectLiveById(String liveId) {
        ResponseUtil<Live> responseUtil = new ResponseUtil<Live>();
        try {
            Live live = liveMapper.selectByPrimaryKey(liveId);
            responseUtil.setData(live);
            responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectLiveById error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Live> insertLive(Live live) {
        ResponseUtil<Live> responseUtil = new ResponseUtil<Live>();
        live.setId(UUID.randomUUID().toString().replace("-", ""));
        try {
            long cuttentTime = DateUtil.getDateline();
            live.setCreateTime(cuttentTime);
            live.setUpdateTime(cuttentTime);
            int n = liveMapper.insertSelective(live);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertLive error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Live> updateLiveById(Live live) {
        ResponseUtil<Live> responseUtil = new ResponseUtil<Live>();
        try {
            live.setUpdateTime(DateUtil.getDateline());
            int n = liveMapper.updateByPrimaryKeySelective(live);
            if (n >= 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateLiveById error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Live> deleteById(String id) {
        ResponseUtil<Live> responseUtil = new ResponseUtil<Live>();
        try {
            int m = liveMapper.deleteByPrimaryKey(id);
            if (m >= 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return responseUtil;
    }
}
