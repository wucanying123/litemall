package org.linlinjava.litemall.db.service.impl;//package org.linlinjava.litemall.db.service.impl;

import net.sf.json.JSONObject;
import org.linlinjava.litemall.db.dao.*;
import org.linlinjava.litemall.db.domain.*;
import org.linlinjava.litemall.db.service.ScreenService;
import org.linlinjava.litemall.db.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;


@Service
public class ScreenServiceImpl implements ScreenService {


    @Autowired
    private CommandMapper commandMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private LayerMapper layerMapper;
    @Autowired
    private ProgramMapper programMapper;
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private SourceMapper sourceMapper;
    @Autowired
    private TaskMapper taskMapper;

    private static Logger logger = LoggerFactory.getLogger(ScreenServiceImpl.class);

    /**
     * 加载顶层网页 （显示在其他界面之上，默认是透明的）
     */
    public ResponseUtil<Object> updateTopWeb(String weburl) {
        Map<String, String> params = new HashMap<>();
        params.put("type", "loadUrl");
        if (StringUtils.isEmpty(weburl)) {
            weburl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598413474585&di=b5ff6352b7f84132a22bada0cfb338fe&imgtype=0&src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2F201303%2F25%2F130453inwzdffbwv51gcs1.jpg";
        }
        params.put("url", weburl);
        params.put("persistent", "true");
        String result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 调用“顶层网页”里的js方法
     * （注意：js里面请不要添加alert这一类需要鼠标操作的代码，否则会卡住）
     * (前提：当前页面的window对象下必须有一个handleData方法（可以是任何方法名），例如:
     * window.handleData = function(data){
     * console.log(data)
     * })
     */
    public ResponseUtil<Object> updateTopWebJS() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "invokeJs");
        params.put("js", "handleData({id:'m2',content:'how/<br>are/<br>you2222/<br>?',direction:'down'})");
        String result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

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
    public ResponseUtil<Object> scrollingText(Integer num, String text, String color, Integer interval, Integer step, String direction, String align) {
        Map<String, String> params = new HashMap<>();
        params.put("type", "invokeBuildInJs");
        params.put("method", "scrollMarquee");
        if (StringUtils.isEmpty(num)) {
            num = 5;
        }
        params.put("num", String.valueOf(num));
        if (StringUtils.isEmpty(text)) {
            text = "这是</b>要滚动的文字";
        }
        if (StringUtils.isEmpty(color)) {
            color = "blue";
        }
        params.put("html", "<i><b style=\\\"color:" + color + ";\\\">" + text + "</i>");
        if (StringUtils.isEmpty(interval)) {
            interval = 50;
        }
        params.put("interval", String.valueOf(interval));
        if (StringUtils.isEmpty(step)) {
            step = 1;
        }
        params.put("step", String.valueOf(step));
        if (StringUtils.isEmpty(direction)) {
            direction = "left";
        }
        params.put("direction", direction);
        if (StringUtils.isEmpty(align)) {
            align = "top";
        }
        params.put("align", align);
        String result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 清屏
     */
    public ResponseUtil<Object> clearScreen() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "clear");
        String result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 启动xwalk （需要先在www.m2mled.net上安装xwalk，xwalk是一个支持html5的浏览器）
     */
    public ResponseUtil<Object> startActivity() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "startActivity");
        params.put("apk", "com.xixun.xy.xwalk");
        String result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 使用xwalk加载网页
     * (第一次使用xwalk加载网页时，务必先用《启动xwalk》命令打开xwalk才能继续使用xwalk加载网页。若persistent（持久化）为true，之后无需再次调用《启动xwalk》命令，重启控制卡后，系统会自动加载上一次的url；若为false，每次重启，开机后需再次调用《启动xwalk》命令才能继续使用xwalk加载网页。
     * 注意：js里面请不要添加alert这一类需要鼠标操作的代码，否则会卡住。郑重声明：请不要使用在网页中使用video标签，经测试长时间使用video标签播放视频会导致画面卡住！）
     */
    public ResponseUtil<Object> loadWebByActivity() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "callXwalkFn");
        params.put("fn", "loadUrl");
        params.put("arg", "{ \n" +
                "\t\t\"url\":\"" + Constant.LOCALHOST + ":" + Constant.WEBPORT + "/score.html\", \n" +
                "\t\t\"backupUrl\":\"file:///mnt/sdcard/res/backup.html\",*//备用地址,没网时自动加载此地址,（不做持久化时可以省略次url，如果url中显示端口号需要加上80，如http://www.codingke.com:80/v/1926-lesson-228-course）* \n" +
                "\t\t\"persistent\": true,  *//持久化，重启会自动加载url* \n" +
                "\t\t\"extra\":{ \t\t\t*//额外数据(可省略),它的值可以为任意类型，,在网页里使用window.\\$extra获取* \n" +
                "            \"a\":1, \n" +
                "            \"b\": true,\n" +
                "            \"c\":\"abc\"\n" +
                "\t\t} \n" +
                "\t} ");
        String result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 调用xwalk加载的网页里的js方法
     */
    public ResponseUtil<Object> updateWebJSByActivity() {
        String json = "{\n" +
                "\t\"type\": \"callXwalkFn\",\n" +
                "\t\"fn\": \"loadUrl\",\n" +
                "\t\"arg\": {\n" +
                "\t\t\"url\": \"http://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4\",\n" +
                "\t\t\"backupUrl\": \"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598593649260&di=ecf93bbae4762a649968bab47600b830&imgtype=0&src=http%3A%2F%2Fgss0.baidu.com%2F9vo3dSag_xI4khGko9WTAnF6hhy%2Fzhidao%2Fwh%253D450%252C600%2Fsign%3Deecf85adcf3d70cf4cafa209cdecfd36%2Fadaf2edda3cc7cd91ce1e34d3f01213fb90e9152.jpg\",\n" +
                "\t\t\"persistent\": true,\n" +
                "\t\t\"extra\": {\n" +
                "\t\t\t\"a\": 1,\n" +
                "\t\t\t\"b\": true,\n" +
                "\t\t\t\"c\": \"abc\"\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";
        String result = HttpUtil.postJsonString(Constant.URL, json);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 设置xwalk背景
     * (注意：当前加载的页面必须已经定义过showHtml方法)
     */
    public ResponseUtil<Object> setBackgroundColor(String color) {
        Map<String, String> params = new HashMap<>();
        params.put("type", "callXwalkFn");
        params.put("fn", "setBackgroundColor");
        if (StringUtils.isEmpty(color)) {
            color = "#666666";
        }
        params.put("arg", color);
        String result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 播放AIPS里下载的节目
     */
    public ResponseUtil<Object> playXixunProgramZip() {
        RequestData3 requestData = new RequestData3();
        requestData.setType("commandXixunPlayer");
        Command2 command = new Command2();
        command.set_type("PlayXixunProgramMp4");
        command.setPath("/data/data/com.xixun.xy.conn/files/local/abc/2.mp4");
        command.setPassword("abc");
        requestData.setCommand(command);
        String result = HttpUtil.postJsonObject(Constant.URL, requestData);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 播放直播
     */
    public ResponseUtil<Object> playLiveVideo() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "callLiveService");
        params.put("_type", "StartLiveVideo");
        params.put("url", "rtmp://58.200.131.2:1935/livetv/hunantv");
        params.put("width", "128");
        params.put("height", "320");
        String result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 播放直播
     */
    public ResponseUtil<Object> stopLiveVideo() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "callLiveService");
        params.put("_type", "StopLiveVideo");
        String result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 以字符串形式上传并保存html、图片等文件到sd卡
     */
    public ResponseUtil<Object> saveSDStringFile() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "saveSDStringFile");
//        params.put("fileName","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598440556009&di=658c561ef1daf8c80c9b2eb1a3d55804&imgtype=0&src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2F201407%2F31%2F182726essfa0380ygmly3h.jpg");
        params.put("fileName", "C:\\Users\\Admin\\Desktop\\timg.jpg");
        params.put("content", "base64Data");
        params.put("base64", "true");
        String result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 删除以字符串形式上传的文件
     */
    public ResponseUtil<Object> deleteSDFile() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "deleteFile");
        params.put("fileName", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598440556009&di=658c" +
                "561ef1daf8c80c9b2eb1a3d55804&imgtype=0&src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2F201407%2F31%2F182726essfa0380ygmly3h.jpg");
//        params.put("deleteAll","true");//如果要删除所有保存的文件，请取消对此的注释
        String result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 下载文件到SD卡（图片、视频等任意文件）
     */
    public ResponseUtil<Object> downloadFileToSD() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "downloadFileToSD");
        String fileName = "http://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4";
        params.put("url", fileName);
//        Date date = new Date();
        params.put("path", "/abc/1.mp4");
        String result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 删除SD上的文件
     */
    public ResponseUtil<Object> deleteFileFromSD() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "downloadFileToSD");
        params.put("path", "/201505/download.html");
        String result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 获取SD卡上文件的大小
     */
    public ResponseUtil<Object> getSDFileLength() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "getFileLength");
        params.put("path", "/201505/download.html");
        String result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 停止默认播放器
     */
    public ResponseUtil<Object> stopPlayer() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "stopPlayer");
        params.put("stop", "true");
        String result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 下载文件到内部存储（图片、视频等任意文件）
     */
    public ResponseUtil<Object> downloadFileToLocal() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "downloadFileToLocal");
        params.put("url", "http://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4");
        params.put("path", "/abc/2.mp4");
        String result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 删除内部存储里的文件夹
     */
    public ResponseUtil<Object> deleteFileFromLocal() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "deleteFileFromLocal");
        params.put("path", "/abc");
        String result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 查看内部存储里的文件大小
     */
    public ResponseUtil<Object> getLocalFileLength() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "getLocalFileLength");
        params.put("path", "/abc/demo.html");
        String result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 获取截图
     * （返回截图为base64编码的字符串（字符串中含有较多的\n换行符，需要用正则去掉才能正常显示，格式为png）
     */
    public ResponseUtil<Object> getScreenshot() {
        RequestData2 requestData2 = new RequestData2();
        requestData2.setType("callCardService");
        requestData2.setFn("screenshot");
        requestData2.setArg1(100);
        requestData2.setArg2(100);
        String result = HttpUtil.postJsonObject(Constant.URL, requestData2);
        JSONObject jsonObject = JSONObject.fromObject(result);
        String imgString = "";
        if (null != jsonObject.get("result")) {
            imgString = jsonObject.get("result").toString();
            imgString = StringUtilsXD.replaceBlank(imgString);
            jsonObject.put("result", imgString);
            result = jsonObject.toString();
        }
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 获取GPS坐标
     */
    public ResponseUtil<Object> getGpsLocation() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "getGpsLocation");
        params.put("fn", "screenshot");
        String result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 开关屏幕
     */
    public ResponseUtil<Object> setScreenOpen(boolean isopen) {
        RequestData2 requestData = new RequestData2();
        requestData.setType("callCardService");
        requestData.setFn("setScreenOpen");
        requestData.setArg1(isopen);
        String result = HttpUtil.postJsonObject(Constant.URL, requestData);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 获取屏幕是否打开
     */
    public ResponseUtil<Object> getScreenOpenStutus() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "callCardService");
        params.put("fn", "isScreenOpen");
        String result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 设置亮度
     */
    public ResponseUtil<Object> setBrightness(String brightNum) {
        RequestData2 requestData2 = new RequestData2();
        requestData2.setType("callCardService");
        requestData2.setFn("setBrightness");
        requestData2.setArg1(Integer.parseInt(brightNum));
        String result = HttpUtil.postJsonObject(Constant.URL, requestData2);

//        Map<String, Object> params = new HashMap<>();
//        params.put("type", "callCardService");
//        params.put("fn", "setBrightness");
//        params.put("arg1",Integer.parseInt(brightNum));
//        String result = HttpUtil.postMapObject(URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }


    /**
     * 提交JsonString
     */
    public ResponseUtil<Object> postJsonString(String jsonString) {
        if (StringUtils.isEmpty(jsonString)) {
            jsonString = "{ \n" +
                    "\t\"type\": \"clear\"\n" +
                    "}";
        }
        jsonString = StringUtilsXD.replaceBlank(jsonString);
        String result = HttpUtil.postJsonString(Constant.URL, jsonString);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 播放熙讯任务
     */
    public ResponseUtil<Object> playXixunTask(String taskId) {
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();

        Task task = taskMapper.selectByPrimaryKey(taskId);
        if(null == task){
            return responseUtil;
        }
        RequestData requestData = new RequestData();
        requestData.setType("commandXixunPlayer");
        Command command = new Command();
        command.set_type("PlayXixunTask");
        command.setId(UUID.randomUUID().toString().replace("-", ""));
        command.setPreDownloadURL("http://192.168.1.108:8081/file/download?id=");
        List<Item> items = new ArrayList<>();
        String itemsIds = task.getItemsIds();
        if (null != itemsIds && itemsIds.length() > 0) {
            List<String> itemsIdsList = Arrays.asList(itemsIds.split(","));
            for (String itemId : itemsIdsList) {
                Item item = itemMapper.selectByPrimaryKey(itemId);
                if (null != item) {
                    items.add(item);
                }
            }
        }
        if (null != items && items.size() > 0) {
            for (Item item : items) {
                //节目
                Program program = programMapper.selectByPrimaryKey(item.getProgramId());
                if (null != program) {
                    if (null != program.getCreateTime()) {
                        String dateCreated = DateUtil.toString(program.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
                        program.setDateCreated(dateCreated);
                    }
                    List<Layer> layers = new ArrayList<>();
                    String layersIds = program.getLayersIds();
                    if (null != layersIds && layersIds.length() > 0) {
                        List<String> layersIdsList = Arrays.asList(layersIds.split(","));
                        for (String layerId : layersIdsList) {
                            Layer layer = layerMapper.selectByPrimaryKey(layerId);
                            if (null != layer) {
                                layers.add(layer);
                            }
                        }
                    }
                    if (null != layers && layers.size() > 0) {
                        for (Layer layer : layers) {
                            layer.setRepeat(layer.getIsRepeat());
                            List<Source> sources = new ArrayList<>();
                            String sourcesIds = layer.getSourcesIds();
                            if (null != sourcesIds && sourcesIds.length() > 0) {
                                List<String> sourcesIdsList = Arrays.asList(sourcesIds.split(","));
                                for (String sourcesId : sourcesIdsList) {
                                    Source source = sourceMapper.selectByPrimaryKey(sourcesId);
                                    if (null != source) {
                                        source.setLeft(source.getTheLeft());
                                        sources.add(source);
                                    }
                                }
                                layer.setSources(sources);
                            }
                        }
                    }
                    program.setLayers(layers);
                }
                item.set_program(program);
                //定时列表
                String scheduleIds = item.getSchedulesIds();
                if (null != scheduleIds && scheduleIds.length() > 0) {
                    List<String> scheduleIdsList = Arrays.asList(scheduleIds.split(","));
                    List<Schedule> schedules = new ArrayList<>();
                    for (String scheduleId : scheduleIdsList) {
                        Schedule schedule = scheduleMapper.selectByPrimaryKey(scheduleId);
                        if (null != schedule) {
                            schedules.add(schedule);
                        }
                    }
                    if (null != schedules && schedules.size() > 0) {
                        List<ScheduleVO> scheduleVOList = new ArrayList<>();
                        for (Schedule schedule1 : schedules) {
                            ScheduleVO vo = tranToScheduleVO(schedule1);
                            scheduleVOList.add(vo);
                        }
                        item.setSchedules(scheduleVOList);
                    }
                }
            }
        }
        task.setItems(items);
        command.setTask(task);
        command.setTaskId(taskId);
        requestData.setCommand(command);
        String result = HttpUtil.postJsonObject(Constant.URL, requestData);
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }


    /**
     * 把schedule的属性转换类型成ScheduleVO
     */
    public ScheduleVO tranToScheduleVO(Schedule schedule) {
        ScheduleVO vo = new ScheduleVO();

        if (StringUtilsXD.isNotEmpty(schedule.getDateType())) {
            DateType equalDateType = null;
            for (DateType dateType : DateType.values()) {
                if (schedule.getTimeType().equals(dateType.name())) {
                    equalDateType = dateType;
                }
            }
            vo.setDateType(equalDateType);
        }

        if (null != schedule.getStartDate()) {
            String startDate = DateUtil.toString(schedule.getStartDate(), "yyyy-MM-dd");
            vo.setStartDate(startDate);
        }

        if (null != schedule.getEndDate()) {
            String endDate = DateUtil.toString(schedule.getEndDate(), "yyyy-MM-dd");
            vo.setEndDate(endDate);
        }

        if (StringUtilsXD.isNotEmpty(schedule.getTimeType())) {
            TimeType equalTimeType = null;
            for (TimeType timeType : TimeType.values()) {
                if (schedule.getTimeType().equals(timeType.name())) {
                    equalTimeType = timeType;
                }
            }
            vo.setTimeType(equalTimeType);
        }

        if (null != schedule.getStartTime()) {
            String startTime = DateUtil.toString(schedule.getStartTime(), "yyyy-MM-dd HH:mm:ss");
            vo.setStartTime(startTime);
        }

        if (null != schedule.getEndTime()) {
            String endTime = DateUtil.toString(schedule.getEndTime(), "yyyy-MM-dd HH:mm:ss");
            vo.setEndTime(endTime);
        }

        if (StringUtilsXD.isNotEmpty(schedule.getFilterType())) {
            FilterType equalFilterType = null;
            for (FilterType filterType : FilterType.values()) {
                if (schedule.getFilterType().equals(filterType.name())) {
                    equalFilterType = filterType;
                }
            }
            vo.setFilterType(equalFilterType);
        }

        if (null != schedule.getWeekFilter()) {
            List<String> weekStrList = Arrays.asList(schedule.getWeekFilter().split(","));
            List<Integer> weekIntList = new ArrayList<>();
            for (String str : weekStrList) {
                Integer intWeek = Integer.parseInt(str);
                weekIntList.add(intWeek);
            }
            vo.setWeekFilter(weekIntList);
        }

        if (null != schedule.getMonthFilter()) {
            List<String> monthStrList = Arrays.asList(schedule.getMonthFilter().split(","));
            List<Integer> monthIntList = new ArrayList<>();
            for (String str : monthStrList) {
                Integer intMonth = Integer.parseInt(str);
                monthIntList.add(intMonth);
            }
            vo.setMonthFilter(monthIntList);
        }

        vo.setId(schedule.getId());
        return vo;
    }

    /**
     * 获取亮度
     */
    public ResponseUtil<Object> getBrightness() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "callCardService");
        params.put("fn", "getBrightness");
        String result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 设置音量
     */
    public ResponseUtil<Object> setVolume(Integer volumeNum) {
        String result = "";
        try {
            RequestData2 requestData2 = new RequestData2();
            requestData2.setType("callCardService");
            requestData2.setFn("setVolume");
            requestData2.setArg1(volumeNum);
            result = HttpUtil.postJsonObject(Constant.URL, requestData2);
        } catch (Exception e) {
            logger.error("setVolume error and volumeNum = {}", volumeNum, e);
        }
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 获取音量
     */
    public ResponseUtil<Object> getVolume() {
        String result = "";
        Map<String, String> params = new HashMap<>();
        params.put("type", "callCardService");
        params.put("fn", "getVolume");
        result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 获取屏宽
     */
    public ResponseUtil<Object> getScreenWidth() {
        String result = "";
        Map<String, String> params = new HashMap<>();
        params.put("type", "callCardService");
        params.put("fn", "getScreenWidth");
        result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 获取屏高
     */
    public ResponseUtil<Object> getScreenHeight() {
        String result = "";
        Map<String, String> params = new HashMap<>();
        params.put("type", "callCardService");
        params.put("fn", "getScreenHeight");
        result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 获取网络类型
     */
    public ResponseUtil<Object> getNetworkType() {
        String result = "";
        Map<String, String> params = new HashMap<>();
        params.put("type", "callCardService");
        params.put("fn", "getNetworkType");
        result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }


    /**
     * 设置NTP服务器或时区
     */
    public ResponseUtil<Object> setTimeSync(String ntpServer, String timezone) {
        String result = "";
        try {
            RequestData2 requestData2 = new RequestData2();
            requestData2.setType("callCardService");
            requestData2.setFn("setTimeSync");
            requestData2.setArg1(ntpServer);
            requestData2.setArg2(timezone);
            result = HttpUtil.postJsonObject(Constant.URL, requestData2);
        } catch (Exception e) {
            logger.error("setTimeSync error and ntpServer = {},timezone = {}", ntpServer, timezone, e);
        }
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 获取NTP服务器
     */
    public ResponseUtil<Object> getNtpServer() {
        String result = "";
        Map<String, String> params = new HashMap<>();
        params.put("type", "callCardService");
        params.put("fn", "getNtpServer");
        result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 获取时区
     */
    public ResponseUtil<Object> getTimezone() {
        String result = "";
        Map<String, String> params = new HashMap<>();
        params.put("type", "callCardService");
        params.put("fn", "getTimezone");
        result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 重启
     */
    public ResponseUtil<Object> reboot() {
        String result = "";
        RequestData2 requestData2 = new RequestData2();
        requestData2.setType("callCardService");
        requestData2.setFn("reboot");
        requestData2.setArg1(1);
        result = HttpUtil.postJsonObject(Constant.URL, requestData2);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 获取apk信息
     */
    public ResponseUtil<Object> getPackageVersion() {
        String result = "";
        RequestData2 requestData2 = new RequestData2();
        requestData2.setType("getPackageVersion");
        requestData2.setApk("com.xixun.xixunplayer");
        result = HttpUtil.postJsonObject(Constant.URL, requestData2);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 获取硬件状态
     */
    public ResponseUtil<Object> getFpgaInfomation() {
        String result = "";
        Map<String, String> params = new HashMap<>();
        params.put("type", "callCardService");
        params.put("fn", "getFpgaInfomation");
        result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 在线更新app接口
     */
    public ResponseUtil<Object> updateAPP() {
        String result = "";
        Map<String, String> params = new HashMap<>();
        params.put("type", "updateAPP");
        params.put("appUrl", "https://m2mled.net/file/download?id=5c13839da62960b53cb07b42");
        result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 高级参数设置接口
     */
    public ResponseUtil<Object> advancedConfig() {
        String result = "";
        Map<String, String> params = new HashMap<>();
        params.put("type", "advancedConfig");
        params.put("serverURL", "www.m2mled.net");
        params.put("companyId", "18948769783");
        params.put("realtimeURL", Constant.LOCALHOST + ":" + Constant.WEBPORT);
        params.put("usbProgramPwd", null);
        result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }


    /**
     * 同步时间设置接口
     */
    public ResponseUtil<Object> setTimeSync2() {
        String result = "";
        Map<String, String> params = new HashMap<>();
        params.put("type", "serial");
        params.put("time", "www.m2mled.net");
        params.put("brightness", "none");
        params.put("volume", "none");
        params.put("screenSwitch", "none");
        params.put("identificationCode", "1");
        params.put("delaySync", "1");
        params.put("checkNtpTime", null);
        result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 设置自动亮度，亮度根据传感器数据变化自动调整
     */
    public ResponseUtil<Object> setAutoBrightness(String sensitivity, String minBrightness) {
        String result = "";
        Map<String, String> params = new HashMap<>();
        params.put("type", "setAutoBrightness");
        params.put("sensitivity", sensitivity);
        params.put("minBrightness", minBrightness);
        result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 查询自动亮度
     */
    public ResponseUtil<Object> getAutoBrightness() {
        String result = "";
        Map<String, String> params = new HashMap<>();
        params.put("type", "getAutoBrightness");
        result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

//        3.3.42、定时亮度，不同时段切换对应亮度值(conn10.0.5T或以上版本支持)
//        task格式请另行参阅相关java类
//
//        接口JSON格式举例：
//
//        {
//        "type": "timedBrightness",
//        "task":{
//        "_id":"591d519f5e3f190f697aaf18",
//        "defaultBrightness":6,
//        "name":"Timing Brightness",
//        "_company":"alahover",
//        "_department":"539eaaedb6e1232a1566d9c2",
//        "_role":"539eaaedb6e1232a1566d9c3",
//        "_user":"Emily2",
//        "__v":0,
//        "items":[
//        {
//        "brightness":32,
//        "_id":"591d519f5e3f190f697aaf1a",
//        "schedules":[
//        {
//        "dateType":"All",
//        "startDate":null,
//        "endDate":null,
//        "timeType":"Range",
//        "startTime":"08:30",
//        "endTime":"18:00",
//        "filterType":"None",
//        "weekFilter":[
//
//        ],
//        "monthFilter":[
//
//        ],
//        "lng":"en"
//        }
//        ],
//        "id":"591d519f5e3f190f697aaf1a"
//        }
//        ],
//        "dateCreated":"2017-05-18T07:47:43.590Z",
//        "createDate":"2017-05-18T07:47:43.590Z",
//        "createBy":"Emily2",
//        "id":"591d519f5e3f190f697aaf18",
//        "lng":"zh-CN"
//        }
//        }
//        返回成功格式：
//
//        {
//        "_type": "success",
//        "_id": "e3147a0c-d792-48c1-aaec-dfe2cf1e2aea",
//        "timestamp": 1553847710833
//        }


    /**
     * 查询定时亮度接口
     */
    public ResponseUtil<Object> getTimedBrightness() {
        String result = "";
        Map<String, String> params = new HashMap<>();
        params.put("type", "getTimedBrightness");
        result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

//
//        3.3.44、设置定时开关屏(conn10.0.5T或以上版本支持)
//        JSON数据格式举例：
//
//        {
//        "type": "timedScreening",
//        "task":{
//        "_id":"5ba098d0502da61b67899c09",
//        "name":"7-0.58",
//        "_company":"alahover",
//        "_department":"539eaaedb6e1232a1566d9c2",
//        "_role":"539eaaedb6e1232a1566d9c3",
//        "_user":"check",
//        "__v":0,
//        "schedules":[
//        {
//        "lng":"zh-CN",
//        "monthFilter":[
//
//        ],
//        "weekFilter":[
//
//        ],
//        "filterType":"None",
//        "endTime":"00:58",
//        "startTime":"07:00",
//        "timeType":"Range",
//        "endDate":null,
//        "startDate":null,
//        "dateType":"All"
//        }
//        ],
//        "dateCreated":"2018-09-18T06:18:56.513Z",
//        "createDate":"2018-09-18T06:18:56.513Z",
//        "createBy":"check",
//        "id":"5ba098d0502da61b67899c09",
//        "lng":"zh-CN"
//        }
//        }
//        注意：task数据结构同接口6中的task，参考相关java类
//

    /**
     * 查询定时开关屏
     */
    public ResponseUtil<Object> getTimedScreening() {
        String result = "";
        Map<String, String> params = new HashMap<>();
        params.put("type", "getTimedScreening");
        result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 定时重启接口
     */
    public ResponseUtil<Object> timedReboot(String time) {
        String result = "";
        Map<String, String> params = new HashMap<>();
        params.put("type", "timedReboot");
        params.put("time", time);
        result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 查询定时重启时间
     */
    public ResponseUtil<Object> getTimedReboot() {
        String result = "";
        Map<String, String> params = new HashMap<>();
        params.put("type", "getTimedReboot");
        result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 清除播放器节目数据和文件
     */
    public ResponseUtil<Object> clearPlayerTask() {
        String result = "";
        Map<String, String> params = new HashMap<>();
        params.put("type", "clearPlayerTask");
        result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 获取传感器数据接口
     */
    public ResponseUtil<Object> getSensor() {
        String result = "";
        Sensor sensor = new Sensor();
        sensor.setType("callCardService");
        sensor.setAction("xixun.intent.action.TEMPERATURE_HUMIDITY");
        sensor.setCallbackURL(null);
        sensor.setSubscribe(true);
        result = HttpUtil.postJsonObject(Constant.URL, sensor);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 查询设备当前时间
     */
    public ResponseUtil<Object> getControllerDate() {
        String result = "";
        Map<String, String> params = new HashMap<>();
        params.put("type", "getControllerDate");
        result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 查询设备磁盘空间
     */
    public ResponseUtil<Object> getDiskSpace() {
        String result = "";
        Map<String, String> params = new HashMap<>();
        params.put("type", "getDiskSpace");
        result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 查询播放器当前存储的节目JSON
     */
    public ResponseUtil<Object> getProgramTask() {
        String result = "";
        Map<String, String> params = new HashMap<>();
        params.put("type", "getProgramTask");
        result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 查询播放器当前正在播放的节目名
     */
    public ResponseUtil<Object> getPlayingProgram() {
        String result = "";
        Map<String, String> params = new HashMap<>();
        params.put("type", "getPlayingProgram");
        result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 获取服务器地址和公司id接口
     */
    public ResponseUtil<Object> getServerAddress() {
        String result = "";
        Map<String, String> params = new HashMap<>();
        params.put("type", "getServerAddress");
        result = HttpUtil.postMap(Constant.URL, params);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }

    /**
     * 开关WiFi/AP接口
     */
    public ResponseUtil<Object> switchWiFi(boolean on) {
        String result = "";
        SwitchWiFi switchWiFi = new SwitchWiFi();
        switchWiFi.set_id("f237643d-c35c");
        switchWiFi.setType("switchWiFi");
        switchWiFi.setOn(on);
        result = HttpUtil.postJsonObject(Constant.URL, switchWiFi);
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        responseUtil = StringUtilsXD.setResponseUtil(responseUtil, result);
        return responseUtil;
    }
}
