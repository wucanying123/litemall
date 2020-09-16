package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.linlinjava.litemall.db.domain.LitemallAdmin;
import org.linlinjava.litemall.db.domain.Live;
import org.linlinjava.litemall.db.service.LiveService;
import org.linlinjava.litemall.db.service.ScreenService;
import org.linlinjava.litemall.db.util.Constant;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.StringUtilsXD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/screen/live")
public class LiveController {
    @Autowired
    private LiveService liveService;
    @Autowired
    private ScreenService screenService;

    private static Logger logger = LoggerFactory.getLogger(LiveController.class);

    /**
     * @Description: 获取直播列表
     * @title selectLivePage
     * @param page 开始页数
     * @param limit 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "获取直播列表")
    @GetMapping("/selectLivePage")
    public ResponseUtil selectLivePage(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        ResponseUtil responseUtil = new ResponseUtil();
        Live live = null;
        logger.info("selectLivePage and live={},page={},limit", JSON.toJSONString(live), page, limit);
        try {
            PageInfo<Live> pageList = liveService.selectLivePage(live, StringUtilsXD.checkPageNumParam(page), StringUtilsXD.checkPageSizeParam(limit));
            return responseUtil.succeedPage(pageList);
        } catch (Exception e) {
            logger.error("selectLivePage and live={},page={},limit", JSON.toJSONString(live), page, limit, e);
        }
        return responseUtil;
    }

    /**
     * @Description: 通过直播id查看直播详情
     * @param liveId 直播id
     * @Title: selectLiveById
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "通过直播id查看直播详情")
    @GetMapping(value = "/selectLiveById")
    public ResponseUtil<Live> selectLiveById(@RequestParam(value = "liveId") String liveId) {
        logger.info("selectLiveById and liveId={}", liveId);
        ResponseUtil<Live> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(liveId)) {
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            Live live = liveService.selectLiveById(liveId);
            responseUtil.setData(live);
            responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectLiveById and liveId={}", liveId, e);
        }
        return responseUtil;
    }

    /**
     * @Description: 添加直播
     * @Title: insertLive
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "添加直播")
    @PostMapping(value = "/insertLive")
    public ResponseUtil<Live> insertLive(@RequestBody Live live) {
        logger.info("insertLive and live:{}", JSON.toJSONString(live));
        ResponseUtil<Live> responseUtil = new ResponseUtil<>();
        try {
            Subject currentUser = SecurityUtils.getSubject();
            LitemallAdmin admin = (LitemallAdmin) currentUser.getPrincipal();
            live.setUserid(admin.getId());
            int n = liveService.insertLive(live);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertLive and live:{}", JSON.toJSONString(live), e);
        }
        return responseUtil;
    }

    /**
     * @Description: 编辑直播
     * @title updateLiveById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "编辑直播")
    @PostMapping(value = "/updateLiveById")
    public ResponseUtil<Live> updateLiveById(@RequestBody Live live) {
        logger.info("updateLiveById and live:{}", JSON.toJSONString(live));
        ResponseUtil<Live> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(live.getId())) {
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            int n = liveService.updateLiveById(live);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateLiveById and live:{}", JSON.toJSONString(live), e);

        }
        return responseUtil;
    }

    /**
     * @Description: 删除直播
     * @Title: deleteById
     * @param id 直播id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "删除直播")
    @PostMapping(value = "/deleteById")
    public ResponseUtil<Live> deleteById(@RequestParam(value = "id") String id) {
        logger.info("deleteById and id={}", JSON.toJSONString(id));
        ResponseUtil<Live> responseUtil = new ResponseUtil<>();
        try {
            int n = liveService.deleteById(id);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteById and id={}", JSON.toJSONString(id), e);
        }
        return responseUtil;
    }

    /**
     * @Description: 播放直播
     * @Title: playLive
     * @param id 直播id
     * @auther IngaWu
     * @currentdate:2020年9月15日
     */
    @ApiOperation(value = "播放直播")
    @PostMapping(value = "/playLive")
    public ResponseUtil<Object> playLive(@RequestParam(value = "id") String id,String cardId) {
        logger.info("playLive and id={}", JSON.toJSONString(id));
        return screenService.playLiveVideo(id,cardId);
    }

    @ApiOperation(value = "停止直播")
    @PostMapping(value = "/stopLiveVideo")
    public ResponseUtil<Object> stopLiveVideo(String cardId) {
        return screenService.stopLiveVideo(cardId);
    }
}
