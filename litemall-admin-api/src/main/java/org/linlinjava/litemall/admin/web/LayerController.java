package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.xinda.common.BaseResp;
import com.xinda.common.Constant;
import com.xinda.screen.model.entity.Layer;
import com.xinda.screen.service.LayerService;
import com.xinda.util.StringUtilsXD;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/screen/layer")
public class LayerController {
    @Autowired
    private LayerService layerService;

    private static Logger logger = LoggerFactory.getLogger(LayerController.class);

    /**
     * @Description: 获取层列表
     * @title selectLayerPage
     * @param pageNum 开始页数
     * @param pageSize 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "获取层列表")
    @GetMapping("/selectLayerPage")
    public BaseResp<PageInfo<Layer>> selectLayerPage(@RequestBody Layer layer, @RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        logger.info("selectLayerPage and layer={}, pageNum={},pageSize",JSON.toJSONString(layer), pageNum, pageSize);
        BaseResp<PageInfo<Layer>> baseResp = new BaseResp<>();
        try {
            PageInfo<Layer> page = layerService.selectLayerPage(layer, StringUtilsXD.checkPageNumParam(pageNum), StringUtilsXD.checkPageSizeParam(pageSize));
            baseResp.setData(page);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectLayerPage and layer={},pageNum={},pageSize", JSON.toJSONString(layer), pageNum, pageSize, e);
        }
        return baseResp;
    }

    /**
     * @Description: 通过层id查看层详情
     * @param layerId 层id
     * @Title: selectLayerById
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "通过层id查看层详情")
    @GetMapping(value = "/selectLayerById")
    public BaseResp<Layer> selectLayerById(@RequestParam(value = "layerId") String layerId) {
        logger.info("selectLayerById and layerId={}", layerId);
        BaseResp<Layer> baseResp = new BaseResp<>();
        if (StringUtilsXD.isBlank(layerId)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            baseResp = layerService.selectLayerById(layerId);
        } catch (Exception e) {
            logger.error("selectLayerById and layerId={}", layerId, e);
        }
        return baseResp;
    }

    /**
     * @Description: 添加层
     * @Title: insertLayer
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "添加层")
    @PostMapping(value = "/insertLayer")
    public BaseResp<Layer> insertLayer(@RequestBody Layer layer) {
        logger.info("insertLayer and layer:{}", JSON.toJSONString(layer));
        BaseResp<Layer> baseResp = new BaseResp<>();
        try {
            baseResp = layerService.insertLayer(layer);
        } catch (Exception e) {
            logger.error("insertLayer and layer:{}", JSON.toJSONString(layer), e);
        }
        return baseResp;
    }

    /**
     * @Description: 编辑层
     * @title updateLayerById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "编辑层")
    @PostMapping(value = "/updateLayerById")
    public BaseResp<Layer> updateLayerById(@RequestBody Layer layer) {
        logger.info("updateLayerById and layer:{}", JSON.toJSONString(layer));
        BaseResp<Layer> baseResp = new BaseResp<>();
        if (StringUtilsXD.isBlank(layer.getId())) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            baseResp = layerService.updateLayerById(layer);
        } catch (Exception e) {
            logger.error("updateLayerById and layer:{}", JSON.toJSONString(layer), e);

        }
        return baseResp;
    }

    /**
     * @Description: 删除层
     * @Title: deleteByIdBatch
     * @param ids 层id集合
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "删除层")
    @PostMapping(value = "/deleteByIdBatch")
    public BaseResp<Layer> deleteByIdBatch(@RequestParam(value = "ids") String[] ids) {
        logger.info("deleteByIdBatch and ids={}", JSON.toJSONString(ids));
        BaseResp<Layer> baseResp = new BaseResp<>();
        try {
            baseResp = layerService.deleteByIdBatch(ids);
        } catch (Exception e) {
            logger.error("deleteByIdBatch and ids={}", JSON.toJSONString(ids), e);
        }
        return baseResp;
    }
}
