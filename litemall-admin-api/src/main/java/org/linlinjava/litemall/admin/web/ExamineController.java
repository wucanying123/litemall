package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.linlinjava.litemall.db.domain.Examine;
import org.linlinjava.litemall.db.service.ExamineService;
import org.linlinjava.litemall.db.util.Constant;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.StringUtilsXD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/screen/examine")
public class ExamineController {
    @Autowired
    private ExamineService examineService;

    private static Logger logger = LoggerFactory.getLogger(ExamineController.class);

    /**
     * @Description: 获取审核列表
     * @title selectExaminePage
     * @param page 开始页数
     * @param limit 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "获取审核列表")
    @GetMapping("/selectExaminePage")
    public ResponseUtil selectExaminePage(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        ResponseUtil responseUtil = new ResponseUtil();
        Examine examine = null;
        logger.info("selectExaminePage and examine={},page={},limit", JSON.toJSONString(examine), page, limit);
        try {
            PageInfo<Examine> pageList = examineService.selectExaminePage(examine, StringUtilsXD.checkPageNumParam(page), StringUtilsXD.checkPageSizeParam(limit));
            return responseUtil.succeedPage(pageList);
        } catch (Exception e) {
            logger.error("selectExaminePage and examine={},page={},limit", JSON.toJSONString(examine), page, limit, e);
        }
        return responseUtil;
    }

    /**
     * @Description: 通过审核id查看审核详情
     * @param examineId 审核id
     * @Title: selectExamineById
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "通过审核id查看审核详情")
    @GetMapping(value = "/selectExamineById")
    public ResponseUtil<Examine> selectExamineById(@RequestParam(value = "examineId") String examineId) {
        logger.info("selectExamineById and examineId={}", examineId);
        ResponseUtil<Examine> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(examineId)) {
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            responseUtil = examineService.selectExamineById(examineId);
        } catch (Exception e) {
            logger.error("selectExamineById and examineId={}", examineId, e);
        }
        return responseUtil;
    }

    /**
     * @Description: 添加审核
     * @Title: insertExamine
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "添加审核")
    @PostMapping(value = "/insertExamine")
    public ResponseUtil<Examine> insertExamine(@RequestBody Examine examine) {
        logger.info("insertExamine and examine:{}", JSON.toJSONString(examine));
        ResponseUtil<Examine> responseUtil = new ResponseUtil<>();
        try {
            responseUtil = examineService.insertExamine(examine);
        } catch (Exception e) {
            logger.error("insertExamine and examine:{}", JSON.toJSONString(examine), e);
        }
        return responseUtil;
    }

    /**
     * @Description: 编辑审核
     * @title updateExamineById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "编辑审核")
    @PostMapping(value = "/updateExamineById")
    public ResponseUtil<Examine> updateExamineById(@RequestBody Examine examine) {
        logger.info("updateExamineById and examine:{}", JSON.toJSONString(examine));
        ResponseUtil<Examine> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(examine.getId())) {
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            responseUtil = examineService.updateExamineById(examine);
        } catch (Exception e) {
            logger.error("updateExamineById and examine:{}", JSON.toJSONString(examine), e);

        }
        return responseUtil;
    }

    /**
     * @Description: 删除审核
     * @Title: deleteById
     * @param id 审核id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "删除审核")
    @PostMapping(value = "/deleteById")
    public ResponseUtil<Examine> deleteById(@RequestParam(value = "id") String id) {
        logger.info("deleteById and id={}", JSON.toJSONString(id));
        ResponseUtil<Examine> responseUtil = new ResponseUtil<>();
        try {
            responseUtil = examineService.deleteById(id);
        } catch (Exception e) {
            logger.error("deleteById and id={}", JSON.toJSONString(id), e);
        }
        return responseUtil;
    }
}
