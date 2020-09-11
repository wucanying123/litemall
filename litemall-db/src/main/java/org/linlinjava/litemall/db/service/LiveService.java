package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageInfo;
import org.linlinjava.litemall.db.domain.Live;
import org.linlinjava.litemall.db.util.ResponseUtil;

public interface LiveService {

    /**
     * @Description: 获取直播列表
     * @title selectLivePage
     * @param pageNum 开始页数
     * @param pageSize 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    PageInfo<Live> selectLivePage(Live live,Integer pageNum,Integer pageSize);

    /**
     * @Description: 通过直播id查看直播详情
     * @Title: selectLiveById
     * @param liveId 直播id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    ResponseUtil<Live> selectLiveById(String liveId);

    /**
     * @Description: 添加直播
     * @title insertLive
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    ResponseUtil<Live> insertLive(Live live);

    /**
     * @Description: 编辑直播
     * @title updateLiveById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    ResponseUtil<Live> updateLiveById(Live live);

    /**
     * @Description: 删除直播
     * @Title: deleteById
     * @param id 直播id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    ResponseUtil<Live> deleteById(String id);
}
