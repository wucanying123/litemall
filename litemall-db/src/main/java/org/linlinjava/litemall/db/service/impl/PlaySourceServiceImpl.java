package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.dao.PlaySourceMapper;
import org.linlinjava.litemall.db.domain.Examine;
import org.linlinjava.litemall.db.domain.PlaySource;
import org.linlinjava.litemall.db.service.ExamineService;
import org.linlinjava.litemall.db.service.PlaySourceService;
import org.linlinjava.litemall.db.util.DateUtil;
import org.linlinjava.litemall.db.util.StringUtilsXD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PlaySourceServiceImpl implements PlaySourceService {

    @Autowired
    private PlaySourceMapper playSourceMapper;
    @Autowired
    private ExamineService examineService;


    private static Logger logger = LoggerFactory.getLogger(PlaySourceServiceImpl.class);

    @Override
    public PageInfo<PlaySource> selectPlaySourcePage(PlaySource playSource, Integer pageNum, Integer pageSize) {
        PageInfo<PlaySource> page = null;
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<PlaySource> list = playSourceMapper.selectPlaySourcePage(playSource);
            page = new PageInfo<>(list);
            List<PlaySource> list1 = page.getList();
            if (null != list1 && list1.size() > 0) {
                for (PlaySource playSource1 : list1) {
                    playSource1.setLeft(playSource1.getTheLeft());
                }
            }
            String jsonString = JSON.toJSONString(list1);
        } catch (Exception e) {
            logger.error("selectPlaySourcePage error and msg={}", e);
        }
        return page;
    }

    @Override
    public List<PlaySource> selectPlaySourceList(PlaySource playSource) {
        List<PlaySource> list = null;
        try {
            list = playSourceMapper.selectPlaySourcePage(playSource);
        } catch (Exception e) {
            logger.error("selectPlaySourcePage error and msg={}", e);
        }
        return list;
    }

    @Override
    public PlaySource selectPlaySourceById(String playSourceId) {
        PlaySource playSource = null;
        try {
            playSource = playSourceMapper.selectByPrimaryKey(playSourceId);
        } catch (Exception e) {
            logger.error("selectPlaySourceById error and msg={}", e);
        }
        return playSource;
    }

    @Override
    public int insertPlaySource(PlaySource playSource) {
        int n = 0;
        playSource.setId(UUID.randomUUID().toString().replace("-", ""));
        try {
            long cuttentTime = DateUtil.getDateline();
            playSource.setCreateTime(cuttentTime);
            playSource.setUpdateTime(cuttentTime);
            playSource.setTheLeft(playSource.getLeft());
            n = playSourceMapper.insertSelective(playSource);
        } catch (Exception e) {
            logger.error("insertPlaySource error and msg={}", e);
        }
        return n;
    }

    @Override
    public int updatePlaySourceById(PlaySource playSource) {
        int n = 0;
        try {
            playSource.setUpdateTime(DateUtil.getDateline());
            n = playSourceMapper.updateByPrimaryKeySelective(playSource);
        } catch (Exception e) {
            logger.error("updatePlaySourceById error and msg={}", e);
        }
        return n;
    }

    @Override
    public int deleteById(String id) {
        int n = 0;
        try {
            n = playSourceMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return n;
    }

    @Override
    public PlaySource selectBySourceIdAndLayerId(String sourceId,String layerId) {
        PlaySource playSource = null;
        try {
            playSource = playSourceMapper.selectBySourceIdAndLayerId(sourceId,layerId);
        } catch (Exception e) {
            logger.error("selectBySourceIdAndLayerId error and msg={}", e);
        }
        return playSource;
    }

    @Override
    public PlaySource selectBySourceIdAndProgramId(String sourceId,String programId) {
        PlaySource playSource = null;
        try {
            playSource = playSourceMapper.selectBySourceIdAndProgramId(sourceId,programId);
        } catch (Exception e) {
            logger.error("selectBySourceIdAndProgramId error and msg={}", e);
        }
        return playSource;
    }
}
