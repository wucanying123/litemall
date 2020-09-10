package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageInfo;
import com.xinda.common.BaseResp;
import com.xinda.screen.model.entity.Task;

public interface TaskService {

    /**
     * @Description: 获取任务列表
     * @title selectTaskPage
     * @param pageNum 开始页数
     * @param pageSize 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    PageInfo<Task> selectTaskPage(Task task,Integer pageNum,Integer pageSize);

    /**
     * @Description: 通过任务id查看任务详情
     * @Title: selectTaskById
     * @param taskId 任务id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    BaseResp<Task> selectTaskById(String taskId);

    /**
     * @Description: 添加任务
     * @title insertTask
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    BaseResp<Task> insertTask(Task task);

    /**
     * @Description: 编辑任务
     * @title updateTaskById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    BaseResp<Task> updateTaskById(Task task);

    /**
     * @Description: 删除任务
     * @Title: deleteByIdBatch
     * @param ids 任务id集合
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    BaseResp<Task> deleteByIdBatch(String[] ids);
}
