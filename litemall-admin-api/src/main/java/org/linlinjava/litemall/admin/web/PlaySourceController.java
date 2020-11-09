package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.linlinjava.litemall.db.domain.LitemallAdmin;
import org.linlinjava.litemall.db.domain.PlaySource;
import org.linlinjava.litemall.db.service.PlaySourceService;
import org.linlinjava.litemall.db.util.Constant;
import org.linlinjava.litemall.db.util.ImageUtil;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.StringUtilsXD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/screen/playSource")
public class PlaySourceController {
    @Autowired
    private PlaySourceService playSourceService;

    private static Logger logger = LoggerFactory.getLogger(PlaySourceController.class);

    /**
     * @param page  开始页数
     * @param limit 每页条数
     * @Description: 获取播放资源列表
     * @title selectPlaySourcePage
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "获取播放资源列表")
    @GetMapping("/selectPlaySourcePage")
    public ResponseUtil selectPlaySourcePage(String name, String _type, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        ResponseUtil responseUtil = new ResponseUtil();
        PlaySource playSource = new PlaySource();
        playSource.setName(name);
        playSource.set_type(_type);
        logger.info("selectPlaySourcePage and playSource={},page={},limit={}", JSON.toJSONString(playSource), page, limit);
        try {
            PageInfo<PlaySource> pageList = playSourceService.selectPlaySourcePage(playSource, StringUtilsXD.checkPageNumParam(page), StringUtilsXD.checkPageSizeParam(limit));
            return responseUtil.succeedPage(pageList);
        } catch (Exception e) {
            logger.error("selectPlaySourcePage and playSource={},page={},limit={}", JSON.toJSONString(playSource), page, limit, e);
        }
        return responseUtil;
    }

    /**
     * @param playSourceId 播放资源id
     * @Description: 通过播放资源id查看播放资源详情
     * @Title: selectPlaySourceById
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "通过播放资源id查看播放资源详情")
    @GetMapping(value = "/selectPlaySourceById")
    public ResponseUtil<PlaySource> selectPlaySourceById(@RequestParam(value = "playSourceId") String playSourceId) {
        logger.info("selectPlaySourceById and playSourceId={}", playSourceId);
        ResponseUtil<PlaySource> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(playSourceId)) {
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            PlaySource playSource = playSourceService.selectPlaySourceById(playSourceId);
            responseUtil.setData(playSource);
            responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectPlaySourceById and playSourceId={}", playSourceId, e);
        }
        return responseUtil;
    }

    /**
     * @Description: 添加播放资源
     * @Title: insertPlaySource
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "添加播放资源")
    @PostMapping(value = "/insertPlaySource")
    public ResponseUtil<PlaySource> insertPlaySource(@RequestBody PlaySource playSource) {
        logger.info("insertPlaySource and playSource:{}", JSON.toJSONString(playSource));
        ResponseUtil<PlaySource> responseUtil = new ResponseUtil<>();
        try {
            Subject currentUser = SecurityUtils.getSubject();
            LitemallAdmin admin = (LitemallAdmin) currentUser.getPrincipal();
            playSource.setUserid(admin.getId());
            int n = playSourceService.insertPlaySource(playSource);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertPlaySource and playSource:{}", JSON.toJSONString(playSource), e);
        }
        return responseUtil;
    }

    /**
     * @Description: 编辑播放资源
     * @title updatePlaySourceById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "编辑播放资源")
    @PostMapping(value = "/updatePlaySourceById")
    public ResponseUtil<PlaySource> updatePlaySourceById(@RequestBody PlaySource playSource) {
        logger.info("updatePlaySourceById and playSource:{}", JSON.toJSONString(playSource));
        ResponseUtil<PlaySource> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(playSource.getId())) {
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            int n = playSourceService.updatePlaySourceById(playSource);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updatePlaySourceById and playSource:{}", JSON.toJSONString(playSource), e);

        }
        return responseUtil;
    }

    /**
     * @param id 播放资源id
     * @Description: 删除播放资源
     * @Title: deleteById
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "删除播放资源")
    @PostMapping(value = "/deleteById")
    public ResponseUtil<PlaySource> deleteById(@RequestParam(value = "id") String id) {
        logger.info("deleteById and id={}", JSON.toJSONString(id));
        ResponseUtil<PlaySource> responseUtil = new ResponseUtil<>();
        try {
            int n = playSourceService.deleteById(id);
            if (n == 1) {
                responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteById and id={}", JSON.toJSONString(id), e);
        }
        return responseUtil;
    }


    /**
     * @param html html
     * @Description: html转Base64
     * @Title: htmlToBase64
     * @auther IngaWu
     * @currentdate:2020年10月27日
     */
    @ApiOperation(value = "html转Base64")
    @PostMapping(value = "/htmlToBase64")
    public ResponseUtil<String> htmlToBase64(int width,int height,String color,@RequestParam(value = "html") String html) {
        ResponseUtil<String> responseUtil = new ResponseUtil<>();
        try {
            height = height/3;
            String saveImageLocation = "C:/Users/Admin/Desktop/source/base64.jpg";
            html = "<div style=\"background:"+color+";width:"+width+"px; height:"+height+"px\">" + html +"</div>";
            ImageUtil.html2Img(html, saveImageLocation);
            ImageUtil.cutImage(saveImageLocation,3);
            String base64Str = ImageUtil.getImageStr(saveImageLocation);
            base64Str = StringUtilsXD.replaceBlank(base64Str);
            responseUtil.setData(base64Str);
            responseUtil.initCodeAndMsg(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("htmlToBase64 and html={}", JSON.toJSONString(html), e);
        }
        return responseUtil;
    }
}
