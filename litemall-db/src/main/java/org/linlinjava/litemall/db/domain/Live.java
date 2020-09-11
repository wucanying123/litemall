package org.linlinjava.litemall.db.domain;

import lombok.Data;

@Data
public class Live {
    /**
     * id
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 宽
     */
    private Integer width;

    /**
     * 高
     */
    private Integer height;

    /**
     * 直播地址
     */
    private String url;

    /**
     * 用户id
     */
    private Integer userid;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;
}