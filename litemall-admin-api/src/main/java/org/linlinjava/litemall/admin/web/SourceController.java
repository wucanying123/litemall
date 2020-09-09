package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.core.util.StringUtilsXD;
import org.linlinjava.litemall.db.domain.Source;
import org.linlinjava.litemall.db.service.SourceService;
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
     * @param page  开始页数
     * @param limit 每页条数
     * @Description: 获取资源列表
     * @title selectSourcePage
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "获取资源列表")
    @GetMapping("/selectSourcePage")
    public Object selectSourcePage(String name, String _type, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        Source source = new Source();
        source.setName(name);
        source.set_type(_type);
        logger.info("selectSourcePage and source={},page={},limit", JSON.toJSONString(source), page, limit);
        PageInfo<Source> pageList = null;
        try {
            pageList = sourceService.selectSourcePage(source, StringUtilsXD.checkPageNumParam(page), StringUtilsXD.checkPageSizeParam(limit));
            return ResponseUtil.okPage(pageList);
        } catch (Exception e) {
            logger.error("selectSourcePage and source={},page={},limit", JSON.toJSONString(source), page, limit, e);
        }
        return ResponseUtil.fail();
    }

    /**
     * @param sourceId 资源id
     * @Description: 通过资源id查看资源详情
     * @Title: selectSourceById
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "通过资源id查看资源详情")
    @GetMapping(value = "/selectSourceById")
    public Object selectSourceById(@RequestParam(value = "sourceId") String sourceId) {
        logger.info("selectSourceById and sourceId={}", sourceId);
        try {
            Source source = sourceService.selectSourceById(sourceId);
            return ResponseUtil.ok(source);
        } catch (Exception e) {
            logger.error("selectSourceById and sourceId={}", sourceId, e);
        }
        return ResponseUtil.fail();
    }

    /**
     * @Description: 添加资源
     * @Title: insertSource
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "添加资源")
    @PostMapping(value = "/insertSource")
    public Object insertSource(@RequestBody Source source) {
        logger.info("insertSource and source:{}", JSON.toJSONString(source));
        try {
            int n = sourceService.insertSource(source);
            if (n == 1) {
                return ResponseUtil.ok();
            }
        } catch (Exception e) {
            logger.error("insertSource and source:{}", JSON.toJSONString(source), e);
        }
        return ResponseUtil.fail();
    }

    /**
     * @Description: 编辑资源
     * @title updateSourceById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "编辑资源")
    @PostMapping(value = "/updateSourceById")
    public Object updateSourceById(@RequestBody Source source) {
        logger.info("updateSourceById and source:{}", JSON.toJSONString(source));
        if (StringUtilsXD.isBlank(source.getId())) {
            return ResponseUtil.badArgument();
        }
        try {
            int n = sourceService.updateSourceById(source);
            if (n == 1) {
                return ResponseUtil.ok();
            }
        } catch (Exception e) {
            logger.error("updateSourceById and source:{}", JSON.toJSONString(source), e);

        }
        return ResponseUtil.fail();
    }

    /**
     * @param id 资源id
     * @Description: 删除资源
     * @Title: deleteById
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "删除资源")
    @PostMapping(value = "/deleteById")
    public Object deleteById(@RequestParam(value = "id") String id) {
        logger.info("deleteById and id={}", id);
        try {
            int n = sourceService.deleteById(id);
            if (n == 1) {
                return ResponseUtil.ok();
            }
        } catch (Exception e) {
            logger.error("deleteById and id={}", id, e);
        }
        return ResponseUtil.fail();
    }
}
