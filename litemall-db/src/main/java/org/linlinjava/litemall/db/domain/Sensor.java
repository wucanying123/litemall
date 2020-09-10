package org.linlinjava.litemall.db.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Sensor implements Serializable {

    String type;
    String action;
    String callbackURL; //接收传感器数据的http地址，post方法
    boolean subscribe; //true订阅，false取消订阅
}
