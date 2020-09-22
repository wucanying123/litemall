package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.linlinjava.litemall.db.domain.Layer;
import org.linlinjava.litemall.db.domain.LitemallAdmin;
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
     * @param page  开始页数
     * @param limit 每页条数
     * @Description: 获取层列表
     * @title selectLayerPage
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "获取层列表")
    @GetMapping("/selectLayerPage")
    public ResponseUtil selectLayerPage(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        ResponseUtil responseUtil = new ResponseUtil();
        Layer layer = null;
        logger.info("selectLayerPage and layer={},page={},limit={}", JSON.toJSONString(layer), page, limit);
        try {
            PageInfo<Layer> pageList = layerService.selectLayerPage(layer, StringUtilsXD.checkPageNumParam(page), StringUtilsXD.checkPageSizeParam(limit));
            return responseUtil.succeedPage(pageList);
        } catch (Exception e) {
            logger.error("selectLayerPage and layer={},page={},limit={}", JSON.toJSONString(layer), page, limit, e);
        }
        return responseUtil;
    }

    /**
     * @param layerId 层id
     * @Description: 通过层id查看层详情
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
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            Layer layer = layerService.selectLayerById(layerId);
            responseUtil.setData(layer);
            responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
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
            Subject currentUser = SecurityUtils.getSubject();
            LitemallAdmin admin = (LitemallAdmin) currentUser.getPrincipal();
            layer.setUserid(admin.getId());
            int n = layerService.insertLayer(layer);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
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
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            int n = layerService.updateLayerById(layer);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateLayerById and layer:{}", JSON.toJSONString(layer), e);

        }
        return responseUtil;
    }

    /**
     * @param id 层id
     * @Description: 删除层
     * @Title: deleteById
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "删除层")
    @PostMapping(value = "/deleteById")
    public ResponseUtil<Layer> deleteById(@RequestParam(value = "id") String id) {
        logger.info("deleteById and id={}", JSON.toJSONString(id));
        ResponseUtil<Layer> responseUtil = new ResponseUtil<>();
        try {
            int n = layerService.deleteById(id);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteById and id={}", JSON.toJSONString(id), e);
        }
        return responseUtil;
    }
}
