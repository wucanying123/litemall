package org.linlinjava.litemall.db.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class RequestData implements Serializable {

	String type;//realtime接口命令类型，用于通知xixunplayer播放信息，内容不可更改，大小写敏感

	Command command;//命令数据
}
