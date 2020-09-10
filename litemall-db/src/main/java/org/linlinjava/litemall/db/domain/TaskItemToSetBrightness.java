package org.linlinjava.litemall.db.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

//定时项
@Data
public class TaskItemToSetBrightness implements Serializable {
    public int brightness;  //此定时项的亮度值
    public List<Schedule> schedules; //此定时项的时间安排
}