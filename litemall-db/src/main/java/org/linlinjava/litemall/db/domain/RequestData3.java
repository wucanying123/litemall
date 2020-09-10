package org.linlinjava.litemall.db.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class RequestData3 implements Serializable {

    public String type;
    public Command2 command;
}
