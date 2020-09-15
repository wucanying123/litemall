package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.dao.ScreenDeviceMapper;
import org.linlinjava.litemall.db.domain.ScreenDevice;
import org.linlinjava.litemall.db.service.ScreenDeviceService;
import org.linlinjava.litemall.db.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ScreenDeviceServiceImpl implements ScreenDeviceService {

    @Autowired
    private ScreenDeviceMapper screenDeviceMapper;


    private static Logger logger = LoggerFactory.getLogger(ScreenDeviceServiceImpl.class);

    @Override
    public PageInfo<ScreenDevice> selectScreenDevicePage(ScreenDevice screenDevice, Integer pageNum, Integer pageSize) {
        PageInfo<ScreenDevice> page = null;
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<ScreenDevice> list = screenDeviceMapper.selectScreenDevicePage(screenDevice);
            String jsonString = JSON.toJSONString(list);
            page = new PageInfo<>(list);
        } catch (Exception e) {
            logger.error("selectScreenDevicePage error and msg={}", e);
        }
        return page;
    }

    @Override
    public ScreenDevice selectScreenDeviceById(String screenDeviceId) {
        ScreenDevice screenDevice = null;
        try {
            screenDevice = screenDeviceMapper.selectByPrimaryKey(screenDeviceId);
        } catch (Exception e) {
            logger.error("selectScreenDeviceById error and msg={}", e);
        }
        return screenDevice;
    }

    @Override
    public int insertScreenDevice(ScreenDevice screenDevice) {
        int n = 0;
        screenDevice.setId(UUID.randomUUID().toString().replace("-", ""));
        try {
            long cuttentTime = DateUtil.getDateline();
            screenDevice.setCreateTime(cuttentTime);
            screenDevice.setUpdateTime(cuttentTime);
            n = screenDeviceMapper.insertSelective(screenDevice);
        } catch (Exception e) {
            logger.error("insertScreenDevice error and msg={}", e);
        }
        return n;
    }

    @Override
    public int updateScreenDeviceById(ScreenDevice screenDevice) {
        int n = 0;
        try {
            screenDevice.setUpdateTime(DateUtil.getDateline());
            n = screenDeviceMapper.updateByPrimaryKeySelective(screenDevice);
        } catch (Exception e) {
            logger.error("updateScreenDeviceById error and msg={}", e);
        }
        return n;
    }

    @Override
    public int deleteById(String id) {
        int n = 0;
        try {
            n = screenDeviceMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return n;
    }
}
