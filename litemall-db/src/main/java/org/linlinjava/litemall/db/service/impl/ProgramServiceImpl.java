package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.dao.LayerMapper;
import org.linlinjava.litemall.db.dao.PlaySourceMapper;
import org.linlinjava.litemall.db.dao.ProgramMapper;
import org.linlinjava.litemall.db.domain.*;
import org.linlinjava.litemall.db.service.LayerService;
import org.linlinjava.litemall.db.service.PlaySourceService;
import org.linlinjava.litemall.db.service.ProgramService;
import org.linlinjava.litemall.db.service.SourceService;
import org.linlinjava.litemall.db.util.DateUtil;
import org.linlinjava.litemall.db.util.StringUtilsXD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProgramServiceImpl implements ProgramService {

    @Autowired
    private ProgramMapper programMapper;
    @Autowired
    private LayerService layerService;
    @Autowired
    private SourceService sourceService;
    @Autowired
    private PlaySourceService playSourceService;
    @Autowired
    private LayerMapper layerMapper;
    @Autowired
    private PlaySourceMapper playSourceMapper;


    private static Logger logger = LoggerFactory.getLogger(ProgramServiceImpl.class);

    @Override
    public PageInfo<Program> selectProgramPage(Program program, Integer pageNum, Integer pageSize) {
        PageInfo<Program> page = null;
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Program> list = programMapper.selectProgramPage(program);
            String jsonString = JSON.toJSONString(list);
            page = new PageInfo<>(list);
        } catch (Exception e) {
            logger.error("selectProgramPage error and msg={}", e);
        }
        return page;
    }

    @Override
    public Program selectProgramById(String programId) {
        Program program = null;
        try {
            program = programMapper.selectByPrimaryKey(programId);
        } catch (Exception e) {
            logger.error("selectProgramById error and msg={}", e);
        }
        return program;
    }

    @Override
    public int insertProgram(Program program) {
        int n = 0;
        program.set_id(UUID.randomUUID().toString().replace("-", ""));
        try {
            long cuttentTime = DateUtil.getDateline();
            program.setCreateTime(cuttentTime);
            program.setUpdateTime(cuttentTime);
            n = programMapper.insertSelective(program);
        } catch (Exception e) {
            logger.error("insertProgram error and msg={}", e);
        }
        return n;
    }

    @Override
    public int updateProgramById(Program program) {
        int n = 0;
        try {
            program.setUpdateTime(DateUtil.getDateline());
            n = programMapper.updateByPrimaryKeySelective(program);
        } catch (Exception e) {
            logger.error("updateProgramById error and msg={}", e);
        }
        return n;
    }

    private void addPlaysource(PlaySource playSource, Program newProgram, String layerId) {
        playSource.setPlayTime(playSource.getPlayTime());
        playSource.setTimeSpan(playSource.getTimeSpan());
        playSource.setLeft(0);
        playSource.setTop(0);
        playSource.setWidth(newProgram.getWidth());
        playSource.setHeight(newProgram.getHeight());
        playSource.setEntryEffect("None");
        playSource.setExitEffect("None");
        playSource.setEntryEffectTimeSpan(0);
        playSource.setExitEffectTimeSpan(0);
        playSource.setShowBg(false);
        playSource.setShowHourScale(false);
        playSource.setShowMinScale(false);
        playSource.setShowScaleNum(false);
        playSource.setShowSecond(false);
        playSource.setCenter(false);

        playSource.setProgramId(newProgram.get_id());
        playSource.setLayerId(layerId);
        playSourceService.insertPlaySource(playSource);
    }

    @Override
    public int deleteById(String id) {
        int n = 0;
        try {
            n = programMapper.deleteByPrimaryKey(id);
            Program program = selectProgramById(id);
            deleteLayerAndSource(program);
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return n;
    }

    @Override
    public Program readProgram(String programId) {
        Program program = selectProgramById(programId);
        if (null == program || null == program.getLayersIds()) {
            return program;
        }
        String layerIds = program.getLayersIds();
        List<Layer> layerList = new ArrayList<>();
        if (StringUtilsXD.isNotEmpty(layerIds)) {
            String[] layersIdArray = layerIds.split(",");
            for (String layerId : layersIdArray) {
                Layer layer = layerService.selectLayerById(layerId);
                if (null != layer) {
                    String sourceIds = layer.getSourcesIds();
                    if (StringUtilsXD.isNotEmpty(sourceIds)) {
                        List<PlaySource> playSourceList = new ArrayList<>();
                        String[] sourceIdArray = sourceIds.split(",");
                        for (String sourceId : sourceIdArray) {
                            PlaySource playSource = playSourceService.selectBySourceIdAndLayerId(sourceId, layerId);
                            if (null != playSource) {
                                playSource.setLeft(playSource.getTheLeft());
                                playSourceList.add(playSource);
                            }
                        }
                        layer.setSources(playSourceList);
                    }
                    layerList.add(layer);
                }
            }
            program.setLayers(layerList);
        }
        return program;
    }

    @Override
    public void updateComplexProgramById(Program newProgram,int type) {
        Program oldProgram = readProgram(newProgram.get_id());
        List<Layer> newLayers = newProgram.getLayers();
        Set<String> existLayerIdList = new HashSet<>();
        if (null != newLayers && newLayers.size() > 0) {
            for (Layer newlayer : newLayers) {
                if (StringUtilsXD.isNotEmpty(newlayer.getId())) {
                    existLayerIdList.add(newlayer.getId());
                }
            }
        }
        String layerIds = oldProgram.getLayersIds();
        if (StringUtilsXD.isNotEmpty(layerIds)) {
            List<String> layerIdList = Arrays.asList(layerIds.split(","));
            Set<String> layerIdSet = new HashSet(layerIdList);
            Set<String> subLayerIdSet = new HashSet<String>();
            subLayerIdSet.addAll(layerIdSet);
            subLayerIdSet.removeAll(existLayerIdList);
            if (null == subLayerIdSet && subLayerIdSet.size() > 0) {
                String newProgromLayerIds = String.join(",", subLayerIdSet);
                oldProgram.setLayersIds(newProgromLayerIds);
                updateProgramById(oldProgram);
                for (String notExistLayerId : subLayerIdSet) {
                    //不在的层，删了该层及层下所有资源
                    layerService.deleteById(notExistLayerId);
                    PlaySource searchPlaySource = new PlaySource();
                    searchPlaySource.setLayerId(notExistLayerId);
                    List<PlaySource> searchPlaySourceList = playSourceService.selectPlaySourceList(searchPlaySource);
                    if (null != searchPlaySourceList && searchPlaySourceList.size() > 0) {
                        for (PlaySource playSource : searchPlaySourceList) {
                            playSourceService.deleteById(playSource.getId());
                        }
                    }
                }
            }
        }

        if (newLayers != null && newLayers.size() > 0 && newLayers.get(0).getSources() !=null && newLayers.get(0).getSources().size() >0) {
            List<String> updateLayerIds = new ArrayList<>();
            for (Layer layer : newLayers) {
                List<PlaySource> sources = layer.getSources();
                List<String> sourceIdList = new ArrayList<>();
                if (null != sources && sources.size() > 0) {
                    if (StringUtilsXD.isEmpty(layer.getId())) {
                        //如果有资源，没有层,新建层
                        layer.setId(UUID.randomUUID().toString().replace("-", ""));
                        layer.setIsRepeat(false);
                        long cuttentTime = DateUtil.getDateline();
                        layer.setCreateTime(cuttentTime);
                        layer.setUpdateTime(cuttentTime);
                        layerMapper.insertSelective(layer);
                    }

                    Integer allPlayTime = 0;
                    for (PlaySource playSource : sources) {
                        if(null != playSource) {
                            //自动设置开始播放时间
                            if (null == playSource.getCreateTime()) {
                                if(StringUtilsXD.isEmpty(playSource.getId())) {
                                    playSource.setPlayTime(allPlayTime);
                                    //新增，复制Source到PlaySource
                                    //初始 持续时长等于素材时长，起始时间等于之前持续时长之和
                                    Integer timeSpan = 0;
                                    if (null != playSource.getTimeSpan()) {
                                        timeSpan = playSource.getTimeSpan();
                                    } else {
                                        timeSpan = playSource.getMaxPlayTime();//持续时长等于素材时长
                                    }
                                    String sourceId = playSource.getSourceId();
                                    Source source = sourceService.selectSourceById(sourceId);
                                    BeanUtils.copyProperties(source, playSource);
                                    playSource.setTimeSpan(timeSpan);
                                    playSource.setPlayTime(allPlayTime);
                                    allPlayTime += timeSpan;
                                    addPlaysource(playSource, newProgram, layer.getId());
                                }else {
                                    playSource.setProgramId(newProgram.get_id());
                                    playSource.setLayerId(layer.getId());
                                    long cuttentTime = DateUtil.getDateline();
                                    playSource.setCreateTime(cuttentTime);
                                    playSource.setUpdateTime(cuttentTime);
                                    playSource.setTheLeft(playSource.getLeft());
                                    playSourceMapper.insertSelective(playSource);
                                }
                            } else {
                                if(type == 0) {
                                    playSource.setPlayTime(allPlayTime);
                                    allPlayTime += playSource.getTimeSpan();
                                }
                                //修改
                                playSourceService.updatePlaySourceById(playSource);
                            }
                            sourceIdList.add(playSource.getSourceId());
                        }
                    }
                }
                layer.setSourcesIds(String.join(",", sourceIdList));
                layerService.updateLayerById(layer);
                updateLayerIds.add(layer.getId());
            }
            String newProgromLayerIds = String.join(",", updateLayerIds);
            oldProgram.setLayersIds(newProgromLayerIds);
            updateProgramById(oldProgram);
        } else {
            //如果没有资源,删掉该项目下所有层及所有资源
            oldProgram.setLayersIds("");
            updateProgramById(oldProgram);
            deleteLayerAndSource(newProgram);
        }
    }

    //删掉该项目下所有层及所有资源
    private void deleteLayerAndSource(Program program) {
        List<Layer> searchLayerList = new ArrayList<>();
        String layersIds = program.getLayersIds();
        if (null != layersIds && layersIds.length() > 0) {
            List<String> layersIdsList = Arrays.asList(layersIds.split(","));
            for (String layerId : layersIdsList) {
                Layer layer = layerService.selectLayerById(layerId);
                if (null != layer) {
                    searchLayerList.add(layer);
                }
            }
        }
        if (null != searchLayerList && searchLayerList.size() > 0) {
            for (Layer layer : searchLayerList) {
                layerService.deleteById(layer.getId());
            }
        }
        PlaySource searchPlaySource = new PlaySource();
        searchPlaySource.setProgramId(program.get_id());
        List<PlaySource> searchPlaySourceList = playSourceService.selectPlaySourceList(searchPlaySource);
        if (null != searchPlaySourceList && searchPlaySourceList.size() > 0) {
            for (PlaySource playSource : searchPlaySourceList) {
                playSourceService.deleteById(playSource.getId());
            }
        }
    }

    @Override
    public Integer getMaxLayerPlaytime(Program program){
        if (null == program || null == program.getLayersIds()) {
            return 0;
        }
        String layerIds = program.getLayersIds();
        List<Layer> layerList = new ArrayList<>();
        List<Integer> layerTotalTimeList = new ArrayList<>();
        Integer maxLayerPlaytime = 0;
        if (StringUtilsXD.isNotEmpty(layerIds)) {
            String[] layersIdArray = layerIds.split(",");
            for (String layerId : layersIdArray) {
                Layer layer = layerService.selectLayerById(layerId);
                Integer layerTotalTime = 0;
                if (null != layer) {
                    String sourceIds = layer.getSourcesIds();
                    if (StringUtilsXD.isNotEmpty(sourceIds)) {
                        List<PlaySource> playSourceList = new ArrayList<>();
                        String[] sourceIdArray = sourceIds.split(",");
                        for (String sourceId : sourceIdArray) {
                            PlaySource playSource = playSourceService.selectBySourceIdAndLayerId(sourceId, layerId);
                            if (null != playSource) {
                                playSource.setLeft(playSource.getTheLeft());
                                playSourceList.add(playSource);
                                if(null != playSource.getTimeSpan()){
                                    layerTotalTime += playSource.getTimeSpan();
                                }
                            }
                        }
                        layer.setSources(playSourceList);
                    }
                    layerList.add(layer);
                    layerTotalTimeList.add(layerTotalTime);
                }
            }
            program.setLayers(layerList);
            maxLayerPlaytime = Collections.max(layerTotalTimeList);
        }
        return maxLayerPlaytime;
    }
}
