package org.linlinjava.litemall.db.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局常量类
 */
public class Constant {

    public static final int PAGE_INDEX_FIRST = 1; //开始页数
    public static final int PAGE_INDEX_SIZE = 15; //每页条数

    public static String LOCALHOST = "http://localhost";
    public static String WEBPORT = "8081";
    public static String UDP_HOSTNAME = "255.255.255.255";
    public static Integer UDP_PORT = 22222;
    public static String URL = LOCALHOST + ":" + WEBPORT + "/command/y60-720-40143";
    public static String PASSWORD = "999";

    /**
     * 服务端返回代码  code
     */
    public static final int STATUS_SYS_00 = 0;
    public static final int STATUS_SYS_01 = -1;
    public static final int STATUS_SYS_02 = -2;

    /**
     * 服务端返回描述
     */
    public static final String RTNINFO_SYS_00 = "操作成功!";
    public static final String RTNINFO_SYS_01 = "系统异常";
    public static final String RTNINFO_SYS_02 = "参数错误";

    //图片的后缀名
    public static final List<String> imgExt = new ArrayList<String>() {{
        add("png");
        add("jpg");
        add("jpeg");
        add("bmp");
        add("gif");
    }};
    //视频的后缀名
    public static final List<String> mp4Ext = new ArrayList<String>() {{
        add("mp4");
        add("avi");
        add("rmvb");
        add("mov");
        add("mpeg");
        add("mpg");
        add("wmv");
        add("flv");
    }};
    //word文档 的后缀名
    public static final List<String> docExt = new ArrayList<String>() {{
        add("doc");
        add("docx");
    }};
    //excel 的后缀名
    public static final List<String> xlsExt = new ArrayList<String>() {{
        add("xls");
        add("xlsx");
    }};
    //PPT 的后缀名
    public static final List<String> pptExt = new ArrayList<String>() {{
        add("ppt");
        add("pptx");
        add("pps");
    }};
    //音频 的后缀名
    public static final List<String> mp3Ext = new ArrayList<String>() {{
        add("mp3");
        add("wma");
        add("rm");
        add("wav");
        add("midi");
    }};
}