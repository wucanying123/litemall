package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.linlinjava.litemall.db.domain.LitemallAdmin;
import org.linlinjava.litemall.db.domain.Source;
import org.linlinjava.litemall.db.service.SourceService;
import org.linlinjava.litemall.db.util.Constant;
import org.linlinjava.litemall.db.util.DateUtil;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.StringUtilsXD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    public ResponseUtil selectSourcePage(String name, String _type, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        ResponseUtil responseUtil = new ResponseUtil();
        Source source = new Source();
        source.setName(name);
        source.set_type(_type);
        logger.info("selectSourcePage and source={},page={},limit={}", JSON.toJSONString(source), page, limit);
        try {
            PageInfo<Source> pageList = sourceService.selectSourcePage(source, StringUtilsXD.checkPageNumParam(page), StringUtilsXD.checkPageSizeParam(limit));
            return responseUtil.succeedPage(pageList);
        } catch (Exception e) {
            logger.error("selectSourcePage and source={},page={},limit={}", JSON.toJSONString(source), page, limit, e);
        }
        return responseUtil;
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
    public ResponseUtil<Source> selectSourceById(@RequestParam(value = "sourceId") String sourceId) {
        logger.info("selectSourceById and sourceId={}", sourceId);
        ResponseUtil<Source> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(sourceId)) {
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            Source source = sourceService.selectSourceById(sourceId);
            responseUtil.setData(source);
            responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
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
            Subject currentUser = SecurityUtils.getSubject();
            LitemallAdmin admin = (LitemallAdmin) currentUser.getPrincipal();
            source.setUserid(admin.getId());
            int n = sourceService.insertSource(source);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
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
        if (StringUtilsXD.isBlank(source.getSourceId())) {
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            int n = sourceService.updateSourceById(source);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateSourceById and source:{}", JSON.toJSONString(source), e);

        }
        return responseUtil;
    }

    /**
     * @param sourceId 资源id
     * @Description: 删除资源
     * @Title: deleteById
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "删除资源")
    @PostMapping(value = "/deleteById")
    public ResponseUtil<Source> deleteById(@RequestParam(value = "sourceId") String sourceId) {
        logger.info("deleteById and sourceId={}", JSON.toJSONString(sourceId));
        ResponseUtil<Source> responseUtil = new ResponseUtil<>();
        try {
            int n = sourceService.deleteById(sourceId);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteById and sourceId={}", JSON.toJSONString(sourceId), e);
        }
        return responseUtil;
    }


    @ApiOperation(value = "获取默认构造多行文本")
    @GetMapping("/selectDefaultMultiText")
    public ResponseUtil selectDefaultMultiText() {
        ResponseUtil responseUtil = new ResponseUtil();
        Source source = new Source();
        source.setSourceId(UUID.randomUUID().toString().replace("-", ""));
        source.setUuid(UUID.randomUUID().toString().replace("-", ""));
        source.set_type("MultiText");
        source.setName("多行文本");
        source.setHtml("内容");
        source.setMaxPlayTime(10);
        responseUtil.setData(source);
        return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
    }

    @ApiOperation(value = "获取默认网址")
    @GetMapping("/selectDefaultWebURL")
    public ResponseUtil selectDefaultWebUrl() {
        ResponseUtil responseUtil = new ResponseUtil();
        Source source = new Source();
        source.setSourceId(UUID.randomUUID().toString().replace("-", ""));
        source.setUuid(UUID.randomUUID().toString().replace("-", ""));
        source.set_type("WebURL");
        source.setName("网址");
        source.setMaxPlayTime(10);
        responseUtil.setData(source);
        return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
    }
}
