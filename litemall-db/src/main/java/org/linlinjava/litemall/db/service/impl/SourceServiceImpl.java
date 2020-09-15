package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.dao.SourceMapper;
import org.linlinjava.litemall.db.domain.Source;
import org.linlinjava.litemall.db.service.SourceService;
import org.linlinjava.litemall.db.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SourceServiceImpl implements SourceService {

    @Autowired
    private SourceMapper sourceMapper;


    private static Logger logger = LoggerFactory.getLogger(SourceServiceImpl.class);

    @Override
    public PageInfo<Source> selectSourcePage(Source source, Integer pageNum, Integer pageSize) {
        PageInfo<Source> page = null;
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Source> list = sourceMapper.selectSourcePage(source);
            page = new PageInfo<>(list);
            List<Source> list1 = page.getList();
            if (null != list1 && list1.size() > 0) {
                for (Source source1 : list1) {
                    source1.setLeft(source1.getTheLeft());
                }
            }
            String jsonString = JSON.toJSONString(list1);
        } catch (Exception e) {
            logger.error("selectSourcePage error and msg={}", e);
        }
        return page;
    }

    @Override
    public Source selectSourceById(String sourceId) {
        Source source = null;
        try {
            source = sourceMapper.selectByPrimaryKey(sourceId);
        } catch (Exception e) {
            logger.error("selectSourceById error and msg={}", e);
        }
        return source;
    }

    @Override
    public int insertSource(Source source) {
        int n = 0;
        source.setId(UUID.randomUUID().toString().replace("-", ""));
        try {
            long cuttentTime = DateUtil.getDateline();
            source.setCreateTime(cuttentTime);
            source.setUpdateTime(cuttentTime);
            source.setTheLeft(source.getLeft());
            if (null == source.getMaxPlayTime()) {
                source.setMaxPlayTime(10);
            }
            n = sourceMapper.insertSelective(source);
        } catch (Exception e) {
            logger.error("insertSource error and msg={}", e);
        }
        return n;
    }

    @Override
    public int updateSourceById(Source source) {
        int n = 0;
        try {
            source.setUpdateTime(DateUtil.getDateline());
            n = sourceMapper.updateByPrimaryKeySelective(source);
        } catch (Exception e) {
            logger.error("updateSourceById error and msg={}", e);
        }
        return n;
    }

    @Override
    public int deleteById(String id) {
        int n = 0;
        try {
            n = sourceMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return n;
    }
}
