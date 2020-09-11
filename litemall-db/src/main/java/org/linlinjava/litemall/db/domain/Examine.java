package org.linlinjava.litemall.db.domain;

import lombok.Data;

@Data
public class Examine {
    /**
     * id
     */
    private String id;

    /**
     * 类型：0节目，1直播，2资源
     */
    private String type;

    /**
     * 审核状态：0未审核，1一审过等待二审，2一审不过，3二审过，4二审不过
     */
    private String passStatus;

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