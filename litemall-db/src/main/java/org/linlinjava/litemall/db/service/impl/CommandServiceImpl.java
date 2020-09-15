package org.linlinjava.litemall.db.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.util.DateUtil;
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
    public PageInfo<Command> selectCommandPage(Command command, Integer pageNum, Integer pageSize) {
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
    public Command selectCommandById(String commandId) {
        Command command = null;
        try {
            command = commandMapper.selectByPrimaryKey(commandId);
        } catch (Exception e) {
            logger.error("selectCommandById error and msg={}", e);
        }
        return command;
    }

    @Override
    public int insertCommand(Command command) {
        int n = 0;
        command.setId(UUID.randomUUID().toString().replace("-", ""));
        try {
            long cuttentTime = DateUtil.getDateline();
            command.setCreateTime(cuttentTime);
            command.setUpdateTime(cuttentTime);
            n = commandMapper.insertSelective(command);
        } catch (Exception e) {
            logger.error("insertCommand error and msg={}", e);
        }
        return n;
    }

    @Override
    public int updateCommandById(Command command) {
        int n = 0;
        try {
            command.setUpdateTime(DateUtil.getDateline());
            n = commandMapper.updateByPrimaryKeySelective(command);
        } catch (Exception e) {
            logger.error("updateCommandById error and msg={}", e);
        }
        return n;
    }

    @Override
    public int deleteById(String id) {
        int n = 0;
        try {
            n = commandMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("deleteById error and msg={}", e);
        }
        return n;
    }
}
