package org.linlinjava.litemall.db.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Command2 implements Serializable {

    String _type;

    String path;

    String password;
}
