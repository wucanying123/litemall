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
     * 加载顶层网页
     *
     * @param weburl
     */
    ResponseUtil<Object> updateTopWeb(String weburl);

    /**
     * 调用“顶层网页”里的js方法
     * （注意：js里面请不要添加alert这一类需要鼠标操作的代码，否则会卡住）
     * (前提：当前页面的window对象下必须有一个handleData方法（可以是任何方法名），例如:
     * window.handleData = function(data){
     * console.log(data)
     * })
     */
    ResponseUtil<Object> updateTopWebJS();

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
    ResponseUtil<Object> scrollingText(Integer num, String text, String color, Integer interval, Integer step, String direction, String align);

    /**
     * 清屏
     */
    ResponseUtil<Object> clearScreen();

    /**
     * 启动xwalk （需要先在www.m2mled.net上安装xwalk，xwalk是一个支持html5的浏览器）
     */
    ResponseUtil<Object> startActivity();

    /**
     * 使用xwalk加载网页
     */
    ResponseUtil<Object> loadWebByActivity();

    /**
     * 调用xwalk加载的网页里的js方法
     */
    ResponseUtil<Object> updateWebJSByActivity();

    /**
     * 设置xwalk背景
     * (注意：当前加载的页面必须已经定义过showHtml方法)
     */
    ResponseUtil<Object> setBackgroundColor(String color);

    /**
     * 播放AIPS里下载的节目
     */
    ResponseUtil<Object> playXixunProgramZip();

    /**
     * 播放直播
     */
    ResponseUtil<Object> playLiveVideo();

    /**
     * 停止直播
     */
    ResponseUtil<Object> stopLiveVideo();

    /**
     * 以字符串形式上传并保存html、图片等文件到sd卡
     */
    ResponseUtil<Object> saveSDStringFile();

    /**
     * 删除以字符串形式上传的文件
     */
    ResponseUtil<Object> deleteSDFile();

    /**
     * 下载文件到SD卡（图片、视频等任意文件）
     */
    ResponseUtil<Object> downloadFileToSD();

    /**
     * 删除SD上的文件
     */
    ResponseUtil<Object> deleteFileFromSD();

    /**
     * 获取SD卡上文件的大小
     */
    ResponseUtil<Object> getSDFileLength();

    /**
     * 停止默认播放器
     */
    ResponseUtil<Object> stopPlayer();

    /**
     * 下载文件到内部存储（图片、视频等任意文件）
     */
    ResponseUtil<Object> downloadFileToLocal();

    /**
     * 删除内部存储里的文件夹
     */
    ResponseUtil<Object> deleteFileFromLocal();

    /**
     * 查看内部存储里的文件大小
     */
    ResponseUtil<Object> getLocalFileLength();

    /**
     * 获取截图
     * （返回截图为base64编码的字符串（字符串中含有较多的\n换行符，需要用正则去掉才能正常显示，格式为png）
     */
    ResponseUtil<Object> getScreenshot();

    /**
     * 获取GPS坐标
     */
    ResponseUtil<Object> getGpsLocation();

    /**
     * 开关屏幕
     */
    ResponseUtil<Object> setScreenOpen(boolean isopen);

    /**
     * 获取屏幕是否打开
     */
    ResponseUtil<Object> getScreenOpenStutus();

    /**
     * 设置亮度
     */
    ResponseUtil<Object> setBrightness(String brightNum);

    /**
     * 提交JsonResponseUtil<Object>
     */
    ResponseUtil<Object> postJsonString(String jsonString);

    /**
     * 播放熙讯任务
     */
    ResponseUtil<Object> playXixunTask(String taskId);

    /**
     * 把schedule的属性转换类型成ScheduleVO
     */
    ScheduleVO tranToScheduleVO(Schedule schedule);


    /**
     * 获取亮度
     */
    ResponseUtil<Object> getBrightness();

    /**
     * 设置音量
     */
    ResponseUtil<Object> setVolume(Integer volumeNum);

    /**
     * 获取音量
     */
    ResponseUtil<Object> getVolume();

    /**
     * 获取屏宽
     */
    ResponseUtil<Object> getScreenWidth();

    /**
     * 获取屏高
     */
    ResponseUtil<Object> getScreenHeight();

    /**
     * 获取网络类型
     */
    ResponseUtil<Object> getNetworkType();

    /**
     * 设置NTP服务器或时区
     */
    ResponseUtil<Object> setTimeSync(String ntpServer, String timezone);

    /**
     * 获取NTP服务器
     */
    ResponseUtil<Object> getNtpServer();

    /**
     * 获取时区
     */
    ResponseUtil<Object> getTimezone();

    /**
     * 重启
     */
    ResponseUtil<Object> reboot();

    /**
     * 获取apk信息
     */
    ResponseUtil<Object> getPackageVersion();

    /**
     * 获取硬件状态
     */
    ResponseUtil<Object> getFpgaInfomation();

    /**
     * 在线更新app接口
     */
    ResponseUtil<Object> updateAPP();

    /**
     * 高级参数设置接口
     */
    ResponseUtil<Object> advancedConfig();

    /**
     * 同步时间设置接口
     */
    ResponseUtil<Object> setTimeSync2();

    /**
     * 设置自动亮度，亮度根据传感器数据变化自动调整
     */
    ResponseUtil<Object> setAutoBrightness(String sensitivity, String minBrightness);

    /**
     * 查询自动亮度
     */
    ResponseUtil<Object> getAutoBrightness();

    /**
     * 查询定时亮度接口
     */
    ResponseUtil<Object> getTimedBrightness();

    /**
     * 查询定时开关屏
     */
    ResponseUtil<Object> getTimedScreening();

    /**
     * 定时重启接口
     */
    ResponseUtil<Object> timedReboot(String time);

    /**
     * 查询定时重启时间
     */
    ResponseUtil<Object> getTimedReboot();

    /**
     * 清除播放器节目数据和文件
     */
    ResponseUtil<Object> clearPlayerTask();

    /**
     * 获取传感器数据接口
     */
    ResponseUtil<Object> getSensor();

    /**
     * 查询设备当前时间
     */
    ResponseUtil<Object> getControllerDate();

    /**
     * 查询设备磁盘空间
     */
    ResponseUtil<Object> getDiskSpace();

    /**
     * 查询播放器当前存储的节目JSON
     */
    ResponseUtil<Object> getProgramTask();

    /**
     * 查询播放器当前正在播放的节目名
     */
    ResponseUtil<Object> getPlayingProgram();

    /**
     * 获取服务器地址和公司id接口
     */
    ResponseUtil<Object> getServerAddress();

    /**
     * 开关WiFi/AP接口
     */
    ResponseUtil<Object> switchWiFi(boolean on);
}
