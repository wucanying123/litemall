package org.linlinjava.litemall.db.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xinda.common.BaseResp;
import com.xinda.common.Constant;
import com.xinda.screen.dao.CommandMapper;
import com.xinda.screen.model.entity.Command;
import com.xinda.screen.service.CommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommandServiceImpl implements CommandService {

    @Autowired
    private CommandMapper commandMapper;


    private static Logger logger = LoggerFactory.getLogger(CommandServiceImpl.class);

    @Override
    public PageInfo<Command> selectCommandPage(Command command,Integer pageNum,Integer pageSize) {
        PageInfo<Command> page = null;
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Command> list = commandMapper.selectCommandPage(command);
//            String jsonString = JSON.toJSONString(list);
            page = new PageInfo<>(list);
        } catch (Exception e) {
            logger.error("selectCommandPage error and msg={}", e);
        }
        return page;
    }

    @Override
    public BaseResp<Command> selectCommandById(String commandId) {
        BaseResp<Command> baseResp = new BaseResp<Command>();
        try {
            Command command = commandMapper.selectByPrimaryKey(commandId);
            baseResp.setData(command);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectCommandById error and msg={}", e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Command> insertCommand(Command command) {
        BaseResp<Command> baseResp = new BaseResp<Command>();
        command.setId(UUID.randomUUID().toString().replace("-", ""));
        try {
            int n = commandMapper.insertSelective(command);
            if (n == 1) {
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertCommand error and msg={}", e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Command> updateCommandById(Command command) {
        BaseResp<Command> baseResp = new BaseResp<Command>();
        try {
            int n = commandMapper.updateByPrimaryKeySelective(command);
            if (n >= 1) {
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateCommandById error and msg={}", e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Command> deleteByIdBatch(String[] ids) {
        BaseResp<Command> baseResp = new BaseResp<Command>();
        try {
            int m = commandMapper.deleteByIdBatch(ids);
            if (m >= 1) {
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteByIdBatch error and msg={}", e);
        }
        return baseResp;
    }
}
