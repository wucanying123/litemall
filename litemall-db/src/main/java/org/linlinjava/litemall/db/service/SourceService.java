package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.domain.Source;
import org.linlinjava.litemall.db.util.ResponseUtil;

public interface SourceService {

    /**
     * @Description: 获取资源列表
     * @title selectSourcePage
     * @param pageNum 开始页数
     * @param pageSize 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    PageInfo<Source> selectSourcePage(Source source,Integer pageNum,Integer pageSize);

    /**
     * @Description: 通过资源id查看资源详情
     * @Title: selectSourceById
     * @param sourceId 资源id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    Source selectSourceById(String sourceId);

    /**
     * @Description: 添加资源
     * @title insertSource
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    int insertSource(Source source);

    /**
     * @Description: 编辑资源
     * @title updateSourceById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    int updateSourceById(Source source);

    /**
     * @Description: 删除资源
     * @Title: deleteById
     * @param id 资源id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    int deleteById(String id);
}
