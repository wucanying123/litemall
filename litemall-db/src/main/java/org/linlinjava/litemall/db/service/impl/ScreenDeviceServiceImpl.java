package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.dao.ScreenDeviceMapper;
import org.linlinjava.litemall.db.domain.ScreenDevice;
import org.linlinjava.litemall.db.service.ScreenDeviceService;
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
public class ScreenDeviceServiceImpl implements ScreenDeviceService {

    @Autowired
    private ScreenDeviceMapper screenDeviceMapper;


    private static Logger logger = LoggerFactory.getLogger(ScreenDeviceServiceImpl.class);

    @Override
    public PageInfo<ScreenDevice> selectScreenDevicePage(ScreenDevice screenDevice,Integer pageNum,Integer pageSize) {
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
    public ResponseUtil<ScreenDevice> selectScreenDeviceById(String screenDeviceId) {
        ResponseUtil<ScreenDevice> responseUtil = new ResponseUtil<ScreenDevice>();
        try {
            ScreenDevice screenDevice = screenDeviceMapper.selectByPrimaryKey(screenDeviceId);
            responseUtil.setData(screenDevice);
            responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectScreenDeviceById error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<ScreenDevice> insertScreenDevice(ScreenDevice screenDevice) {
        ResponseUtil<ScreenDevice> responseUtil = new ResponseUtil<ScreenDevice>();
        screenDevice.setId(UUID.randomUUID().toString().replace("-", ""));
        try {
            screenDevice.setCreateTime(DateUtil.getDateline());
            int n = screenDeviceMapper.insertSelective(screenDevice);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertScreenDevice error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<ScreenDevice> updateScreenDeviceById(ScreenDevice screenDevice) {
        ResponseUtil<ScreenDevice> responseUtil = new ResponseUtil<ScreenDevice>();
        try {
            screenDevice.setUpdateTime(DateUtil.getDateline());
            int n = screenDeviceMapper.updateByPrimaryKeySelective(screenDevice);
            if (n >= 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateScreenDeviceById error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<ScreenDevice> deleteById(String id) {
        ResponseUtil<ScreenDevice> responseUtil = new ResponseUtil<ScreenDevice>();
        try {
            int m = screenDeviceMapper.deleteByPrimaryKey(id);
            if (m >= 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return responseUtil;
    }
}
