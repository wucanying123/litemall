package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.domain.PlaySource;

public interface PlaySourceService {

    /**
     * @Description: 获取播放资源列表
     * @title selectPlaySourcePage
     * @param pageNum 开始页数
     * @param pageSize 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    PageInfo<PlaySource> selectPlaySourcePage(PlaySource playSource,Integer pageNum,Integer pageSize);

    /**
     * @Description: 通过播放资源id查看播放资源详情
     * @Title: selectPlaySourceById
     * @param playSourceId 播放资源id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    PlaySource selectPlaySourceById(String playSourceId);

    /**
     * @Description: 添加播放资源
     * @title insertPlaySource
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    int insertPlaySource(PlaySource playSource);

    /**
     * @Description: 编辑播放资源
     * @title updatePlaySourceById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    int updatePlaySourceById(PlaySource playSource);

    /**
     * @Description: 删除播放资源
     * @Title: deleteById
     * @param id 播放资源id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    int deleteById(String id);
}
