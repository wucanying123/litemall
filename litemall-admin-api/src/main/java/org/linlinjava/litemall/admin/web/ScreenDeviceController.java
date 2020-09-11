package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.linlinjava.litemall.db.domain.LitemallAdmin;
import org.linlinjava.litemall.db.domain.ScreenDevice;
import org.linlinjava.litemall.db.service.ScreenDeviceService;
import org.linlinjava.litemall.db.util.Constant;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.StringUtilsXD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/screen/screenDevice")
public class ScreenDeviceController {
    @Autowired
    private ScreenDeviceService screenDeviceService;

    private static Logger logger = LoggerFactory.getLogger(ScreenDeviceController.class);

    /**
     * @Description: 获取屏幕设备列表
     * @title selectScreenDevicePage
     * @param page 开始页数
     * @param limit 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "获取屏幕设备列表")
    @GetMapping("/selectScreenDevicePage")
    public ResponseUtil selectScreenDevicePage(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        ResponseUtil responseUtil = new ResponseUtil();
        ScreenDevice screenDevice = null;
        logger.info("selectScreenDevicePage and screenDevice={},page={},limit", JSON.toJSONString(screenDevice), page, limit);
        try {
            PageInfo<ScreenDevice> pageList = screenDeviceService.selectScreenDevicePage(screenDevice, StringUtilsXD.checkPageNumParam(page), StringUtilsXD.checkPageSizeParam(limit));
            return responseUtil.succeedPage(pageList);
        } catch (Exception e) {
            logger.error("selectScreenDevicePage and screenDevice={},page={},limit", JSON.toJSONString(screenDevice), page, limit, e);
        }
        return responseUtil;
    }

    /**
     * @Description: 通过屏幕设备id查看屏幕设备详情
     * @param screenDeviceId 屏幕设备id
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
            responseUtil = screenDeviceService.selectScreenDeviceById(screenDeviceId);
        } catch (Exception e) {
            logger.error("selectScreenDeviceById and screenDeviceId={}", screenDeviceId, e);
        }
        return responseUtil;
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
            responseUtil = screenDeviceService.insertScreenDevice(screenDevice);
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
        try {
            responseUtil = screenDeviceService.updateScreenDeviceById(screenDevice);
        } catch (Exception e) {
            logger.error("updateScreenDeviceById and screenDevice:{}", JSON.toJSONString(screenDevice), e);

        }
        return responseUtil;
    }

    /**
     * @Description: 删除屏幕设备
     * @Title: deleteById
     * @param id 屏幕设备id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "删除屏幕设备")
    @PostMapping(value = "/deleteById")
    public ResponseUtil<ScreenDevice> deleteById(@RequestParam(value = "id") String id) {
        logger.info("deleteById and id={}", JSON.toJSONString(id));
        ResponseUtil<ScreenDevice> responseUtil = new ResponseUtil<>();
        try {
            responseUtil = screenDeviceService.deleteById(id);
        } catch (Exception e) {
            logger.error("deleteById and id={}", JSON.toJSONString(id), e);
        }
        return responseUtil;
    }
}
