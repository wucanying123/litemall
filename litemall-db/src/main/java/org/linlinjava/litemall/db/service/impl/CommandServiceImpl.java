package org.linlinjava.litemall.db.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.util.DateUtil;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.Constant;
import org.linlinjava.litemall.db.dao.CommandMapper;
import org.linlinjava.litemall.db.domain.Command;
import org.linlinjava.litemall.db.service.CommandService;
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
    public ResponseUtil<Command> selectCommandById(String commandId) {
        ResponseUtil<Command> responseUtil = new ResponseUtil<Command>();
        try {
            Command command = commandMapper.selectByPrimaryKey(commandId);
            responseUtil.setData(command);
            responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectCommandById error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Command> insertCommand(Command command) {
        ResponseUtil<Command> responseUtil = new ResponseUtil<Command>();
        command.setId(UUID.randomUUID().toString().replace("-", ""));
        try {
            command.setCreateTime(DateUtil.getDateline());
            int n = commandMapper.insertSelective(command);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertCommand error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Command> updateCommandById(Command command) {
        ResponseUtil<Command> responseUtil = new ResponseUtil<Command>();
        try {
            command.setUpdateTime(DateUtil.getDateline());
            int n = commandMapper.updateByPrimaryKeySelective(command);
            if (n >= 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateCommandById error and msg={}", e);
        }
        return responseUtil;
    }

    @Override
    public ResponseUtil<Command> deleteById(String id) {
        ResponseUtil<Command> responseUtil = new ResponseUtil<Command>();
        try {
            int m = commandMapper.deleteByPrimaryKey(id);
            if (m >= 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return responseUtil;
    }
}
