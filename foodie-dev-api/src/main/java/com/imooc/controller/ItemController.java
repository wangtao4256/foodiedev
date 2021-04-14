package com.imooc.controller;

import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.CommentLevelVO;
import com.imooc.pojo.vo.ItemCommentsVO;
import com.imooc.pojo.vo.ItemInfoVO;
import com.imooc.pojo.vo.JSONVO;
import com.imooc.service.ItemService;
import com.imooc.utils.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @GetMapping("/info/{itemId}")
    public Results<ItemInfoVO> getItemInfo(@PathVariable("itemId") String itemId) {
        ItemInfoVO itemInfoVO = new ItemInfoVO();
        if (null == itemId) {
            return Results.error("题目id不能为空");
        }
        Items items = itemService.queryItemById(itemId);
        if (null == items) {
            return Results.success();
        }
        itemInfoVO.setItem(items);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);
        List<ItemsImg> itemsImgs = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemsSpecs = itemService.queryItemSpecList(itemId);
        itemInfoVO.setItemParams(itemsParam);
        itemInfoVO.setItemImgList(itemsImgs);
        itemInfoVO.setItemSpecList(itemsSpecs);
        return Results.success(itemInfoVO);
    }

    @GetMapping("/comments/{itemId}")
    public Results<ItemInfoVO> getItemInfo(@RequestParam("itemId") String itemId, @RequestParam("level") String level) {
        ItemInfoVO itemInfoVO = new ItemInfoVO();
        if (null == itemId) {
            return Results.error("题目id不能为空");
        }
        Items items = itemService.queryItemById(itemId);
        if (null == items) {
            return Results.success();
        }
        itemInfoVO.setItem(items);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);
        List<ItemsImg> itemsImgs = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemsSpecs = itemService.queryItemSpecList(itemId);
        itemInfoVO.setItemParams(itemsParam);
        itemInfoVO.setItemImgList(itemsImgs);
        itemInfoVO.setItemSpecList(itemsSpecs);
        return Results.success(itemInfoVO);
    }

    @GetMapping("/commentLevel")
    public Results<CommentLevelVO> getItemCommentLevel(@RequestParam("itemId") String itemId) {
        CommentLevelVO itemInfoVO = new CommentLevelVO();
        if (null == itemId) {
            return Results.error("题目id不能为空");
        }
        return Results.success(itemService.getItemCommentLevel(itemId));
    }

    /**
     * 商品评论
     */
    @GetMapping("/comments")
    public Results<ItemCommentsVO> comment(
            @RequestParam String itemId,
            @RequestParam Integer level,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        if (StringUtils.isEmpty(itemId)) {
            return Results.error("题目不能为空");
        }
        return Results.success(itemService.queryPagedComments(itemId, level, page, pageSize));
    }

    /**
     * 商品列表
     */
    @GetMapping("/search")
    public JSONVO search(
            @RequestParam(name = "keywords") String keyword,
            @RequestParam String sort,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        if (StringUtils.isEmpty(keyword)) {
            return JSONVO.errorMsg(null);
        }

        return JSONVO.ok(itemService.queryItem(keyword, sort, page, pageSize));
    }

    /**
     * 分类的商品列表
     */
    @GetMapping("/catItems")
    public JSONVO categoryItem(
            @RequestParam(name = "catId") Integer categoryId,
            @RequestParam String sort,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        return JSONVO.ok(itemService.queryItem(categoryId, sort, page, pageSize));
    }

    /**
     * 刷新购物车
     */
    @GetMapping("/refresh")
    public JSONVO refresh(
            @RequestParam String itemSpecIds) {

        if (StringUtils.isEmpty(itemSpecIds)) {
            return JSONVO.ok();
        }

        return JSONVO.ok(itemService.queryItemsBySpecIds(itemSpecIds));
    }
}
