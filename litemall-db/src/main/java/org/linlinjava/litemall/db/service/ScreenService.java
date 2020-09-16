package org.linlinjava.litemall.db.service;//package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.domain.Schedule;
import org.linlinjava.litemall.db.domain.ScheduleVO;
import org.linlinjava.litemall.db.util.ResponseUtil;

/**
 * 智慧路灯屏幕相关操作
 *
 * @author WuCanying
 * 2020/8/25
 */
public interface ScreenService {

    /**
     * 通过UDP广播找卡
     */
    ResponseUtil<Object> udpFindCard();

    /**
     * 加载顶层网页
     *
     * @param weburl
     */
    ResponseUtil<Object> updateTopWeb(String weburl,String cardId);

    /**
     * 调用“顶层网页”里的js方法
     * （注意：js里面请不要添加alert这一类需要鼠标操作的代码，否则会卡住）
     * (前提：当前页面的window对象下必须有一个handleData方法（可以是任何方法名），例如:
     * window.handleData = function(data){
     * console.log(data)
     * })
     */
    ResponseUtil<Object> updateTopWebJS(String cardId);

    /**
     * 滚动文字
     *
     * @param num       滚动次数, 注意类型为Number, 填0停止滚动，填负数永久滚动
     * @param text      滚动的文字
     * @param color     文字颜色
     * @param interval  步进间隔，单位毫秒，注意类型为Number
     * @param step      步进距离，单位像素，注意类型为Number
     * @param direction 往左滚动，可填值left、 right
     * @param align     在上方显示，可填值top、center、bottom
     */
    ResponseUtil<Object> scrollingText(Integer num, String text, String color, Integer interval, Integer step, String direction, String align,String cardId);

    /**
     * 清屏
     */
    ResponseUtil<Object> clearScreen(String cardId);

    /**
     * 启动xwalk （需要先在www.m2mled.net上安装xwalk，xwalk是一个支持html5的浏览器）
     */
    ResponseUtil<Object> startActivity(String cardId);

    /**
     * 使用xwalk加载网页
     */
    ResponseUtil<Object> loadWebByActivity(String cardId);

    /**
     * 调用xwalk加载的网页里的js方法
     */
    ResponseUtil<Object> updateWebJSByActivity(String cardId);

    /**
     * 设置xwalk背景
     * (注意：当前加载的页面必须已经定义过showHtml方法)
     */
    ResponseUtil<Object> setBackgroundColor(String color,String cardId);

    /**
     * 播放AIPS里下载的节目
     */
    ResponseUtil<Object> playXixunProgramZip(String cardId);

    /**
     * 播放直播
     */
    ResponseUtil<Object> playLiveVideo(String liveId,String cardId);

    /**
     * 停止直播
     */
    ResponseUtil<Object> stopLiveVideo(String cardId);

    /**
     * 以字符串形式上传并保存html、图片等文件到sd卡
     */
    ResponseUtil<Object> saveSDStringFile(String cardId);

    /**
     * 删除以字符串形式上传的文件
     */
    ResponseUtil<Object> deleteSDFile(String cardId);

    /**
     * 下载文件到SD卡（图片、视频等任意文件）
     */
    ResponseUtil<Object> downloadFileToSD(String cardId);

    /**
     * 删除SD上的文件
     */
    ResponseUtil<Object> deleteFileFromSD(String cardId);

    /**
     * 获取SD卡上文件的大小
     */
    ResponseUtil<Object> getSDFileLength(String cardId);

    /**
     * 停止默认播放器
     */
    ResponseUtil<Object> stopPlayer(String cardId);

    /**
     * 下载文件到内部存储（图片、视频等任意文件）
     */
    ResponseUtil<Object> downloadFileToLocal(String cardId);

    /**
     * 删除内部存储里的文件夹
     */
    ResponseUtil<Object> deleteFileFromLocal(String cardId);

    /**
     * 查看内部存储里的文件大小
     */
    ResponseUtil<Object> getLocalFileLength(String cardId);

    /**
     * 获取截图
     * （返回截图为base64编码的字符串（字符串中含有较多的\n换行符，需要用正则去掉才能正常显示，格式为png）
     */
    ResponseUtil<Object> getScreenshot(String cardId);

    /**
     * 获取GPS坐标
     */
    ResponseUtil<Object> getGpsLocation(String cardId);



    /**
     * 提交JsonResponseUtil<Object>
     */
    ResponseUtil<Object> postJsonString(String jsonString,String cardId);

    /**
     * 播放熙讯任务
     */
    ResponseUtil<Object> playXixunTask(String taskId,String cardId);

    /**
     * 把schedule的属性转换类型成ScheduleVO
     */
    ScheduleVO tranToScheduleVO(Schedule schedule);

    /**
     * 开关屏幕
     */
    ResponseUtil<Object> setScreenOpen(boolean isopen,String cardId);

    /**
     * 设置亮度
     */
    ResponseUtil<Object> setBrightness(String brightNum,String cardId);

    /**
     * 设置音量
     */
    ResponseUtil<Object> setVolume(Integer volumeNum,String cardId);

    /**
     * 获取屏幕是否打开
     */
    ResponseUtil<Object> getScreenOpenStatus(String cardId);

    /**
     * 获取亮度
     */
    ResponseUtil<Object> getBrightness(String cardId);


    /**
     * 获取音量
     */
    ResponseUtil<Object> getVolume(String cardId);

    /**
     * 获取屏宽
     */
    ResponseUtil<Object> getScreenWidth(String cardId);

    /**
     * 获取屏高
     */
    ResponseUtil<Object> getScreenHeight(String cardId);

    /**
     * 获取网络类型
     */
    ResponseUtil<Object> getNetworkType(String cardId);

    /**
     * 设置NTP服务器或时区
     */
    ResponseUtil<Object> setTimeSync(String ntpServer, String timezone,String cardId);

    /**
     * 获取NTP服务器
     */
    ResponseUtil<Object> getNtpServer(String cardId);

    /**
     * 获取时区
     */
    ResponseUtil<Object> getTimezone(String cardId);

    /**
     * 重启
     */
    ResponseUtil<Object> reboot(String cardId);

    /**
     * 获取apk信息
     */
    ResponseUtil<Object> getPackageVersion(String cardId);

    /**
     * 获取硬件状态
     */
    ResponseUtil<Object> getFpgaInfomation(String cardId);

    /**
     * 在线更新app接口
     */
    ResponseUtil<Object> updateAPP(String cardId);

    /**
     * 高级参数设置接口
     */
    ResponseUtil<Object> advancedConfig(String cardId);

    /**
     * 同步时间设置接口
     */
    ResponseUtil<Object> setTimeSync2(String cardId);

    /**
     * 设置自动亮度，亮度根据传感器数据变化自动调整
     */
    ResponseUtil<Object> setAutoBrightness(String sensitivity, String minBrightness,String cardId);

    /**
     * 查询自动亮度
     */
    ResponseUtil<Object> getAutoBrightness(String cardId);

    /**
     * 查询定时亮度接口
     */
    ResponseUtil<Object> getTimedBrightness(String cardId);

    /**
     * 查询定时开关屏
     */
    ResponseUtil<Object> getTimedScreening(String cardId);

    /**
     * 定时重启接口
     */
    ResponseUtil<Object> timedReboot(String time,String cardId);

    /**
     * 查询定时重启时间
     */
    ResponseUtil<Object> getTimedReboot(String cardId);

    /**
     * 清除播放器节目数据和文件
     */
    ResponseUtil<Object> clearPlayerTask(String cardId);

    /**
     * 获取传感器数据接口
     */
    ResponseUtil<Object> getSensor(String cardId);

    /**
     * 查询设备当前时间
     */
    ResponseUtil<Object> getControllerDate(String cardId);

    /**
     * 查询设备磁盘空间
     */
    ResponseUtil<Object> getDiskSpace(String cardId);

    /**
     * 查询播放器当前存储的节目JSON
     */
    ResponseUtil<Object> getProgramTask(String cardId);

    /**
     * 查询播放器当前正在播放的节目名
     */
    ResponseUtil<Object> getPlayingProgram(String cardId);

    /**
     * 获取服务器地址和公司id接口
     */
    ResponseUtil<Object> getServerAddress(String cardId);

    /**
     * 开关WiFi/AP接口
     */
    ResponseUtil<Object> switchWiFi(boolean on,String cardId);
}
