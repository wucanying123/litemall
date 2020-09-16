package org.linlinjava.litemall.db.domain;

import lombok.Data;

@Data
public class ScreenDevice {
    /**
     * id
     */
    private String id;

    /**
     * 卡号
     */
    private String cardId;

    /**
     * 网络类型
     */
    private String networkType;

    /**
     * 宽
     */
    private Integer width;

    /**
     * 高
     */
    private Integer height;

    /**
     * 音量
     */
    private Integer volume;

    /**
     * 屏幕状态
     */
    private Boolean screenOpenStatus;

    /**
     * 在线状态
     */
    private Boolean onlineStatus;

    /**
     * 屏幕亮度
     */
    private Integer brightness;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;
}