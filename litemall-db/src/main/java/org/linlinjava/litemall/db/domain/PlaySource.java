package org.linlinjava.litemall.db.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class PlaySource implements Serializable {
    String id;//素材id，不能为空，不能与其他id重复
    String name;//名称

    String url;

    Integer maxPlayTime;//素材时长，可忽略，播放程序不使用该值

    /*
    * 素材类型:Video视频，Image图片，AnalogClock时钟，DigitalClock数字时钟，
    *        Countdown倒计时，Flash，Weather天气预报，MultiLineText多行文本，MultiLineTextV2多行文本图片
    */
    String _type;
    String md5;//素材内容MD5值

    /*
     * 视频类型"video/quicktime"，mp4类型"video/mp4"，图像类型"image/jpeg"
     * gif类型"image/gif"，flash类型"application/x-shockwave-flash"
     */
    String mime;
    Long size;//素材文件大小，单位字节
    boolean enabled;//可忽略，播放程序不使用
    String enabledBy;//可忽略，播放程序不使用
    String mediaGroup;//可忽略，播放程序不使用
    String deletedBy;//可忽略，播放程序不使用
    String deleted;//可忽略，播放程序不使用
    String newName;//可忽略，播放程序不使用
    String oldFilePath;//平台文件存放路径，可为空字符串
    String fileExt;//文件扩展名，可为空字符串
    Integer playTime;//在时间轴上的相对起始播放时间，单位秒
    Integer timeSpan;//持续显示时长，单位秒

    Integer left;//素材显示的左角坐标，单位像素
    Integer theLeft;//素材显示的左角坐标，单位像素

    Integer top;//素材显示的上角坐标，单位像素
    Integer width;//素材显示的宽，单位像素
    Integer height;//素材显示的高，单位像素
    String entryEffect;//进场特效，特效类型请参考文末
    String exitEffect;//出场特效，特效类型请参考文末
    Integer entryEffectTimeSpan;//进场特效时长，单位秒
    Integer exitEffectTimeSpan;//出场特效时长，单位秒

    Integer shade;
    Integer opacity;
    boolean showBg;
    String bgColor;
    boolean showHourScale;
    String scaleHourColor;
    boolean showMinScale;
    String scaleMinColor;
    Integer scaleStyle;
    boolean showScaleNum;
    Integer pinStyle;
    String pinHourColor;
    String pinMinColor;
    String pinSecColor;
    boolean showSecond;
    String backgroundColor;//背景色，a为透明值
    Integer speed; //速度
    Float lineHeight;//行高
    Integer timezone;//显示时间对应的时区
    String language;//语言 cn中文，en英文，pt-BR葡萄牙语，fr法语
    boolean center;//文字是否居中

    /*需要显示的内容，html格式，
     *占位符含义：%y:年,%M:月(数字表示,注意大写),%Mw:月(文字表示,注意大写),%d:日,%w:星期,%H:小时(24小时制,注意大写),%h:小时(12小时制),%am:上下午,%m:分钟,%s:秒,
     */
    String html;
    String sUrl;//可忽略，播放器不使用
    Integer sInterval;//可忽略，播放器不使用
    String city;//城市名称，支持国内主要城市
    Long code;//城市代码
    Long createTime;//创建时间
    Long updateTime;//修改时间
    Integer userid;//用户id
    String sourceId;//资源id
    String layerId;//层id
    String programId;//节目id
    String uuid;//唯一标识

    //--多行文本图片start---
    String src;//base64图片编码
    boolean fixCenter;//垂直居中
    boolean moveDown;//向下移动
    //--多行文本图片end---
    Integer imgHeight;
}
