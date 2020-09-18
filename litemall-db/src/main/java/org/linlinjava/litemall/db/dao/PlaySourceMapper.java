package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.PlaySource;

import java.util.List;

@Mapper
public interface PlaySourceMapper {

    List<PlaySource> selectPlaySourcePage(@Param("playSource") PlaySource playSource);

    PlaySource selectByPrimaryKey(String id);

    int insertSelective(PlaySource playSource);

    int updateByPrimaryKeySelective(PlaySource playSource);

    int deleteByIdBatch(String[] ids);

    int deleteByPrimaryKey(String id);

    /**
     * @Description: 通过资源id和层id查看播放资源详情
     * @Title: selectBySourceIdAndLayerId
     * @param sourceId 资源id
     * @param layerId 层id
     * @auther IngaWu
     * @currentdate:2020年9月18日
     */
    PlaySource selectBySourceIdAndLayerId(@Param("sourceId") String sourceId,@Param("layerId") String layerId);

    /**
     * @Description: 通过资源id和节目id查看播放资源详情
     * @Title: selectBySourceIdAndLayerId
     * @param sourceId 资源id
     * @param programId 节目id
     * @auther IngaWu
     * @currentdate:2020年9月18日
     */
    PlaySource selectBySourceIdAndProgramId(@Param("sourceId") String sourceId,@Param("programId") String programId);
}