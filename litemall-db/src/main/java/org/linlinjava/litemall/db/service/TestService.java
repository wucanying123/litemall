package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.util.ResponseUtil;

public interface TestService {

    /**
     * 模拟添加熙讯任务
     */
    ResponseUtil<Object> imitateAddXixunTask();

    void imitateAddSource(String path, String sourceName, String fileExt);
}
