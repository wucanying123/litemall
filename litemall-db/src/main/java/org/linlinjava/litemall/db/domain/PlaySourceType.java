package org.linlinjava.litemall.db.domain;

public enum PlaySourceType {
    /*
     * 素材类型:Video视频，Image图片，AnalogClock时钟，DigitalClock数字时钟，
     *        Countdown倒计时，Flash，Weather天气预报，MultiLineText多行文本，MultiLineTextV2多行文本图片
     */

    Video("视频"),

    Image("图片"),

    AnalogClock("时钟"),

    DigitalClock("数字时钟"),

    Countdown("倒计时"),

    Flash("Flash"),

    Weather("天气预报"),

    MultiLineText("多行文本"),

    MultiLineTextV2("多行文本图片");

    private String description;

    PlaySourceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
