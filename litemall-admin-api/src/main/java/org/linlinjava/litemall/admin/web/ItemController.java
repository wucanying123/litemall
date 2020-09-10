package org.linlinjava.litemall.admin.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.xinda.common.BaseResp;
import com.xinda.common.Constant;
import com.xinda.screen.model.entity.Item;
import com.xinda.screen.service.ItemService;
import com.xinda.util.StringUtilsXD;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/screen/item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    private static Logger logger = LoggerFactory.getLogger(ItemController.class);

    /**
     * @Description: 获取项目列表
     * @title selectItemPage
     * @param pageNum 开始页数
     * @param pageSize 每页条数
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "获取项目列表")
    @GetMapping("/selectItemPage")
    public BaseResp<PageInfo<Item>> selectItemPage(@RequestBody Item item,@RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        logger.info("selectItemPage and item={}, pageNum={},pageSize",JSON.toJSONString(item), pageNum, pageSize);
        BaseResp<PageInfo<Item>> baseResp = new BaseResp<>();
        try {
            PageInfo<Item> page = itemService.selectItemPage(item, StringUtilsXD.checkPageNumParam(pageNum), StringUtilsXD.checkPageSizeParam(pageSize));
            baseResp.setData(page);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("selectItemPage and item={},pageNum={},pageSize", JSON.toJSONString(item), pageNum, pageSize, e);
        }
        return baseResp;
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
    public BaseResp<Item> selectItemById(@RequestParam(value = "itemId") String itemId) {
        logger.info("selectItemById and itemId={}", itemId);
        BaseResp<Item> baseResp = new BaseResp<>();
        if (StringUtilsXD.isBlank(itemId)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            baseResp = itemService.selectItemById(itemId);
        } catch (Exception e) {
            logger.error("selectItemById and itemId={}", itemId, e);
        }
        return baseResp;
    }

    /**
     * @Description: 添加项目
     * @Title: insertItem
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "添加项目")
    @PostMapping(value = "/insertItem")
    public BaseResp<Item> insertItem(@RequestBody Item item) {
        logger.info("insertItem and item:{}", JSON.toJSONString(item));
        BaseResp<Item> baseResp = new BaseResp<>();
        try {
            baseResp = itemService.insertItem(item);
        } catch (Exception e) {
            logger.error("insertItem and item:{}", JSON.toJSONString(item), e);
        }
        return baseResp;
    }

    /**
     * @Description: 编辑项目
     * @title updateItemById
     * @author IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "编辑项目")
    @PostMapping(value = "/updateItemById")
    public BaseResp<Item> updateItemById(@RequestBody Item item) {
        logger.info("updateItemById and item:{}", JSON.toJSONString(item));
        BaseResp<Item> baseResp = new BaseResp<>();
        if (StringUtilsXD.isBlank(item.get_id())) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
        }
        try {
            baseResp = itemService.updateItemById(item);
        } catch (Exception e) {
            logger.error("updateItemById and item:{}", JSON.toJSONString(item), e);

        }
        return baseResp;
    }

    /**
     * @Description: 删除项目
     * @Title: deleteByIdBatch
     * @param ids 项目id集合
     * @auther IngaWu
     * @currentdate:2020年9月2日
     */
    @ApiOperation(value = "删除项目")
    @PostMapping(value = "/deleteByIdBatch")
    public BaseResp<Item> deleteByIdBatch(@RequestParam(value = "ids") String[] ids) {
        logger.info("deleteByIdBatch and ids={}", JSON.toJSONString(ids));
        BaseResp<Item> baseResp = new BaseResp<>();
        try {
            baseResp = itemService.deleteByIdBatch(ids);
        } catch (Exception e) {
            logger.error("deleteByIdBatch and ids={}", JSON.toJSONString(ids), e);
        }
        return baseResp;
    }
}
