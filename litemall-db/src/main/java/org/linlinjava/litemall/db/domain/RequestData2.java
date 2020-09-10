package org.linlinjava.litemall.db.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class RequestData2 implements Serializable {

    public String type;

    public String fn;

    public Object arg1;

    public Object arg2;

    public String apk;
}
