package org.linlinjava.litemall.db.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Item implements Serializable {
    String _id;//_id必须有内容，且不重复
    Program _program;//节目数据
    Integer priority;//task item优先级，数值越小优先级越高，多个节目在同一个可播放时段内按该优先级顺序播放
    Integer repeatTimes;//根据优先级获得播放权后的播放次数

    /*0为高级节目，2为简易节目
      *简易节目素材只能是Video或Image，不能指定特效，默认显示坐标0，0，素材宽高默认与节目宽高相同
      */
    Integer version;
    List<ScheduleVO> schedules;//定时播放，日期、时间和周几可以同时设置，为逻辑与的关系

    //----------------扩展字段----------------
    String programId; //节目id

    String schedulesIds;
    Long createTime;//创建时间
    Long updateTime;//修改时间
    Integer userid;//用户id

    String[] program;
}
