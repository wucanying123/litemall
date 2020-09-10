package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.xinda.common.BaseResp;
import com.xinda.common.Constant;
import com.xinda.screen.model.entity.Program;
import com.xinda.screen.service.ProgramService;
import com.xinda.util.StringUtilsXD;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/screen/program")
public class ProgramController {
    @Autowired
    private ProgramService programService;

    private static Logger logger = LoggerFactory.getLogger(ProgramController.class);

    /**
     * @Description: 获取节目列表
     * @title selectProgramPage
     * @param pageNum 开始页数
     * @param pageSize 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "获取节目列表")
    @GetMapping("/selectProgramPage")
    public BaseResp<PageInfo<Program>> selectProgramPage(@RequestBody Program program,@RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        logger.info("selectProgramPage and program={}, pageNum={},pageSize",JSON.toJSONString(program), pageNum, pageSize);
        BaseResp<PageInfo<Program>> baseResp = new BaseResp<>();
        try {
            PageInfo<Program> page = programService.selectProgramPage(program, StringUtilsXD.checkPageNumParam(pageNum), StringUtilsXD.checkPageSizeParam(pageSize));
            baseResp.setData(page);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectProgramPage and program={},pageNum={},pageSize", JSON.toJSONString(program), pageNum, pageSize, e);
        }
        return baseResp;
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
    public BaseResp<Program> selectProgramById(@RequestParam(value = "programId") String programId) {
        logger.info("selectProgramById and programId={}", programId);
        BaseResp<Program> baseResp = new BaseResp<>();
        if (StringUtilsXD.isBlank(programId)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            baseResp = programService.selectProgramById(programId);
        } catch (Exception e) {
            logger.error("selectProgramById and programId={}", programId, e);
        }
        return baseResp;
    }

    /**
     * @Description: 添加节目
     * @Title: insertProgram
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "添加节目")
    @PostMapping(value = "/insertProgram")
    public BaseResp<Program> insertProgram(@RequestBody Program program) {
        logger.info("insertProgram and program:{}", JSON.toJSONString(program));
        BaseResp<Program> baseResp = new BaseResp<>();
        try {
            baseResp = programService.insertProgram(program);
        } catch (Exception e) {
            logger.error("insertProgram and program:{}", JSON.toJSONString(program), e);
        }
        return baseResp;
    }

    /**
     * @Description: 编辑节目
     * @title updateProgramById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "编辑节目")
    @PostMapping(value = "/updateProgramById")
    public BaseResp<Program> updateProgramById(@RequestBody Program program) {
        logger.info("updateProgramById and program:{}", JSON.toJSONString(program));
        BaseResp<Program> baseResp = new BaseResp<>();
        if (StringUtilsXD.isBlank(program.get_id())) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            baseResp = programService.updateProgramById(program);
        } catch (Exception e) {
            logger.error("updateProgramById and program:{}", JSON.toJSONString(program), e);

        }
        return baseResp;
    }

    /**
     * @Description: 删除节目
     * @Title: deleteByIdBatch
     * @param ids 节目id集合
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "删除节目")
    @PostMapping(value = "/deleteByIdBatch")
    public BaseResp<Program> deleteByIdBatch(@RequestParam(value = "ids") String[] ids) {
        logger.info("deleteByIdBatch and ids={}", JSON.toJSONString(ids));
        BaseResp<Program> baseResp = new BaseResp<>();
        try {
            baseResp = programService.deleteByIdBatch(ids);
        } catch (Exception e) {
            logger.error("deleteByIdBatch and ids={}", JSON.toJSONString(ids), e);
        }
        return baseResp;
    }
}
