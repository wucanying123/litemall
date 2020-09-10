package org.linlinjava.litemall.db.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

//定时亮度任务
@Data
public class TaskToSetBrightness implements Serializable {
    public String createDate; //任务创建的时间
    public String createBy; //任务创建者
    public String name; //任务名
    public List<TaskItemToSetBrightness> items; //任务项
    public int defaultBrightness; //默认亮度，表示非定时时间段使用此亮度值
}