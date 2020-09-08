package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.admin.vo.CategoryVo;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.core.util.StringUtilsXD;
import org.linlinjava.litemall.db.dao.SourceMapper;
import org.linlinjava.litemall.db.domain.LitemallCategory;
import org.linlinjava.litemall.db.domain.Source;
import org.linlinjava.litemall.db.service.LitemallCategoryService;
import org.linlinjava.litemall.db.service.SourceService;
import org.linlinjava.litemall.db.util.BaseResp;
import org.linlinjava.litemall.db.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/admin/screen/source")
public class SourceController {
    @Autowired
    private SourceService sourceService;
    @Autowired
    private SourceMapper sourceMapper;
    @Autowired
    LitemallCategoryService categoryService;

    private static Logger logger = LoggerFactory.getLogger(SourceController.class);

    /**
     * @param pageNum  开始页数
     * @param pageSize 每页条数
     * @Description: 获取资源列表
     * @title selectSourcePage
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "获取资源列表")
    @GetMapping("/selectSourcePage")
    public Object selectSourcePage(@RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        logger.info("selectSourcePage and pageNum={},pageSize", pageNum, pageSize);
        Source source = null;
        PageInfo<Source> page = null;
        try {
            page = sourceService.selectSourcePage(source, StringUtilsXD.checkPageNumParam(pageNum), StringUtilsXD.checkPageSizeParam(pageSize));
        } catch (Exception e) {
            logger.error("selectSourcePage and source={},pageNum={},pageSize", JSON.toJSONString(source), pageNum, pageSize, e);
        }
        return ResponseUtil.okPage(page);
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
    public BaseResp<Source> selectSourceById(@RequestParam(value = "sourceId") String sourceId) {
        logger.info("selectSourceById and sourceId={}", sourceId);
        BaseResp<Source> baseResp = new BaseResp<>();
        if (StringUtilsXD.isBlank(sourceId)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            baseResp = sourceService.selectSourceById(sourceId);
        } catch (Exception e) {
            logger.error("selectSourceById and sourceId={}", sourceId, e);
        }
        return baseResp;
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
