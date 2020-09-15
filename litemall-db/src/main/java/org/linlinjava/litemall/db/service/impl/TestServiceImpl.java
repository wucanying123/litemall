package org.linlinjava.litemall.db.service.impl;

import org.linlinjava.litemall.db.dao.*;
import org.linlinjava.litemall.db.domain.*;
import org.linlinjava.litemall.db.service.TestService;
import org.linlinjava.litemall.db.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TestServiceImpl implements TestService {
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

    private static Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);

    /**
     * 模拟添加熙讯任务
     */
    public ResponseUtil<Object> imitateAddXixunTask() {
        ResponseUtil<Object> responseUtil = new ResponseUtil<>();
        try {
            Command command = new Command();
            command.set_type("PlayXixunTask");
            command.setId("3be5e443-d025-4f8d-b708-62ac89c5b5fe");
            command.setPreDownloadURL("https://m2mled.net/file/download?id=");
            Task task = new Task();
            task.set_id("55f5b637-a529-4807-8063-deeb3c12f9ab");
            task.setName("demo");
            task.set_department(null);
            task.setExecuteDate(null);
            task.setCmdId("55f5b637-a529-4807-8063-deeb3c12f9ab");
            List<Item> items = new ArrayList<>();
            Item item = new Item();
            item.set_id("0d10f114-b93d-4eb7-a1a7-60311eeab6b2");
            item.setPriority(0);
            item.setRepeatTimes(1);
            item.setVersion(0);
            Program program = new Program();
            program.set_id("5c909eca4477c9247940613b");
            program.setTotalSize(14545722);
            program.setName("name");
            program.setWidth(300);
            program.setHeight(240);
            program.set_company("alahover");
            program.set_department("539eaaedb6e1232a1566d9c2");
            program.set_role("539eaaedb6e1232a1566d9c3");
            program.set_user("yzd");
            program.set__v(0);
            program.setDateCreated("2019-03-19T07:48:26.984Z");
            List<Layer> layers = new ArrayList<>();
            Layer layer = new Layer();
            layer.setRepeat(false);


            layers.add(layer);
            program.setLayers(layers);
            item.set_program(program);

////        ------------------ScheduleList Start------------------
//            List<Schedule> schedules = new ArrayList<>();
//            Schedule schedule = new Schedule();
//            schedule.setDateType(DateType.All);
//            schedule.setStartDate(null);
//            schedule.setEndDate(null);
//            schedule.setTimeType(TimeType.All);
//            schedule.setStartTime(null);
//            schedule.setEndTime(null);
//            schedule.setFilterType(FilterType.Week);
//            List<Integer> weekFilter = new ArrayList<>();
//            weekFilter.add(1);
//            weekFilter.add(2);
//            weekFilter.add(3);
//            schedule.setWeekFilter(weekFilter);
//            schedule.setMonthFilter(null);
//
////        Schedule schedule1 = new Schedule();
////        schedule1.setDateType(DateType.All);
////        schedule1.setStartDate(null);
////        schedule1.setEndDate(null);
////        schedule1.setTimeType(TimeType.Range);
////        schedule1.setStartTime("00:00");
////        schedule1.setEndTime("23:59");
////        schedule1.setFilterType(FilterType.None);
////        schedule1.setWeekFilter(null);
////        schedule1.setMonthFilter(null);
////
////        Schedule schedule2 = new Schedule();
////        schedule2.setDateType(DateType.Range);
////        schedule2.setStartDate("2020-08-27");
////        schedule2.setEndDate("2020-09-26");
////        schedule2.setTimeType(TimeType.All);
////        schedule2.setStartTime(null);
////        schedule2.setEndTime(null);
////        schedule2.setFilterType(FilterType.None);
////        schedule2.setWeekFilter(null);
////        schedule2.setMonthFilter(null);
//
//            schedules.add(schedule);
////        schedules.add(schedule1);
////        schedules.add(schedule2);
//
//            item.setSchedules(schedules);
//        ------------------ScheduleList End------------------

            items.add(item);
            task.setItems(items);
            command.setTask(task);
            responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("imitateAddXixunTask error", e);
        }
        return responseUtil;
    }

    public void imitateAddSource(String path, String sourceName, String fileExt) {
//        sourceName="tool";
//        fileExt="mp4";
        List<Source> sources = new ArrayList<>();
        Source vedioSource = new Source();
//        String fileUrl = serverConfig.getUrl()+"/file/tool.mp4";
//        vedioSource.setUrl(fileUrl);
        vedioSource.setMaxPlayTime(7);
        vedioSource.set_type("Video");

//        vedioSource.setName(sourceName+"."+fileExt);
        vedioSource.setName("tool.mp4");
        vedioSource.setMime("video/mp4");
//        path = "C:/Users/Admin/Desktop/source/"+sourceName+fileExt;
        path = "C:/Users/Admin/Desktop/source/tool.mp4";
        File file = new File(path);
        Long fileSize= 0L;
        if (file.exists() && file.isFile()) {
            String fileName = file.getName();
            System.out.println("文件"+fileName+"的大小是："+file.length());
            fileSize=file.length();
        }
        String md5 = StringUtilsXD.getFileMD5(file);
        vedioSource.setMd5(md5);
        vedioSource.setSize(fileSize);
        vedioSource.setEnabled(true);
        vedioSource.setEnabledBy("check");
        vedioSource.setMediaGroup(null);
        vedioSource.setDeletedBy(null);
        vedioSource.setDeleted(null);
        vedioSource.setNewName(null);
        vedioSource.setOldFilePath(null);
        vedioSource.setFileExt(".mp4");
        vedioSource.setId(UUID.randomUUID().toString().replace("-", ""));
        vedioSource.setPlayTime(0);
        vedioSource.setTimeSpan(VideoUtil.getPlayTime(file).intValue());
        vedioSource.setMaxPlayTime(VideoUtil.getPlayTime(file).intValue());
        vedioSource.setTheLeft(0);
        vedioSource.setTop(0);
        vedioSource.setWidth(180);
        vedioSource.setHeight(320);
        vedioSource.setEntryEffect("None");
        vedioSource.setExitEffect("None");
        vedioSource.setEntryEffectTimeSpan(0);
        vedioSource.setExitEffectTimeSpan(0);
        vedioSource.setLanguage("en");

        String path1 = "C:/Users/Admin/Desktop/source/wu.jpg";
        File file1 = new File(path1);
        Source source1 = new Source();
//        String fileUrl1 = serverConfig.getUrl()+"/file/wu.jpg";
//        source1.setUrl(fileUrl1);
        source1.set_type("Image");
        source1.setName("wu.jpg");
        source1.setMime("image/jpeg");
        Long fileSize1= 0L;
        if (file1.exists() && file1.isFile()) {
            String fileName1 = file1.getName();
            System.out.println("文件1"+fileName1+"的大小是："+file1.length());
            fileSize1=file1.length();
        }
        String md51 = StringUtilsXD.getFileMD5(file1);
        source1.setMd5(md51);
        source1.setSize(fileSize1);
        source1.setEnabled(true);
        source1.setEnabledBy("admin");
        source1.setMediaGroup(null);
        source1.setDeletedBy(null);
        source1.setDeleted(null);
        source1.setNewName(null);
        source1.setOldFilePath(null);
        source1.setFileExt(".jpg");
        source1.setId(UUID.randomUUID().toString().replace("-", ""));
        source1.setPlayTime(0);
        source1.setTimeSpan(10);
        source1.setMaxPlayTime(10);
        source1.setTheLeft(0);
        source1.setTop(0);
        source1.setWidth(180);
        source1.setHeight(320);
        source1.setEntryEffect("None");
        source1.setExitEffect("None");
        source1.setEntryEffectTimeSpan(0);
        source1.setExitEffectTimeSpan(0);
        source1.setLanguage("en");

//        Source source2 = new Source();
//        source2.setId(UUID.randomUUID().toString().replace("-", ""));
//        source2.setName(sourceName + "." + fileExt);
//        source2.set_type("AnalogClock");
//        source2.setShade(0);
//        source2.setOpacity(1);
//        source2.setShowBg(true);
//        source2.setBgColor("#482c51");
//        source2.setShowHourScale(true);
//        source2.setScaleHourColor("#12229c");
//        source2.setShowMinScale(true);
//        source2.setScaleMinColor("#3bc73b");
//        source2.setScaleStyle(3);
//        source2.setShowScaleNum(true);
//        source2.setPinStyle(1);
//        source2.setPinHourColor("#ff0000");
//        source2.setPinMinColor("#00ffd2");
//        source2.setPinSecColor("#fbca00");
//        source2.setShowSecond(true);
//        source2.setPlayTime(0);
//        source2.setTimeSpan(100);
//        source2.setLeft(0);
//        source2.setTop(0);
//        source2.setWidth(150);
//        source2.setHeight(120);
//        source2.setEntryEffect("None");
//        source2.setExitEffect("None");
//        source2.setEntryEffectTimeSpan(0);
//        source2.setExitEffectTimeSpan(0);

        sources.add(vedioSource);
        sources.add(source1);
        sourceMapper.insertSelective(vedioSource);
        sourceMapper.insertSelective(source1);
        imitateAddLayer(sources);
    }

    private String getSourceType(String fileExt) {
        String type = "";
        return type;
    }

    private String getSourceMime(String fileExt) {
        String type = "";
        return type;
    }

    public void imitateAddLayer(List<Source> sources) {
        List<Layer> layers = new ArrayList<>();
        Layer layer = new Layer();
        String sourcesIds = "";
        if(null != sources && sources.size() >0){
            List<String> idList = new ArrayList<>();
            for(Source source : sources){
                String id = source.getId();
                idList.add(id);
            }
            sourcesIds = String.join("," , idList);
        }
        layer.setSourcesIds(sourcesIds);
        layer.setId(UUID.randomUUID().toString().replace("-", ""));
        layer.setIsRepeat(true);
        layerMapper.insertSelective(layer);
        layers.add(layer);
        imitateAddProgram("test", layers);
    }


    public void imitateAddProgram(String programName,List<Layer> layers) {
        Program program = new Program();
        String layersIds = "";
        if(null != layers && layers.size() >0){
            List<String> idList = new ArrayList<>();
            for(Layer layer : layers){
                String id = layer.getId();
                idList.add(id);
            }
            layersIds = String.join("," , idList);
        }
        program.setLayersIds(layersIds);
        program.set_id(UUID.randomUUID().toString().replace("-", ""));
        program.setTotalSize(null);
        program.setName(programName);
        program.setWidth(180);
        program.setHeight(320);
        program.set_company("xinda");
        program.set_department("development");
        program.set_role("admin");
        program.set_user("xinda");
        program.set__v(0);
        long cuttentTime = DateUtil.getDateline();
        program.setCreateTime(cuttentTime);
        program.setUpdateTime(cuttentTime);
        programMapper.insertSelective(program);
        imitateAddItems( program);
    }

    public void imitateAddItems(Program program) {
        List<Schedule> schedules = new ArrayList<>();
        Schedule schedule = new Schedule();
        schedule.setId(UUID.randomUUID().toString().replace("-", ""));
        schedule.setDateType(DateType.All.toString());
        schedule.setTimeType(TimeType.All.toString());
        schedule.setFilterType(FilterType.Week.toString());
        schedule.setWeekFilter("0,1,2,3,4,5,6");
        schedule.setLanguage("zh-CN");
        scheduleMapper.insertSelective(schedule);

//        Schedule schedule1 = new Schedule();
//        schedule1.setId(UUID.randomUUID().toString().replace("-", ""));
//        schedule1.setDateType(DateType.All.toString());
//        schedule1.setTimeType(TimeType.Range.toString());
//        schedule1.setFilterType(FilterType.None.toString());
//        schedule1.setStartTime("00:00");
//        schedule1.setEndTime("23:59");
//        schedule1.setLanguage("zh-CN");
        schedules.add(schedule);

        Item item = new Item();
        String scheduleIds = "";
        if(null != schedules && schedules.size() >0){
            List<String> idList = new ArrayList<>();
            for(Schedule schedule1 : schedules){
                String id = schedule1.getId();
                idList.add(id);
            }
            scheduleIds = String.join("," , idList);
        }
        item.setSchedulesIds(scheduleIds);
        item.set_id(UUID.randomUUID().toString().replace("-", ""));
        item.setPriority(0);
        item.setRepeatTimes(1);
        item.setVersion(0);
        item.setProgramId(program.get_id());
        itemMapper.insertSelective(item);
        List<Item> items = new ArrayList<>();
        items.add(item);
        imitateAddTask(items);
    }

    public void imitateAddTask(List<Item> items) {
        Task task = new Task();
        String itemsIds = "";
        if(null != items && items.size() >0){
            List<String> idList = new ArrayList<>();
            for(Item item : items){
                String id = item.get_id();
                idList.add(id);
            }
            itemsIds = String.join("," , idList);
        }
        task.setItemsIds(itemsIds);
        task.set_id(UUID.randomUUID().toString().replace("-", ""));
        task.setName("MyTask");
        task.set_department("dev");
        task.setCmdId(UUID.randomUUID().toString().replace("-", ""));
        taskMapper.insertSelective(task);
        imitateAddCommand(task);
    }

    public void imitateAddCommand(Task task) {
        Command command = new Command();
        command.set_type("PlayXixunTask");
        command.setId(UUID.randomUUID().toString().replace("-", ""));
        command.setPreDownloadURL(null);
        command.setTaskId(task.get_id());
        commandMapper.insertSelective(command);
    }

    public void imitateAddRequestData(Command command) {
        RequestData requestData = new RequestData();
        requestData.setType("commandXixunPlayer");
//        requestData.setCommand();
    }
}
