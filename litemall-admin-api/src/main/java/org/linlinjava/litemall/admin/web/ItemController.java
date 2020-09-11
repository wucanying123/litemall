package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.linlinjava.litemall.db.domain.Item;
import org.linlinjava.litemall.db.domain.Item;
import org.linlinjava.litemall.db.domain.LitemallAdmin;
import org.linlinjava.litemall.db.util.ResponseUtil;
import org.linlinjava.litemall.db.util.Constant;
import org.linlinjava.litemall.db.service.ItemService;
import io.swagger.annotations.ApiOperation;
import org.linlinjava.litemall.db.util.StringUtilsXD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/screen/item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    private static Logger logger = LoggerFactory.getLogger(ItemController.class);

    /**
     * @Description: 获取项目列表
     * @title selectItemPage
     * @param page 开始页数
     * @param limit 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "获取项目列表")
    @GetMapping("/selectItemPage")
    public ResponseUtil selectItemPage(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        ResponseUtil responseUtil = new ResponseUtil();
        Item item = null;
        logger.info("selectItemPage and item={},page={},limit", JSON.toJSONString(item), page, limit);
        try {
            PageInfo<Item> pageList = itemService.selectItemPage(item, StringUtilsXD.checkPageNumParam(page), StringUtilsXD.checkPageSizeParam(limit));
            return responseUtil.succeedPage(pageList);
        } catch (Exception e) {
            logger.error("selectItemPage and item={},page={},limit", JSON.toJSONString(item), page, limit, e);
        }
        return responseUtil;
    }

    /**
     * @Description: 通过项目id查看项目详情
     * @param itemId 项目id
     * @Title: selectItemById
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "通过项目id查看项目详情")
    @GetMapping(value = "/selectItemById")
    public ResponseUtil<Item> selectItemById(@RequestParam(value = "itemId") String itemId) {
        logger.info("selectItemById and itemId={}", itemId);
        ResponseUtil<Item> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(itemId)) {
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            responseUtil = itemService.selectItemById(itemId);
        } catch (Exception e) {
            logger.error("selectItemById and itemId={}", itemId, e);
        }
        return responseUtil;
    }

    /**
     * @Description: 添加项目
     * @Title: insertItem
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "添加项目")
    @PostMapping(value = "/insertItem")
    public ResponseUtil<Item> insertItem(@RequestBody Item item) {
        logger.info("insertItem and item:{}", JSON.toJSONString(item));
        ResponseUtil<Item> responseUtil = new ResponseUtil<>();
        try {
            Subject currentUser = SecurityUtils.getSubject();
            LitemallAdmin admin = (LitemallAdmin) currentUser.getPrincipal();
            item.setUserid(admin.getId());
            responseUtil = itemService.insertItem(item);
        } catch (Exception e) {
            logger.error("insertItem and item:{}", JSON.toJSONString(item), e);
        }
        return responseUtil;
    }

    /**
     * @Description: 编辑项目
     * @title updateItemById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "编辑项目")
    @PostMapping(value = "/updateItemById")
    public ResponseUtil<Item> updateItemById(@RequestBody Item item) {
        logger.info("updateItemById and item:{}", JSON.toJSONString(item));
        ResponseUtil<Item> responseUtil = new ResponseUtil<>();
        if (StringUtilsXD.isBlank(item.get_id())) {
            return responseUtil.initCodeAndMsg(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            responseUtil = itemService.updateItemById(item);
        } catch (Exception e) {
            logger.error("updateItemById and item:{}", JSON.toJSONString(item), e);

        }
        return responseUtil;
    }

    /**
     * @Description: 删除项目
     * @Title: deleteById
     * @param id 项目id
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "删除项目")
    @PostMapping(value = "/deleteById")
    public ResponseUtil<Item> deleteById(@RequestParam(value = "id") String id) {
        logger.info("deleteById and id={}", JSON.toJSONString(id));
        ResponseUtil<Item> responseUtil = new ResponseUtil<>();
        try {
            responseUtil = itemService.deleteById(id);
        } catch (Exception e) {
            logger.error("deleteById and id={}", JSON.toJSONString(id), e);
        }
        return responseUtil;
    }
}
