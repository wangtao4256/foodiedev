package com.imooc.controller;

import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.CommentLevelVO;
import com.imooc.pojo.vo.ItemInfoVO;
import com.imooc.service.ItemService;
import com.imooc.utils.Results;
import org.springframework.beans.factory.annotation.Autowired;
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
}
