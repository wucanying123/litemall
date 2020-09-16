package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.linlinjava.litemall.db.dao.ScreenDeviceMapper;
import org.linlinjava.litemall.db.domain.ScreenDevice;
import org.linlinjava.litemall.db.service.ScreenDeviceService;
import org.linlinjava.litemall.db.service.ScreenService;
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
    @Autowired
    private ScreenService screenService;


    private static Logger logger = LoggerFactory.getLogger(ScreenDeviceServiceImpl.class);

    @Override
    public PageInfo<ScreenDevice> selectScreenDevicePage(ScreenDevice screenDevice, Integer pageNum, Integer pageSize) {
        PageInfo<ScreenDevice> page = null;
        try {
            updateDeviceByFindCard();
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
    public void updateDeviceByFindCard(){
        ResponseUtil<Object> responseUtil = screenService.udpFindCard();
        if (responseUtil.getErrno() == 0) {
            JSONObject jsonObject = JSONObject.fromObject(responseUtil.getData());
            String cardId = null;
            if (null != jsonObject.get("cardId")) {
                cardId = jsonObject.get("cardId").toString();
            }
            ScreenDevice screenDevice1 = selectByCardId(cardId);
            ScreenDevice newScreenDevice = new ScreenDevice();
            newScreenDevice.setCardId(cardId);
            if (null != jsonObject.get("width")) {
                String width = jsonObject.get("width").toString();
                newScreenDevice.setWidth(Integer.parseInt(width));
            }
            if (null != jsonObject.get("height")) {
                String height = jsonObject.get("height").toString();
                newScreenDevice.setHeight(Integer.parseInt(height));
            }
            if (null != jsonObject.get("brightness")) {
                String brightness = jsonObject.get("brightness").toString();
                newScreenDevice.setBrightness(Integer.parseInt(brightness));
            }
            newScreenDevice.setOnlineStatus(true);
            ResponseUtil<Object> openStatusRes = screenService.getScreenOpenStatus();
            if (openStatusRes.getErrno() == 0) {
                JSONObject openStatusJSONObject = JSONObject.fromObject(openStatusRes.getData());
                Boolean screenOpenStatus=(Boolean)openStatusJSONObject.get("result");
                newScreenDevice.setScreenOpenStatus(screenOpenStatus);
            }
            ResponseUtil<Object> networkTypeRes = screenService.getNetworkType();
            if (networkTypeRes.getErrno() == 0) {
                JSONObject networkTypeJSONObject = JSONObject.fromObject(networkTypeRes.getData());
                String networkType =networkTypeJSONObject.get("result").toString();
                newScreenDevice.setNetworkType(networkType);
            }
            if (null == screenDevice1) {//数据表中没有就新增
                insertScreenDevice(newScreenDevice);
            } else {//数据表中有就修改为新的值
                newScreenDevice.setId(screenDevice1.getId());
                updateScreenDeviceById(newScreenDevice);
            }
            updateOffLineByCardId(cardId);
        }
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
    public ScreenDevice selectByCardId(String cardId) {
        ScreenDevice screenDevice = null;
        try {
            screenDevice = screenDeviceMapper.selectByCardId(cardId);
        } catch (Exception e) {
            logger.error("selectByCardId error and msg={}", e);
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
    public int updateOffLineByCardId(String cardId) {
        int n = 0;
        try {
            n = screenDeviceMapper.updateOffLineByCardId(cardId);
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
