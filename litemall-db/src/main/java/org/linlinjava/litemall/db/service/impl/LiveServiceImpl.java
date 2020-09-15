package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.dao.LiveMapper;
import org.linlinjava.litemall.db.domain.Live;
import org.linlinjava.litemall.db.service.LiveService;
import org.linlinjava.litemall.db.util.DateUtil;
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
    public PageInfo<Live> selectLivePage(Live live, Integer pageNum, Integer pageSize) {
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
    public Live selectLiveById(String liveId) {
        Live live = null;
        try {
            live = liveMapper.selectByPrimaryKey(liveId);
        } catch (Exception e) {
            logger.error("selectLiveById error and msg={}", e);
        }
        return live;
    }

    @Override
    public int insertLive(Live live) {
        int n = 0;
        live.setId(UUID.randomUUID().toString().replace("-", ""));
        try {
            long cuttentTime = DateUtil.getDateline();
            live.setCreateTime(cuttentTime);
            live.setUpdateTime(cuttentTime);
            n = liveMapper.insertSelective(live);
        } catch (Exception e) {
            logger.error("insertLive error and msg={}", e);
        }
        return n;
    }

    @Override
    public int updateLiveById(Live live) {
        int n = 0;
        try {
            live.setUpdateTime(DateUtil.getDateline());
            n = liveMapper.updateByPrimaryKeySelective(live);
        } catch (Exception e) {
            logger.error("updateLiveById error and msg={}", e);
        }
        return n;
    }

    @Override
    public int deleteById(String id) {
        int n = 0;
        try {
            n = liveMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return n;
    }
}
