package org.linlinjava.litemall.db.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Program implements Serializable {
    String _id;//_id必须有内容，且不重复
    Integer totalSize;//当前节目中sources数组中每个元素的size属性的累加和
    String name;//节目名字
    Integer width;//节目窗口宽
    Integer height;//节目窗口高
    String _company;//公司id，不使用AIPS平台时不重要，可任意设置
    String _department;//部门id，不使用AIPS平台时不重要，可任意设置
    String _role;//角色id，不使用AIPS平台时不重要，可任意设置
    String _user;//用户，不使用AIPS平台时不重要，可任意设置
    Integer __v;//节目版本类型，默认0不用修改，会被播放程序改写
    List<Layer> layers;//layer对应AIPS平台节目编辑中的时间轴，用于内容分层显示，实际显示效果可参考平台节目预览
    Long createTime;//创建时间

    //----------------扩展字段----------------
    String dateCreated;//创建时间字符串形式，可为空 展示用
    String layersIds;
    Long updateTime;//修改时间
    Integer userid;//用户id
    String[] playSource;//播放资源id,仅作用于简易节目
}
