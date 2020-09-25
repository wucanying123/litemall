package org.linlinjava.litemall.db.domain;

public enum PlaySourceType {
    /*
     * 素材类型:Video视频，Image图片，AnalogClock时钟，DigitalClock数字时钟，
     *        Countdown倒计时，Flash，Weather天气预报，MultiText多行文本
     */

    Video("视频"),

    Image("图片"),

    AnalogClock("时钟"),

    DigitalClock("数字时钟"),

    Countdown("倒计时"),

    Flash("Flash"),

    Weather("天气预报"),

    MultiText("多行文本");

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
