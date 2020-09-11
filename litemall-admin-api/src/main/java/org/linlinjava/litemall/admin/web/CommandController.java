package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.linlinjava.litemall.db.domain.Command;
import org.linlinjava.litemall.db.service.CommandService;
import org.linlinjava.litemall.db.util.Constant;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.StringUtilsXD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/screen/command")
public class CommandController {
    @Autowired
    private CommandService commandService;

    private static Logger logger = LoggerFactory.getLogger(CommandController.class);

    /**
     * @Description: 获取命令列表
     * @title selectCommandPage
     * @param page 开始页数
     * @param limit 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "获取命令列表")
    @GetMapping("/selectCommandPage")
    public Object selectCommandPage(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        Command command = null;
        logger.info("selectCommandPage and command={},page={},limit", JSON.toJSONString(command), page, limit);
        PageInfo<Command> pageList = null;
        try {
            pageList = commandService.selectCommandPage(command, StringUtilsXD.checkPageNumParam(page), StringUtilsXD.checkPageSizeParam(limit));
            return ResponseUtil.okPage(pageList);
        } catch (Exception e) {
            logger.error("selectCommandPage and command={},page={},limit", JSON.toJSONString(command), page, limit, e);
        }
        return ResponseUtil.fail();
    }

    /**
     * @Description: 通过命令id查看命令详情
     * @param commandId 命令id
     * @Title: selectCommandById
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "通过命令id查看命令详情")
    @GetMapping(value = "/selectCommandById")
    public ResponseUtil<Command> selectCommandById(@RequestParam(value = "commandId") String commandId) {
        logger.info("selectCommandById and commandId={}", commandId);
        ResponseUtil<Command> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(commandId)) {
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            responseUtil = commandService.selectCommandById(commandId);
        } catch (Exception e) {
            logger.error("selectCommandById and commandId={}", commandId, e);
        }
        return responseUtil;
    }

    /**
     * @Description: 添加命令
     * @Title: insertCommand
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "添加命令")
    @PostMapping(value = "/insertCommand")
    public ResponseUtil<Command> insertCommand(@RequestBody Command command) {
        logger.info("insertCommand and command:{}", JSON.toJSONString(command));
        ResponseUtil<Command> responseUtil = new ResponseUtil<>();
        try {
            responseUtil = commandService.insertCommand(command);
        } catch (Exception e) {
            logger.error("insertCommand and command:{}", JSON.toJSONString(command), e);
        }
        return responseUtil;
    }

    /**
     * @Description: 编辑命令
     * @title updateCommandById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "编辑命令")
    @PostMapping(value = "/updateCommandById")
    public ResponseUtil<Command> updateCommandById(@RequestBody Command command) {
        logger.info("updateCommandById and command:{}", JSON.toJSONString(command));
        ResponseUtil<Command> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(command.getId())) {
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            responseUtil = commandService.updateCommandById(command);
        } catch (Exception e) {
            logger.error("updateCommandById and command:{}", JSON.toJSONString(command), e);
        }
        return responseUtil;
    }

    /**
     * @Description: 删除命令
     * @Title: deleteById
     * @param id 命令id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "删除命令")
    @PostMapping(value = "/deleteById")
    public ResponseUtil<Command> deleteById(@RequestParam(value = "id") String id) {
        logger.info("deleteById and id={}", JSON.toJSONString(id));
        ResponseUtil<Command> responseUtil = new ResponseUtil<>();
        try {
            responseUtil = commandService.deleteById(id);
        } catch (Exception e) {
            logger.error("deleteById and id={}", JSON.toJSONString(id), e);
        }
        return responseUtil;
    }
}
