package org.linlinjava.litemall.db.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.linlinjava.litemall.db.dao.LayerMapper;
import org.linlinjava.litemall.db.dao.ProgramMapper;
import org.linlinjava.litemall.db.domain.*;
import org.linlinjava.litemall.db.service.LayerService;
import org.linlinjava.litemall.db.service.PlaySourceService;
import org.linlinjava.litemall.db.service.ProgramService;
import org.linlinjava.litemall.db.service.SourceService;
import org.linlinjava.litemall.db.util.DateUtil;
import org.linlinjava.litemall.db.util.ResponseUtil;
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

    @Override
    public void updatePlaySources(Program newProgram) {
        Program oldProgram = readProgram(newProgram.get_id());
        String[] sourceIdList = newProgram.getPlaySource();
        if (null != sourceIdList && sourceIdList.length >0) {
            if (StringUtilsXD.isEmpty(oldProgram.getLayersIds())) {
                //如果有资源，没有层,新建层
                Layer layer = new Layer();
                layer.setId(UUID.randomUUID().toString().replace("-", ""));
                layer.setIsRepeat(false);
                long cuttentTime = DateUtil.getDateline();
                layer.setCreateTime(cuttentTime);
                layer.setUpdateTime(cuttentTime);
                layerMapper.insertSelective(layer);
                oldProgram.setLayersIds(layer.getId());
                updateProgramById(oldProgram);
                oldProgram = readProgram(newProgram.get_id());
            }
            //查出有该资源的该项目所有层，不是该资源的层全部删掉
            List<PlaySource> existPlaySourceList = new ArrayList<>();
            Set<String> existLayerIdList = new HashSet<>();
            //查出有该资源的该项目所有层
            for (String sourceId : sourceIdList) {
                PlaySource existPlaySource = playSourceService.selectBySourceIdAndProgramId(sourceId, newProgram.get_id());
                if (null != existPlaySource) {
                    existPlaySourceList.add(existPlaySource);
                    String existLayerId = existPlaySource.getLayerId();
                    existLayerIdList.add(existLayerId);
                }
            }

            String layerIds = oldProgram.getLayersIds();
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

            String layerId = layerIdList.get(0);
            Layer layer = layerService.selectLayerById(layerId);
            layer.setSourcesIds(String.join(",", sourceIdList));
            layerService.updateLayerById(layer);

            //复制Source到PlaySource
            for (String sourceId : sourceIdList) {
                addPlaysource(newProgram.get_id(), sourceId, layerId);
            }
        } else {
            //如果没有资源,删掉该项目下所有层及所有资源
            oldProgram.setLayersIds("");
            updateProgramById(oldProgram);

            List<Layer> searchLayerList = new ArrayList<>();
            String layersIds = newProgram.getLayersIds();
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
            searchPlaySource.setProgramId(newProgram.get_id());
            List<PlaySource> searchPlaySourceList = playSourceService.selectPlaySourceList(searchPlaySource);
            if (null != searchPlaySourceList && searchPlaySourceList.size() > 0) {
                for (PlaySource playSource : searchPlaySourceList) {
                    playSourceService.deleteById(playSource.getId());
                }
            }
        }
    }

    protected void addPlaysource(String programId, String sourceId, String layerId) {
        Source source = sourceService.selectSourceById(sourceId);
        PlaySource playSource = new PlaySource();
        BeanUtils.copyProperties(source, playSource);
        PlaySource existPlaySource = playSourceService.selectBySourceIdAndLayerId(sourceId, layerId);
        if (null == existPlaySource) {
            playSource.setPlayTime(0);
            playSource.setTimeSpan(10);
            playSource.setLeft(0);
            playSource.setTop(0);
            playSource.setWidth(256);
            playSource.setHeight(128);
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

            playSource.setProgramId(programId);
            playSource.setSourceId(sourceId);
            playSource.setLayerId(layerId);
            playSource.setId(UUID.randomUUID().toString().replace("-", ""));
            playSourceService.insertPlaySource(playSource);
        } else {
            playSourceService.updatePlaySourceById(playSource);
        }
    }

    @Override
    public int deleteById(String id) {
        int n = 0;
        try {
            n = programMapper.deleteByPrimaryKey(id);
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
}
