package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.linlinjava.litemall.db.domain.Source;
import org.linlinjava.litemall.db.service.SourceService;
import org.linlinjava.litemall.db.util.Constant;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.StringUtilsXD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/screen/source")
public class SourceController {
    @Autowired
    private SourceService sourceService;

    private static Logger logger = LoggerFactory.getLogger(SourceController.class);

    /**
     * @Description: 获取资源列表
     * @title selectSourcePage
     * @param page 开始页数
     * @param limit 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "获取资源列表")
    @GetMapping("/selectSourcePage")
    public Object selectSourcePage(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        Source source = null;
        logger.info("selectSourcePage and source={},page={},limit", JSON.toJSONString(source), page, limit);
        try {
            PageInfo<Source> pageList = sourceService.selectSourcePage(source, StringUtilsXD.checkPageNumParam(page), StringUtilsXD.checkPageSizeParam(limit));
            return ResponseUtil.okPage(pageList);
        } catch (Exception e) {
            logger.error("selectSourcePage and source={},page={},limit", JSON.toJSONString(source), page, limit, e);
        }
        return ResponseUtil.fail();
    }

    /**
     * @Description: 通过资源id查看资源详情
     * @param sourceId 资源id
     * @Title: selectSourceById
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "通过资源id查看资源详情")
    @GetMapping(value = "/selectSourceById")
    public ResponseUtil<Source> selectSourceById(@RequestParam(value = "sourceId") String sourceId) {
        logger.info("selectSourceById and sourceId={}", sourceId);
        ResponseUtil<Source> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(sourceId)) {
            return responseUtil.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            responseUtil = sourceService.selectSourceById(sourceId);
        } catch (Exception e) {
            logger.error("selectSourceById and sourceId={}", sourceId, e);
        }
        return responseUtil;
    }

    /**
     * @Description: 添加资源
     * @Title: insertSource
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "添加资源")
    @PostMapping(value = "/insertSource")
    public ResponseUtil<Source> insertSource(@RequestBody Source source) {
        logger.info("insertSource and source:{}", JSON.toJSONString(source));
        ResponseUtil<Source> responseUtil = new ResponseUtil<>();
        try {
            responseUtil = sourceService.insertSource(source);
        } catch (Exception e) {
            logger.error("insertSource and source:{}", JSON.toJSONString(source), e);
        }
        return responseUtil;
    }

    /**
     * @Description: 编辑资源
     * @title updateSourceById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "编辑资源")
    @PostMapping(value = "/updateSourceById")
    public ResponseUtil<Source> updateSourceById(@RequestBody Source source) {
        logger.info("updateSourceById and source:{}", JSON.toJSONString(source));
        ResponseUtil<Source> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(source.getId())) {
            return responseUtil.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            responseUtil = sourceService.updateSourceById(source);
        } catch (Exception e) {
            logger.error("updateSourceById and source:{}", JSON.toJSONString(source), e);

        }
        return responseUtil;
    }

    /**
     * @Description: 删除资源
     * @Title: deleteById
     * @param id 资源id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "删除资源")
    @PostMapping(value = "/deleteById")
    public ResponseUtil<Source> deleteById(@RequestParam(value = "id") String id) {
        logger.info("deleteById and id={}", JSON.toJSONString(id));
        ResponseUtil<Source> responseUtil = new ResponseUtil<>();
        try {
            responseUtil = sourceService.deleteById(id);
        } catch (Exception e) {
            logger.error("deleteById and id={}", JSON.toJSONString(id), e);
        }
        return responseUtil;
    }
}
