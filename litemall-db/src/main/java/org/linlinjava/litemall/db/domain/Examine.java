package org.linlinjava.litemall.db.domain;

import lombok.Data;

@Data
public class Examine {
    /**
     * id
     */
    private String id;

    /**
     * 类型：1节目，2直播，3资源
     */
    private Integer type;

    /**
     * 详细id
     */
    private String detailId;
    /**
     * 详细名称
     */
    private String detailName;

    /**
     * 审核状态：1未审核，2一审过等待二审，3一审不过，4二审过，5二审不过
     */
    private Integer passStatus;

    /**
     * 一审人员
     */
    private Integer checkUserid1;

    /**
     * 二审人员
     */
    private Integer checkUserid2;

    /**
     * 一审不通过原因
     */
    private String rejectReason1;

    /**
     * 二审不通过原因
     */
    private String rejectReason2;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;
}