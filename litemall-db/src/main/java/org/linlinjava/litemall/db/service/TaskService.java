package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.domain.Task;

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
    Task selectTaskById(String taskId);

    /**
     * @Description: 添加任务
     * @title insertTask
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    int insertTask(Task task);

    /**
     * @Description: 编辑任务
     * @title updateTaskById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    int updateTaskById(Task task);

    /**
     * @Description: 编辑完整任务
     * @title updateTaskTotalById
     * @author IngaWu
     * @currentdate:2020年9月22日
     */
    int updateTaskTotalById(Task task);

    /**
     * @Description: 删除任务
     * @Title: deleteById
     * @param id 任务id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    int deleteById(String id);

    /**
     * @Description: 快速创建任务
     * @Title: insertQuickTask
     * @auther IngaWu
     * @currentdate:2020年9月22日
     */
    int insertQuickTask(String taskName,String programId,Integer userId);

    /**
     * @Description: 通过任务名称查看任务详情
     * @Title: selectTaskByName
     * @param taskName 任务名称
     * @auther IngaWu
     * @currentdate:2020年9月25日
     */
    Task selectTaskByName(String taskName);
}
