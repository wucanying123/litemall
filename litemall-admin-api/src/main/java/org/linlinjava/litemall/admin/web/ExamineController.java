package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.linlinjava.litemall.db.domain.Examine;
import org.linlinjava.litemall.db.domain.LitemallAdmin;
import org.linlinjava.litemall.db.service.ExamineService;
import org.linlinjava.litemall.db.util.Constant;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.StringUtilsXD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/screen/examine")
public class ExamineController {
    @Autowired
    private ExamineService examineService;

    private static Logger logger = LoggerFactory.getLogger(ExamineController.class);

    /**
     * @param page  开始页数
     * @param limit 每页条数
     * @Description: 获取审核列表
     * @title selectExaminePage
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "获取审核列表")
    @GetMapping("/selectExaminePage")
    public ResponseUtil selectExaminePage(String detailName, Integer type, Integer passStatus, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        ResponseUtil responseUtil = new ResponseUtil();
        Examine examine = new Examine();
        examine.setDetailName(detailName);
        examine.setType(type);
        examine.setPassStatus(passStatus);
        logger.info("selectExaminePage and examine={},page={},limit={}", JSON.toJSONString(examine), page, limit);
        try {
            PageInfo<Examine> pageList = examineService.selectExaminePage(examine, StringUtilsXD.checkPageNumParam(page), StringUtilsXD.checkPageSizeParam(limit));
            return responseUtil.succeedPage(pageList);
        } catch (Exception e) {
            logger.error("selectExaminePage and examine={},page={},limit={}", JSON.toJSONString(examine), page, limit, e);
        }
        return responseUtil;
    }

    /**
     * @param examineId 审核id
     * @Description: 通过审核id查看审核详情
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
            Examine examine = examineService.selectExamineById(examineId);
            responseUtil.setData(examine);
            responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
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
            int n = examineService.insertExamine(examine);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
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
            if(examine.getPassStatus1() != null){
                examine.setPassStatus(examine.getPassStatus1());
            }
            if(examine.getPassStatus2() != null){
                examine.setPassStatus(examine.getPassStatus2());
            }
            Subject currentUser = SecurityUtils.getSubject();
            LitemallAdmin admin = (LitemallAdmin) currentUser.getPrincipal();
            if (2 == examine.getPassStatus() || 3 == examine.getPassStatus()) {
                examine.setCheckUserid1(admin.getId());
            } else if (4 == examine.getPassStatus() || 5 == examine.getPassStatus()) {
                examine.setCheckUserid2(admin.getId());
            }
            int n = examineService.updateExamineById(examine);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateExamineById and examine:{}", JSON.toJSONString(examine), e);

        }
        return responseUtil;
    }

    /**
     * @param id 审核id
     * @Description: 删除审核
     * @Title: deleteById
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "删除审核")
    @PostMapping(value = "/deleteById")
    public ResponseUtil<Examine> deleteById(@RequestParam(value = "id") String id) {
        logger.info("deleteById and id={}", JSON.toJSONString(id));
        ResponseUtil<Examine> responseUtil = new ResponseUtil<>();
        try {
            int n = examineService.deleteById(id);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteById and id={}", JSON.toJSONString(id), e);
        }
        return responseUtil;
    }
}
