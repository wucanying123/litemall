package org.linlinjava.litemall.db.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class SwitchWiFi implements Serializable {

    String _id;
    String type;
    boolean on;//true打开，false关闭
}
