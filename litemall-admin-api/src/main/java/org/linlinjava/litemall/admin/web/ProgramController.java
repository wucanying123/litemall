package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.linlinjava.litemall.db.domain.*;
import org.linlinjava.litemall.db.util.FastJsonUtils;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.Constant;
import org.linlinjava.litemall.db.service.ProgramService;
import io.swagger.annotations.ApiOperation;
import org.linlinjava.litemall.db.util.StringUtilsXD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseUtil selectProgramPage(String name,@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        ResponseUtil responseUtil = new ResponseUtil();
        Program program = new Program();
        program.setName(name);
        logger.info("selectProgramPage and program={},page={},limit={}", JSON.toJSONString(program), page, limit);
        try {
            PageInfo<Program> pageList = programService.selectProgramPage(program, StringUtilsXD.checkPageNumParam(page), StringUtilsXD.checkPageSizeParam(limit));
            return responseUtil.succeedPage(pageList);
        } catch (Exception e) {
            logger.error("selectProgramPage and program={},page={},limit={}", JSON.toJSONString(program), page, limit, e);
        }
        return responseUtil;
    }

    @GetMapping("/readProgram")
    public Object readProgram(@NotNull String id) {
        Program program = programService.readProgram(id);
        Map<String, Object> data = new HashMap<>(2);
        data.put("program", program);
        List<PlaySource> playSourceList = new ArrayList<>();
        if(null !=program.getLayers() && program.getLayers().size() >0 ) {
            playSourceList = program.getLayers().get(0).getSources();
            if(null != playSourceList && playSourceList.size() >0) {
                String[] playSource = new String[playSourceList.size()];
                for (int i = 0; i < playSourceList.size(); i++) {
                    if(null != playSourceList.get(i)) {
                        playSource[i] = playSourceList.get(i).getSourceId();
                    }
                }
                program.setPlaySource(playSource);
            }
        }
        data.put("playSourceList", playSourceList);
        return ResponseUtil.ok(data);
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
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            Program program = programService.selectProgramById(programId);
            responseUtil.setData(program);
            responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
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
            Subject currentUser = SecurityUtils.getSubject();
            LitemallAdmin admin = (LitemallAdmin) currentUser.getPrincipal();
            program.setUserid(admin.getId());
            int n = programService.insertProgram(program);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
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
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            int n = programService.updateProgramById(program);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateProgramById and program:{}", JSON.toJSONString(program), e);

        }
        return responseUtil;
    }


    /**
     * @Description: 编辑简易节目
     * @title
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "编辑简易节目")
    @PostMapping(value = "/updateSimpleProgramById")
    public ResponseUtil<Program> updateSimpleProgramById(@RequestBody Program program) {
        logger.info("updateSimpleProgramById and program:{}", JSON.toJSONString(program));
        ResponseUtil<Program> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(program.get_id())) {
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            int n = programService.updateProgramById(program);
//            String[] sourceIdList = program.getPlaySource();
//            String a = String.join(",", sourceIdList);
            programService.updatePlaySources(program);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateSimpleProgramById and program:{}", JSON.toJSONString(program), e);

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
            int n = programService.deleteById(id);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteById and id={}", JSON.toJSONString(id), e);
        }
        return responseUtil;
    }

    /**
     * @Description: 编辑高级节目
     * @title
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "编辑高级节目")
    @PostMapping(value = "/updateComplexProgramById")
    public ResponseUtil<Program> updateComplexProgramById(@RequestBody String programJson) {
        logger.info("updateComplexProgramById and programJson:{}", JSON.toJSONString(programJson));
        ResponseUtil<Program> responseUtil = new ResponseUtil<>();
        Program program = FastJsonUtils.getJsonToBean(programJson,Program.class);
        if (StringUtilsXD.isBlank(program.get_id())) {
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            int n = programService.updateProgramById(program);
            programService.updateComplexProgramById(program);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateComplexProgramById and program:{}", JSON.toJSONString(program), e);
        }
        return responseUtil;
    }
}
