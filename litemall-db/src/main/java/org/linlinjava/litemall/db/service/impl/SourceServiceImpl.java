package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.dao.SourceMapper;
import org.linlinjava.litemall.db.domain.Source;
import org.linlinjava.litemall.db.service.SourceService;
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
public class SourceServiceImpl implements SourceService {

    @Autowired
    private SourceMapper sourceMapper;


    private static Logger logger = LoggerFactory.getLogger(SourceServiceImpl.class);

    @Override
    public PageInfo<Source> selectSourcePage(Source source,Integer pageNum,Integer pageSize) {
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
    public ResponseUtil<Source> selectSourceById(String sourceId) {
        ResponseUtil<Source> responseUtil = new ResponseUtil<Source>();
        try {
            Source source = sourceMapper.selectByPrimaryKey(sourceId);
            responseUtil.setData(source);
            responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectSourceById error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Source> insertSource(Source source) {
        ResponseUtil<Source> responseUtil = new ResponseUtil<Source>();
        source.setId(UUID.randomUUID().toString().replace("-", ""));
        try {
            source.setCreateTime(DateUtil.getDateline());
            source.setTheLeft(source.getLeft());
            if(null == source.getMaxPlayTime()){
                source.setMaxPlayTime(10);
            }
            int n = sourceMapper.insertSelective(source);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertSource error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Source> updateSourceById(Source source) {
        ResponseUtil<Source> responseUtil = new ResponseUtil<Source>();
        try {
            source.setUpdateTime(DateUtil.getDateline());
            int n = sourceMapper.updateByPrimaryKeySelective(source);
            if (n >= 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateSourceById error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Source> deleteById(String id) {
        ResponseUtil<Source> responseUtil = new ResponseUtil<Source>();
        try {
            int m = sourceMapper.deleteByPrimaryKey(id);
            if (m >= 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return responseUtil;
    }
}
