package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.linlinjava.litemall.db.domain.Layer;
import org.linlinjava.litemall.db.service.LayerService;
import org.linlinjava.litemall.db.util.Constant;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.StringUtilsXD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/screen/layer")
public class LayerController {
    @Autowired
    private LayerService layerService;

    private static Logger logger = LoggerFactory.getLogger(LayerController.class);

    /**
     * @Description: 获取层列表
     * @title selectLayerPage
     * @param page 开始页数
     * @param limit 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "获取层列表")
    @GetMapping("/selectLayerPage")
    public Object selectLayerPage(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        Layer layer = null;
        logger.info("selectLayerPage and layer={},page={},limit", JSON.toJSONString(layer), page, limit);
        try {
            PageInfo<Layer> pageList = layerService.selectLayerPage(layer, StringUtilsXD.checkPageNumParam(page), StringUtilsXD.checkPageSizeParam(limit));
            return ResponseUtil.okPage(pageList);
        } catch (Exception e) {
            logger.error("selectLayerPage and layer={},page={},limit", JSON.toJSONString(layer), page, limit, e);
        }
        return ResponseUtil.fail();
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
    public ResponseUtil<Layer> selectLayerById(@RequestParam(value = "layerId") String layerId) {
        logger.info("selectLayerById and layerId={}", layerId);
        ResponseUtil<Layer> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(layerId)) {
            return responseUtil.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            responseUtil = layerService.selectLayerById(layerId);
        } catch (Exception e) {
            logger.error("selectLayerById and layerId={}", layerId, e);
        }
        return responseUtil;
    }

    /**
     * @Description: 添加层
     * @Title: insertLayer
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "添加层")
    @PostMapping(value = "/insertLayer")
    public ResponseUtil<Layer> insertLayer(@RequestBody Layer layer) {
        logger.info("insertLayer and layer:{}", JSON.toJSONString(layer));
        ResponseUtil<Layer> responseUtil = new ResponseUtil<>();
        try {
            responseUtil = layerService.insertLayer(layer);
        } catch (Exception e) {
            logger.error("insertLayer and layer:{}", JSON.toJSONString(layer), e);
        }
        return responseUtil;
    }

    /**
     * @Description: 编辑层
     * @title updateLayerById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "编辑层")
    @PostMapping(value = "/updateLayerById")
    public ResponseUtil<Layer> updateLayerById(@RequestBody Layer layer) {
        logger.info("updateLayerById and layer:{}", JSON.toJSONString(layer));
        ResponseUtil<Layer> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(layer.getId())) {
            return responseUtil.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            responseUtil = layerService.updateLayerById(layer);
        } catch (Exception e) {
            logger.error("updateLayerById and layer:{}", JSON.toJSONString(layer), e);

        }
        return responseUtil;
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
    public ResponseUtil<Layer> deleteByIdBatch(@RequestParam(value = "ids") String[] ids) {
        logger.info("deleteByIdBatch and ids={}", JSON.toJSONString(ids));
        ResponseUtil<Layer> responseUtil = new ResponseUtil<>();
        try {
            responseUtil = layerService.deleteByIdBatch(ids);
        } catch (Exception e) {
            logger.error("deleteByIdBatch and ids={}", JSON.toJSONString(ids), e);
        }
        return responseUtil;
    }
}
