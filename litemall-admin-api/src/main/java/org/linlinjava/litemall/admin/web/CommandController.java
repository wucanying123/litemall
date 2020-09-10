package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.xinda.common.BaseResp;
import com.xinda.common.Constant;
import com.xinda.screen.model.entity.Command;
import com.xinda.screen.service.CommandService;
import com.xinda.util.StringUtilsXD;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/screen/command")
public class CommandController {
    @Autowired
    private CommandService commandService;

    private static Logger logger = LoggerFactory.getLogger(CommandController.class);

    /**
     * @Description: 获取命令列表
     * @title selectCommandPage
     * @param pageNum 开始页数
     * @param pageSize 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "获取命令列表")
    @GetMapping("/selectCommandPage")
    public BaseResp<PageInfo<Command>> selectCommandPage(@RequestBody Command command,@RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        logger.info("selectCommandPage and command={}, pageNum={},pageSize",JSON.toJSONString(command), pageNum, pageSize);
        BaseResp<PageInfo<Command>> baseResp = new BaseResp<>();
        try {
            PageInfo<Command> page = commandService.selectCommandPage(command, StringUtilsXD.checkPageNumParam(pageNum), StringUtilsXD.checkPageSizeParam(pageSize));
            baseResp.setData(page);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectCommandPage and command={},pageNum={},pageSize", JSON.toJSONString(command), pageNum, pageSize, e);
        }
        return baseResp;
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
    public BaseResp<Command> selectCommandById(@RequestParam(value = "commandId") String commandId) {
        logger.info("selectCommandById and commandId={}", commandId);
        BaseResp<Command> baseResp = new BaseResp<>();
        if (StringUtilsXD.isBlank(commandId)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            baseResp = commandService.selectCommandById(commandId);
        } catch (Exception e) {
            logger.error("selectCommandById and commandId={}", commandId, e);
        }
        return baseResp;
    }

    /**
     * @Description: 添加命令
     * @Title: insertCommand
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "添加命令")
    @PostMapping(value = "/insertCommand")
    public BaseResp<Command> insertCommand(@RequestBody Command command) {
        logger.info("insertCommand and command:{}", JSON.toJSONString(command));
        BaseResp<Command> baseResp = new BaseResp<>();
        try {
            baseResp = commandService.insertCommand(command);
        } catch (Exception e) {
            logger.error("insertCommand and command:{}", JSON.toJSONString(command), e);
        }
        return baseResp;
    }

    /**
     * @Description: 编辑命令
     * @title updateCommandById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "编辑命令")
    @PostMapping(value = "/updateCommandById")
    public BaseResp<Command> updateCommandById(@RequestBody Command command) {
        logger.info("updateCommandById and command:{}", JSON.toJSONString(command));
        BaseResp<Command> baseResp = new BaseResp<>();
        if (StringUtilsXD.isBlank(command.getId())) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            baseResp = commandService.updateCommandById(command);
        } catch (Exception e) {
            logger.error("updateCommandById and command:{}", JSON.toJSONString(command), e);
        }
        return baseResp;
    }

    /**
     * @Description: 删除命令
     * @Title: deleteByIdBatch
     * @param ids 命令id集合
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "删除命令")
    @PostMapping(value = "/deleteByIdBatch")
    public BaseResp<Command> deleteByIdBatch(@RequestParam(value = "ids") String[] ids) {
        logger.info("deleteByIdBatch and ids={}", JSON.toJSONString(ids));
        BaseResp<Command> baseResp = new BaseResp<>();
        try {
            baseResp = commandService.deleteByIdBatch(ids);
        } catch (Exception e) {
            logger.error("deleteByIdBatch and ids={}", JSON.toJSONString(ids), e);
        }
        return baseResp;
    }
}
