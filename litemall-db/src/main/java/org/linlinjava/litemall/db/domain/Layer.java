package org.linlinjava.litemall.db.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Layer implements Serializable {

    Boolean repeat;//若干repeat=true，当前layer下的sources根据时间最长的一个layer重复显示，通常设置为false

    Boolean isRepeat;//若干isRepeat=true，当前layer下的sources根据时间最长的一个layer重复显示，通常设置为false

    List<Source> sources;//素材数据

    //----------------扩展字段----------------
    String id;
    String sourcesIds;
}
