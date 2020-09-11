package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.domain.ScreenDevice;
import org.linlinjava.litemall.db.util.ResponseUtil;

public interface ScreenDeviceService {

    /**
     * @Description: 获取屏幕设备列表
     * @title selectScreenDevicePage
     * @param pageNum 开始页数
     * @param pageSize 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    PageInfo<ScreenDevice> selectScreenDevicePage(ScreenDevice screenDevice,Integer pageNum,Integer pageSize);

    /**
     * @Description: 通过屏幕设备id查看屏幕设备详情
     * @Title: selectScreenDeviceById
     * @param screenDeviceId 屏幕设备id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    ResponseUtil<ScreenDevice> selectScreenDeviceById(String screenDeviceId);

    /**
     * @Description: 添加屏幕设备
     * @title insertScreenDevice
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    ResponseUtil<ScreenDevice> insertScreenDevice(ScreenDevice screenDevice);

    /**
     * @Description: 编辑屏幕设备
     * @title updateScreenDeviceById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    ResponseUtil<ScreenDevice> updateScreenDeviceById(ScreenDevice screenDevice);

    /**
     * @Description: 删除屏幕设备
     * @Title: deleteById
     * @param id 屏幕设备id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    ResponseUtil<ScreenDevice> deleteById(String id);
}
