package org.linlinjava.litemall.db.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Command implements Serializable {

    String _type;//xixunplayer命令类型，用于播放节目，内容不可更改，大小写敏感
    String id;//id必须有内容，且不重复

    /*下载url前缀 preDownloadURL属性非常重要，播放程序会通过字符串连接preDownloadURL和program中source的id属性
     * 构成一个有效的http下载链接，如果preDownloadURL属性不正确将导致无法下载需要显示的图片或视频文件。
     * 注意：若不使用preDownloadURL+id作为url下载文件，可在Video、Image、Flash三类素材的source中增加url属性指明文件下载路径。
     */
    String preDownloadURL;

    /*节目的下载进度url notificationURL属性会被播放程序用来通知客户服务器，节目的下载进度。如果不设置该属性，用户无法知晓节目下载情况
     * 下载进度的数据格式请参考protocolC文档
     */
    String notificationURL;

    org.linlinjava.litemall.db.domain.Task task;//节目任务数据

    //----------------扩展字段----------------
    String taskId; //节目任务id
    Long createTime;//创建时间
    Long updateTime;//修改时间
    Integer userid;//用户id
}
