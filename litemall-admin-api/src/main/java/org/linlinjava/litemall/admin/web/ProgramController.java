package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.domain.Program;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.Constant;
import org.linlinjava.litemall.db.service.ProgramService;
import io.swagger.annotations.ApiOperation;
import org.linlinjava.litemall.db.util.StringUtilsXD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/screen/program")
public class ProgramController {
    @Autowired
    private ProgramService programService;

    private static Logger logger = LoggerFactory.getLogger(ProgramController.class);

    /**
     * @Description: 获取节目列表
     * @title selectProgramPage
     * @param page 开始页数
     * @param limit 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "获取节目列表")
    @GetMapping("/selectProgramPage")
    public Object selectProgramPage(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        Program program = null;
        logger.info("selectProgramPage and program={},page={},limit", JSON.toJSONString(program), page, limit);
        try {
            PageInfo<Program> pageList = programService.selectProgramPage(program, StringUtilsXD.checkPageNumParam(page), StringUtilsXD.checkPageSizeParam(limit));
            return ResponseUtil.okPage(pageList);
        } catch (Exception e) {
            logger.error("selectProgramPage and program={},page={},limit", JSON.toJSONString(program), page, limit, e);
        }
        return ResponseUtil.fail();
    }

    /**
     * @Description: 通过节目id查看节目详情
     * @param programId 节目id
     * @Title: selectProgramById
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "通过节目id查看节目详情")
    @GetMapping(value = "/selectProgramById")
    public ResponseUtil<Program> selectProgramById(@RequestParam(value = "programId") String programId) {
        logger.info("selectProgramById and programId={}", programId);
        ResponseUtil<Program> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(programId)) {
            return responseUtil.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            responseUtil = programService.selectProgramById(programId);
        } catch (Exception e) {
            logger.error("selectProgramById and programId={}", programId, e);
        }
        return responseUtil;
    }

    /**
     * @Description: 添加节目
     * @Title: insertProgram
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "添加节目")
    @PostMapping(value = "/insertProgram")
    public ResponseUtil<Program> insertProgram(@RequestBody Program program) {
        logger.info("insertProgram and program:{}", JSON.toJSONString(program));
        ResponseUtil<Program> responseUtil = new ResponseUtil<>();
        try {
            responseUtil = programService.insertProgram(program);
        } catch (Exception e) {
            logger.error("insertProgram and program:{}", JSON.toJSONString(program), e);
        }
        return responseUtil;
    }

    /**
     * @Description: 编辑节目
     * @title updateProgramById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "编辑节目")
    @PostMapping(value = "/updateProgramById")
    public ResponseUtil<Program> updateProgramById(@RequestBody Program program) {
        logger.info("updateProgramById and program:{}", JSON.toJSONString(program));
        ResponseUtil<Program> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(program.get_id())) {
            return responseUtil.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            responseUtil = programService.updateProgramById(program);
        } catch (Exception e) {
            logger.error("updateProgramById and program:{}", JSON.toJSONString(program), e);

        }
        return responseUtil;
    }

    /**
     * @Description: 删除节目
     * @Title: deleteById
     * @param id 节目id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "删除节目")
    @PostMapping(value = "/deleteById")
    public ResponseUtil<Program> deleteById(@RequestParam(value = "id") String id) {
        logger.info("deleteById and id={}", JSON.toJSONString(id));
        ResponseUtil<Program> responseUtil = new ResponseUtil<>();
        try {
            responseUtil = programService.deleteById(id);
        } catch (Exception e) {
            logger.error("deleteById and id={}", JSON.toJSONString(id), e);
        }
        return responseUtil;
    }
}
