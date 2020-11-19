package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.domain.ScreenDevice;

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
     * @Description: 查找卡更新屏幕设备信息
     * @title updateDeviceByFindCard
     * @auther IngaWu
     * @currentdate:2020年9月16日
     */
    void updateDeviceByFindCard();

    /**
     * @Description: 通过屏幕设备id查看屏幕设备详情
     * @Title: selectScreenDeviceById
     * @param screenDeviceId 屏幕设备id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    ScreenDevice selectScreenDeviceById(String screenDeviceId);

    /**
     * @Description: 通过卡号查看屏幕设备详情
     * @Title: selectByCardId
     * @param cardId 卡号
     * @auther IngaWu
     * @currentdate:2020年9月16日
     */
    ScreenDevice selectByCardId(String cardId);

    /**
     * @Description: 添加屏幕设备
     * @title insertScreenDevice
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    int insertScreenDevice(ScreenDevice screenDevice);

    /**
     * @Description: 编辑屏幕设备
     * @title updateScreenDeviceById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    int updateScreenDeviceById(ScreenDevice screenDevice);

    /**
     * @Description: 修改其余卡号为离线状态
     * @title updateOffLine
     * @author IngaWu
     * @currentdate:2020年9月16日
     */
    int updateOffLine();

    /**
     * @Description: 删除屏幕设备
     * @Title: deleteById
     * @param id 屏幕设备id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    int deleteById(String id);
}
