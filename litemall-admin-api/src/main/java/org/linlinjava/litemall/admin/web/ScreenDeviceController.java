package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.linlinjava.litemall.core.util.JacksonUtil;
import org.linlinjava.litemall.db.domain.LitemallRole;
import org.linlinjava.litemall.db.domain.ScreenDevice;
import org.linlinjava.litemall.db.service.ScreenDeviceService;
import org.linlinjava.litemall.db.service.ScreenService;
import org.linlinjava.litemall.db.util.Constant;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.StringUtilsXD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/admin/screen/screenDevice")
public class ScreenDeviceController {
    @Autowired
    private ScreenDeviceService screenDeviceService;
    @Autowired
    private ScreenService screenService;

    private static Logger logger = LoggerFactory.getLogger(ScreenDeviceController.class);

    /**
     * @param page  开始页数
     * @param limit 每页条数
     * @Description: 获取屏幕设备列表
     * @title selectScreenDevicePage
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "获取屏幕设备列表")
    @GetMapping("/selectScreenDevicePage")
    public ResponseUtil selectScreenDevicePage(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        ResponseUtil responseUtil = new ResponseUtil();
        ScreenDevice screenDevice = null;
        logger.info("selectScreenDevicePage and screenDevice={},page={},limit={}", JSON.toJSONString(screenDevice), page, limit);
        try {
            PageInfo<ScreenDevice> pageList = screenDeviceService.selectScreenDevicePage(screenDevice, StringUtilsXD.checkPageNumParam(page), StringUtilsXD.checkPageSizeParam(limit));
            return responseUtil.succeedPage(pageList);
        } catch (Exception e) {
            logger.error("selectScreenDevicePage and screenDevice={},page={},limit={}", JSON.toJSONString(screenDevice), page, limit, e);
        }
        return responseUtil;
    }


    /**
     * @Description: 获取在线屏幕设备列表
     * @title selectOnlineDevice
     * @auther IngaWu
     * @currentdate:2020年9月16日
     */
    @GetMapping("/selectOnlineDevice")
    public Object selectOnlineDevice() {
        ScreenDevice screenDevice = new ScreenDevice();
        screenDevice.setOnlineStatus(true);
        PageInfo<ScreenDevice> pageList = screenDeviceService.selectScreenDevicePage(screenDevice, 1, 1000);
        List<ScreenDevice> list = pageList.getList();
        List<Map<String, Object>> options = new ArrayList<>(list.size());
        for (ScreenDevice screenDevice1 : list) {
            Map<String, Object> option = new HashMap<>(2);
            option.put("value", screenDevice1.getCardId());
            option.put("label", screenDevice1.getCardId());
            options.add(option);
        }
        return ResponseUtil.okList(options);
    }

    /**
     * @param screenDeviceId 屏幕设备id
     * @Description: 通过屏幕设备id查看屏幕设备详情
     * @Title: selectScreenDeviceById
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "通过屏幕设备id查看屏幕设备详情")
    @GetMapping(value = "/selectScreenDeviceById")
    public ResponseUtil<ScreenDevice> selectScreenDeviceById(@RequestParam(value = "screenDeviceId") String screenDeviceId) {
        logger.info("selectScreenDeviceById and screenDeviceId={}", screenDeviceId);
        ResponseUtil<ScreenDevice> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(screenDeviceId)) {
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            ScreenDevice screenDevice = screenDeviceService.selectScreenDeviceById(screenDeviceId);
            responseUtil.setData(screenDevice);
            responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectScreenDeviceById and screenDeviceId={}", screenDeviceId, e);
        }
        return responseUtil;
    }

    @ApiOperation(value = "通过UDP广播找卡")
    @PostMapping(value = "/udpFindCard")
    public ResponseUtil<Object> udpFindCard() {
        return screenService.udpFindCard();
    }

    /**
     * @Description: 添加屏幕设备
     * @Title: insertScreenDevice
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "添加屏幕设备")
    @PostMapping(value = "/insertScreenDevice")
    public ResponseUtil<ScreenDevice> insertScreenDevice(@RequestBody ScreenDevice screenDevice) {
        logger.info("insertScreenDevice and screenDevice:{}", JSON.toJSONString(screenDevice));
        ResponseUtil<ScreenDevice> responseUtil = new ResponseUtil<>();
        try {
            int n = screenDeviceService.insertScreenDevice(screenDevice);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertScreenDevice and screenDevice:{}", JSON.toJSONString(screenDevice), e);
        }
        return responseUtil;
    }

    /**
     * @Description: 编辑屏幕设备
     * @title updateScreenDeviceById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "编辑屏幕设备")
    @PostMapping(value = "/updateScreenDeviceById")
    public ResponseUtil<ScreenDevice> updateScreenDeviceById(@RequestBody ScreenDevice screenDevice) {
        logger.info("updateScreenDeviceById and screenDevice:{}", JSON.toJSONString(screenDevice));
        ResponseUtil<ScreenDevice> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(screenDevice.getId())) {
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        String cardId = screenDevice.getCardId();
        try {
            if (null != screenDevice.getVolume()) {
                screenService.setVolume(screenDevice.getVolume(), cardId);
            }
            if (null != screenDevice.getBrightness()) {
                screenService.setBrightness(screenDevice.getBrightness(), cardId);
            }
            if (null != screenDevice.getScreenOpenStatus()) {
                screenService.setScreenOpen(screenDevice.getScreenOpenStatus(), cardId);
            }
            int n = screenDeviceService.updateScreenDeviceById(screenDevice);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateScreenDeviceById and screenDevice:{}", JSON.toJSONString(screenDevice), e);

        }
        return responseUtil;
    }

    /**
     * @param id 屏幕设备id
     * @Description: 删除屏幕设备
     * @Title: deleteById
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "删除屏幕设备")
    @PostMapping(value = "/deleteById")
    public ResponseUtil<ScreenDevice> deleteById(@RequestParam(value = "id") String id) {
        logger.info("deleteById and id={}", JSON.toJSONString(id));
        ResponseUtil<ScreenDevice> responseUtil = new ResponseUtil<>();
        try {
            int n = screenDeviceService.deleteById(id);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteById and id={}", JSON.toJSONString(id), e);
        }
        return responseUtil;
    }

    @ApiOperation(value = "重启")
    @PostMapping(value = "/reboot")
    public ResponseUtil<Object> reboot(String cardId) {
        if (StringUtilsXD.isEmpty(cardId)) {
            ResponseUtil<Object> responseUtil = new ResponseUtil<>();
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_03, Constant.RTNINFO_SYS_03);
        }
        return screenService.reboot(cardId);
    }

    @ApiOperation(value = "停止直播")
    @PostMapping(value = "/stopLiveVideo")
    public ResponseUtil<Object> stopLiveVideo(@RequestBody String body) {
        List<String> selectCardIds = JacksonUtil.parseStringList(body, "selectCardIds");
        logger.info("stopLiveVideo and selectCardIds={}", JSON.toJSONString(selectCardIds));
        if (null == selectCardIds || selectCardIds.size() < 1) {
            ResponseUtil<Object> responseUtil = new ResponseUtil<>();
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_03, Constant.RTNINFO_SYS_03);
        }
        return screenService.stopLiveVideo(selectCardIds);
    }

    @ApiOperation(value = "清屏")
    @PostMapping(value = "/clearScreen")
    public ResponseUtil<Object> clearScreen(String cardId) {
        if (StringUtilsXD.isEmpty(cardId)) {
            ResponseUtil<Object> responseUtil = new ResponseUtil<>();
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_03, Constant.RTNINFO_SYS_03);
        }
        return screenService.clearScreen(cardId);
    }

    @ApiOperation(value = "停止节目，清除播放器节目数据和文件")
    @PostMapping(value = "/clearPlayerTask")
    public ResponseUtil<Object> clearPlayerTask(@RequestBody String body) {
        List<String> selectCardIds = JacksonUtil.parseStringList(body, "selectCardIds");
        logger.info("clearPlayerTask and selectCardIds={}", JSON.toJSONString(selectCardIds));
        if (null == selectCardIds || selectCardIds.size() < 1) {
            ResponseUtil<Object> responseUtil = new ResponseUtil<>();
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_03, Constant.RTNINFO_SYS_03);
        }
        return screenService.clearPlayerTask(selectCardIds);
    }

    @ApiOperation(value = "获取截图", notes = "返回截图为base64编码的字符串（字符串中含有较多的\\n换行符，需要用正则去掉才能正常显示，格式为png")
    @PostMapping(value = "/getScreenshot")
    public ResponseUtil<Object> getScreenshot(String cardId) {
        if (StringUtilsXD.isEmpty(cardId)) {
            ResponseUtil<Object> responseUtil = new ResponseUtil<>();
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_03, Constant.RTNINFO_SYS_03);
        }
        return screenService.getScreenshot(cardId);
    }
}
