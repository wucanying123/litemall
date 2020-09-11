package org.linlinjava.litemall.db.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Task implements Serializable {
    String _id;//_id必须有内容，且不重复
    String name;//节目名称
    String _department;//所属部门，可以为null或空字符串
    List<org.linlinjava.litemall.db.domain.Item> items;//一个task可以包含多个节目，每个节目对应一个item，播放规则优先按时间排序，时间相同后按priority属性值从小到大排序（值小优先播放）
    Long executeDate;//可指定节目下载日期和时间，建议设置为null
    String cmdId;//命令id，建议设置有效字符串，不要与其他id重复

    //----------------扩展字段----------------
    String itemsIds;
    Long createTime;//创建时间
    Long updateTime;//修改时间
}
