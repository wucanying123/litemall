package org.linlinjava.litemall.admin.web;

import com.xinda.UDPSocket.UDPClient;
import com.xinda.common.BaseResp;
import com.xinda.common.Constant;
import com.xinda.config.ServerConfig;
import com.xinda.screen.service.ScreenService;
import com.xinda.test.Service.TestService;
import com.xinda.util.StringUtilsXD;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/screen")
public class ScreenController {
    private static Logger logger = LoggerFactory.getLogger(ScreenController.class);

    @Autowired
    private ScreenService screenService;
    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private TestService testService;

    @GetMapping(value = "/print")
    public void getModel(String key) {
        System.out.println(key);
        System.out.println(serverConfig.getUrl());
    }


    /**
     * 获取文件长度
     */
    @GetMapping(value = "/getFileSize")
    public void getFileSize() {
        File file = new File("C:/Users/Admin/Desktop/source/tool.mp4");
        if (file.exists() && file.isFile()) {
            String fileName = file.getName();
            System.out.println("文件"+fileName+"的大小是："+file.length());
        }
    }

    @ApiOperation(value = "通过UDP广播找卡")
    @PostMapping(value = "/udpFindCard")
    public BaseResp<Object> udpFindCard() {
        BaseResp<Object> baseResp = new BaseResp<>();
        String jsonString = "{action:getInfo}";
        String resultString = UDPClient.getCardInfo(jsonString);
        if(!StringUtilsXD.isEmpty(resultString)) {
            int indexStart = resultString.indexOf("{");
            int indexEnd = resultString.indexOf("}");
            String result = resultString.substring(indexStart, indexEnd + 1);
            baseResp = StringUtilsXD.setBaseResp(baseResp, result);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        }
        return baseResp;
    }

    @ApiOperation(value = "1、加载顶层网页")
    @PostMapping(value = "/updateTopWeb")
    public BaseResp<Object> updateTopWeb(String weburl) {
        return screenService.updateTopWeb(weburl);
    }

    @ApiOperation(value = "2、调用“顶层网页”里的js方法",
            notes = "<br/>（注意：js里面请不要添加alert这一类需要鼠标操作的代码，否则会卡住）\n" +
                    "<br/>(前提：当前页面的window对象下必须有一个handleData方法（可以是任何方法名），例如: \n" +
                    "<br/>window.handleData = function(data){\n" +
                    "<br/>\tconsole.log(data) \n" +
                    "<br/>})")
    @PostMapping(value = "/updateTopWebJS")
    public BaseResp<Object> updateTopWebJS() {
        return screenService.updateTopWebJS();
    }

    @ApiOperation(value = "3、滚动文字",
            notes = "<br/>滚动次数, 注意类型为Number, 填0停止滚动，填负数永久滚动\n" +
                    "<br/>text 滚动的文字\n" +
                    "<br/>color 文字颜色\n" +
                    "<br/>interval 步进间隔，单位毫秒，注意类型为Number\n" +
                    "<br/>step 步进距离，单位像素，注意类型为Number\n" +
                    "<br/>direction 往左滚动，可填值left、 right\n" +
                    "<br/>align 在上方显示，可填值top、center、bottom")
    @PostMapping(value = "/scrollingText")
    public BaseResp<Object> scrollingText(Integer num, String text, String color, Integer interval, Integer step, String direction, String align) {
        return screenService.scrollingText(num, text, color, interval, step, direction, align);
    }

    @ApiOperation(value = "4、清屏")
    @PostMapping(value = "/clearScreen")
    public BaseResp<Object> clearScreen() {
        return screenService.clearScreen();
    }

    @ApiOperation(value = "5、启动xwalk",
            notes = "<br/>启动xwalk （需要先在www.m2mled.net上安装xwalk，xwalk是一个支持html5的浏览器）")
    @PostMapping(value = "/startActivity")
    public BaseResp<Object> startActivity() {
        return screenService.startActivity();
    }

    @ApiOperation(value = "6、使用xwalk加载网页")
    @PostMapping(value = "/loadWebByActivity")
    public BaseResp<Object> loadWebByActivity() {
        return screenService.loadWebByActivity();
    }

    @ApiOperation(value = "7、调用xwalk加载的网页里的js方法")
    @PostMapping(value = "/updateWebJSByActivity")
    public BaseResp<Object> updateWebJSByActivity() {
        return screenService.updateWebJSByActivity();
    }

    @ApiOperation(value = "8、设置xwalk背景", notes = "注意：当前加载的页面必须已经定义过showHtml方法")
    @PostMapping(value = "/setBackgroundColor")
    public BaseResp<Object> setBackgroundColor(String color) {
        return screenService.setBackgroundColor(color);
    }

    @ApiOperation(value = "9、播放AIPS里下载的节目")
    @PostMapping(value = "/playXixunProgramZip")
    public BaseResp<Object> playXixunProgramZip() {
        return screenService.playXixunProgramZip();
    }

    @ApiOperation(value = "10、播放直播")
    @PostMapping(value = "/playLiveVideo")
    public BaseResp<Object> playLiveVideo() {
        return screenService.playLiveVideo();
    }

    @ApiOperation(value = "10、停止直播")
    @PostMapping(value = "/stopLiveVideo")
    public BaseResp<Object> stopLiveVideo() {
        return screenService.stopLiveVideo();
    }

    @ApiOperation(value = "11、以字符串形式上传并保存html、图片等文件到sd卡")
    @PostMapping(value = "/saveSDStringFile")
    public BaseResp<Object> saveSDStringFile() {
        return screenService.saveSDStringFile();
    }

    @ApiOperation(value = "12、删除以字符串形式上传的文件")
    @PostMapping(value = "/deleteSDFile")
    public BaseResp<Object> deleteSDFile() {
        return screenService.deleteSDFile();
    }

    @ApiOperation(value = "13、下载文件到SD卡（图片、视频等任意文件）")
    @PostMapping(value = "/downloadFileToSD")
    public BaseResp<Object> downloadFileToSD() {
        return screenService.downloadFileToSD();
    }

    @ApiOperation(value = "14、删除SD上的文件")
    @PostMapping(value = "/deleteFileFromSD")
    public BaseResp<Object> deleteFileFromSD() {
        return screenService.deleteFileFromSD();
    }

    @ApiOperation(value = "15、获取SD卡上文件的大小")
    @PostMapping(value = "/getSDFileLength")
    public BaseResp<Object> getSDFileLength() {
        return screenService.getSDFileLength();
    }

    @ApiOperation(value = "16、停止默认播放器")
    @PostMapping(value = "/stopPlayer")
    public BaseResp<Object> stopPlayer() {
        return screenService.stopPlayer();
    }

    @ApiOperation(value = "17、下载文件到内部存储（图片、视频等任意文件）")
    @PostMapping(value = "/downloadFileToLocal")
    public BaseResp<Object> downloadFileToLocal() {
        return screenService.downloadFileToLocal();
    }

    @ApiOperation(value = "18、删除内部存储里的文件夹")
    @PostMapping(value = "/deleteFileFromLocal")
    public BaseResp<Object> deleteFileFromLocal() {
        return screenService.deleteFileFromLocal();
    }

    @ApiOperation(value = "19、查看内部存储里的文件大小")
    @PostMapping(value = "/getLocalFileLength")
    public BaseResp<Object> getLocalFileLength() {
        return screenService.getLocalFileLength();
    }

    @ApiOperation(value = "20、获取截图", notes = "返回截图为base64编码的字符串（字符串中含有较多的\\n换行符，需要用正则去掉才能正常显示，格式为png")
    @PostMapping(value = "/getScreenshot")
    public BaseResp<Object> getScreenshot() {
        return screenService.getScreenshot();
    }

    @ApiOperation(value = "21、获取GPS坐标")
    @PostMapping(value = "/getGpsLocation")
    public BaseResp<Object> getGpsLocation() {
        return screenService.getGpsLocation();
    }

    @ApiOperation(value = "22、开关屏幕")
    @PostMapping(value = "/setScreenOpen")
    public BaseResp<Object> setScreenOpen(boolean isopen) {
        return screenService.setScreenOpen(isopen);
    }

    @ApiOperation(value = "23、获取屏幕是否打开")
    @PostMapping(value = "/getScreenOpenStutus")
    public BaseResp<Object> getScreenOpenStutus() {
        return screenService.getScreenOpenStutus();
    }

    @ApiOperation(value = "24、设置亮度")
    @PostMapping(value = "/setBrightness")
    public BaseResp<Object> setBrightness(String brightnum) {
        return screenService.setBrightness(brightnum);
    }

    @ApiOperation(value = "提交JsonString")
    @PostMapping(value = "/postJsonString")
    public BaseResp<Object> postJsonString(String jsonString) {
        return screenService.postJsonString(jsonString);
    }

    @ApiOperation(value = "播放熙讯任务")
    @PostMapping(value = "/playXixunTask")
    public BaseResp<Object> playXixunTask(@RequestParam(value = "taskId") String taskId) {
        return screenService.playXixunTask(taskId);
    }

    @ApiOperation(value = "测试")
    @PostMapping(value = "/test")
    public void testService() {
        testService.imitateAddSource(null, null, null);
    }

    @ApiOperation(value = "25、获取亮度")
    @PostMapping(value = "/getBrightness")
    public BaseResp<Object> getBrightness() {
        return screenService.getBrightness();
    }

    @ApiOperation(value = "26、设置音量")
    @PostMapping(value = "/setVolume")
    public BaseResp<Object> setVolume(String volumenum) {
        logger.info("setVolume and volumenum={}", volumenum);
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtilsXD.isEmpty(volumenum)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);

        }
        return screenService.setVolume(Integer.parseInt(volumenum));
    }

    @ApiOperation(value = "27、获取音量")
    @PostMapping(value = "/getVolume")
    public BaseResp<Object> getVolume() {
        return screenService.getVolume();
    }

    @ApiOperation(value = "28、获取屏宽")
    @PostMapping(value = "/getScreenWidth")
    public BaseResp<Object> getScreenWidth() {
        return screenService.getScreenWidth();
    }

    @ApiOperation(value = "29、获取屏高")
    @PostMapping(value = "/getScreenHeight")
    public BaseResp<Object> getScreenHeight() {
        return screenService.getScreenHeight();
    }

    @ApiOperation(value = "30、获取网络类型")
    @PostMapping(value = "/getNetworkType")
    public BaseResp<Object> getNetworkType() {
        return screenService.getNetworkType();
    }

    @ApiOperation(value = "31、设置NTP服务器或时区")
    @PostMapping(value = "/setTimeSync")
    BaseResp<Object> setTimeSync(String ntpserver, String timezone) {
        logger.info("setTimeSync and ntpServer = {},timezone = {}", ntpserver, timezone);
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtilsXD.hasBlankParams(ntpserver, timezone)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);

        }
        return screenService.setTimeSync(ntpserver, timezone);
    }

    @ApiOperation(value = "32、获取NTP服务器")
    @PostMapping(value = "/getNtpServer")
    public BaseResp<Object> getNtpServer() {
        return screenService.getNtpServer();
    }

    @ApiOperation(value = "33、获取时区")
    @PostMapping(value = "/getTimezone")
    public BaseResp<Object> getTimezone() {
        return screenService.getTimezone();
    }

    @ApiOperation(value = "34、重启")
    @PostMapping(value = "/reboot")
    public BaseResp<Object> reboot() {
        return screenService.reboot();
    }

    @ApiOperation(value = "35、获取apk信息")
    @PostMapping(value = "/getPackageVersion")
    public BaseResp<Object> getPackageVersion() {
        return screenService.getPackageVersion();
    }

    @ApiOperation(value = "36、获取硬件状态")
    @PostMapping(value = "/getFpgaInfomation")
    public BaseResp<Object> getFpgaInfomation() {
        return screenService.getFpgaInfomation();
    }

    @ApiOperation(value = "37、在线更新app接口")
    @PostMapping(value = "/updateAPP")
    public BaseResp<Object> updateAPP() {
        return screenService.updateAPP();
    }

    @ApiOperation(value = "38、高级参数设置接口")
    @PostMapping(value = "/advancedConfig")
    public BaseResp<Object> advancedConfig() {
        return screenService.advancedConfig();
    }

    @ApiOperation(value = "39、同步时间设置接口")
    @PostMapping(value = "/setTimeSync2")
    public BaseResp<Object> setTimeSync2() {
        return screenService.setTimeSync2();
    }

    @ApiOperation(value = "40、设置自动亮度，亮度根据传感器数据变化自动调整")
    @PostMapping(value = "/setAutoBrightness")
    public BaseResp<Object> setAutoBrightness(String sensitivity, String minBrightness) {
        return screenService.setAutoBrightness(sensitivity, minBrightness);
    }

    @ApiOperation(value = "41、查询自动亮度")
    @PostMapping(value = "/getAutoBrightness")
    public BaseResp<Object> getAutoBrightness() {
        return screenService.getAutoBrightness();
    }

    @ApiOperation(value = "43、查询定时亮度接口")
    @PostMapping(value = "/getTimedBrightness")
    public BaseResp<Object> getTimedBrightness() {
        return screenService.getTimedBrightness();
    }


    @ApiOperation(value = "45、查询定时开关屏")
    @PostMapping(value = "/getTimedScreening")
    public BaseResp<Object> getTimedScreening() {
        return screenService.getTimedScreening();
    }

    @ApiOperation(value = "46、定时重启接口")
    @PostMapping(value = "/timedReboot")
    public BaseResp<Object> timedReboot(String time) {
        return screenService.timedReboot(time);
    }

    @ApiOperation(value = "47、查询定时重启时间")
    @PostMapping(value = "/getTimedReboot")
    public BaseResp<Object> getTimedReboot() {
        return screenService.getTimedReboot();
    }

    @ApiOperation(value = "48、清除播放器节目数据和文件")
    @PostMapping(value = "/clearPlayerTask")
    public BaseResp<Object> clearPlayerTask() {
        return screenService.clearPlayerTask();
    }

    @ApiOperation(value = "49、获取传感器数据接口")
    @PostMapping(value = "/getSensor")
    public BaseResp<Object> getSensor() {
        return screenService.getSensor();
    }

    @ApiOperation(value = "50、查询设备当前时间")
    @PostMapping(value = "/getControllerDate")
    public BaseResp<Object> getControllerDate() {
        return screenService.getControllerDate();
    }

    @ApiOperation(value = "51、查询设备磁盘空间")
    @PostMapping(value = "/getDiskSpace")
    public BaseResp<Object> getDiskSpace() {
        return screenService.getDiskSpace();
    }

    @ApiOperation(value = "52、查询播放器当前存储的节目JSON")
    @PostMapping(value = "/getProgramTask")
    public BaseResp<Object> getProgramTask() {
        return screenService.getProgramTask();
    }

    @ApiOperation(value = "53、查询播放器当前正在播放的节目名")
    @PostMapping(value = "/getPlayingProgram")
    public BaseResp<Object> getPlayingProgram() {
        return screenService.getPlayingProgram();
    }

    @ApiOperation(value = "54、获取服务器地址和公司id接口")
    @PostMapping(value = "/getServerAddress")
    public BaseResp<Object> getServerAddress() {
        return screenService.getServerAddress();
    }

    @ApiOperation(value = "55、开关WiFi/AP接口")
    @PostMapping(value = "/switchWiFi")
    public BaseResp<Object> switchWiFi(boolean on) {
        return screenService.switchWiFi(on);
    }
}
