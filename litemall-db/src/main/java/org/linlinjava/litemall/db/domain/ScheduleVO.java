package org.linlinjava.litemall.db.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ScheduleVO implements Serializable {
     DateType dateType; //如果此项值等于DateType.Range， 表示定时项的日期范围：从 startDate 到 endDate。 如果此项值等于DateType.All， 表示日期范围为：永久
     String startDate;//例："2015-10-27"， 此值在dateType等于DateType.Range时有效
     String endDate; //例："2015-11-18"， 此值在dateType等于DateType.Range时有效
     TimeType timeType;//如果此项值等于TimeType.Range, 表示定时项的时间范围：每天 startTime 到 endTime。  如果此项值等于TimeType.All， 表示时间范围为：全天
     String startTime;//例："07:30"， 此值在timeType等于TimeType.Range时有效
     String endTime; //例："21:40"， 此值在timeType等于TimeType.Range时有效
     FilterType filterType;//如果此项值为FilterType.None,表示不过滤定时项。如果此项值为FilterType.Week，表示定时项按星期过滤。如果此项值为FilterType.Month，表示定时项按月份过滤。
     List<Integer> weekFilter;// sunday==0  例：[1,6,0] 表示定时项按周一、周六、周日过滤， 此值在filterType等于FilterType.Week时有效
     List<Integer> monthFilter;// 例：[1,13,28] 表示定时项按每月1、13、28号过滤， 此值在filterType等于FilterType.Month时有效
     String lng ="zh-CN";//语言为中文

     //----------------扩展字段----------------
     String id;
     Long createTime;//创建时间
     Long updateTime;//修改时间
}
