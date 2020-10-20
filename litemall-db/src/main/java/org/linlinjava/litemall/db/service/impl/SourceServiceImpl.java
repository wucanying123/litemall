package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.dao.SourceMapper;
import org.linlinjava.litemall.db.domain.Examine;
import org.linlinjava.litemall.db.domain.LitemallStorage;
import org.linlinjava.litemall.db.domain.Source;
import org.linlinjava.litemall.db.service.ExamineService;
import org.linlinjava.litemall.db.service.LitemallStorageService;
import org.linlinjava.litemall.db.service.SourceService;
import org.linlinjava.litemall.db.util.Constant;
import org.linlinjava.litemall.db.util.DateUtil;
import org.linlinjava.litemall.db.util.StringUtilsXD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SourceServiceImpl implements SourceService {

    @Autowired
    private SourceMapper sourceMapper;
    @Autowired
    private ExamineService examineService;
    @Autowired
    private LitemallStorageService litemallStorageService;


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
                    source1.setCreateTime(null);
                    String uuid = UUID.randomUUID().toString().replace("-", "");
                    source1.setUuid(uuid);
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
        source.setSourceId(UUID.randomUUID().toString().replace("-", ""));
        try {
            long cuttentTime = DateUtil.getDateline();
            source.setCreateTime(cuttentTime);
            source.setUpdateTime(cuttentTime);
            source.setTheLeft(source.getLeft());
            if (null == source.getMaxPlayTime()) {
                source.setMaxPlayTime(Constant.PLAYTIME);
            }
            source = updateSourceOption(source);
            n = sourceMapper.insertSelective(source);
            //同步添加到审核表
            Examine examine = new Examine();
            examine.setPassStatus(1);
            examine.setType(3);
            examine.setDetailId(source.getSourceId());
            examine.setDetailName(source.getName());
            examineService.insertExamine(examine);
        } catch (Exception e) {
            logger.error("insertSource error and msg={}", e);
        }
        return n;
    }


    private Source updateSourceOption(Source source){
        String url = source.getUrl();
        if(StringUtilsXD.isNotEmpty(url)) {
            url = url.trim();
            String key = url.substring(url.lastIndexOf("/")+1);
            String fileExt = key.substring(key.lastIndexOf("."));
            source.setFileExt(fileExt);
            LitemallStorage storage = litemallStorageService.findByKey(key);
            if(null != storage){
                Long size = storage.getSize().longValue();
                source.setSize(size);
                source.setMime(storage.getType());
                if(null != storage.getMaxPlayTime()){
                    source.setMaxPlayTime(storage.getMaxPlayTime());
                }
            }
        }
        source.setEnabled(true);
        return source;
    }

    @Override
    public int updateSourceById(Source source) {
        int n = 0;
        try {
            source = updateSourceOption(source);
            source.setUpdateTime(DateUtil.getDateline());
            n = sourceMapper.updateByPrimaryKeySelective(source);
            //同步修改名称到审核表
            if (StringUtilsXD.isNotEmpty(source.getSourceId()) && StringUtilsXD.isNotEmpty(source.getName())) {
                examineService.updateExamineDetailName(source.getSourceId(),source.getName());
            }
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
            //同步删除审核表数据
            examineService.deleteByDetailId(id);
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return n;
    }
}
