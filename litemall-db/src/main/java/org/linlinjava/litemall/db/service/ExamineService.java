package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.domain.Examine;
import org.linlinjava.litemall.db.util.ResponseUtil;

public interface ExamineService {

    /**
     * @Description: 获取审核列表
     * @title selectExaminePage
     * @param pageNum 开始页数
     * @param pageSize 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    PageInfo<Examine> selectExaminePage(Examine examine,Integer pageNum,Integer pageSize);

    /**
     * @Description: 通过审核id查看审核详情
     * @Title: selectExamineById
     * @param examineId 审核id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    ResponseUtil<Examine> selectExamineById(String examineId);

    /**
     * @Description: 添加审核
     * @title insertExamine
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    ResponseUtil<Examine> insertExamine(Examine examine);

    /**
     * @Description: 编辑审核
     * @title updateExamineById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    ResponseUtil<Examine> updateExamineById(Examine examine);

    /**
     * @Description: 删除审核
     * @Title: deleteById
     * @param id 审核id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    ResponseUtil<Examine> deleteById(String id);
}
