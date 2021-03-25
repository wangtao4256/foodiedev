package com.imooc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.enums.CommentLevel;
import com.imooc.mapper.*;
import com.imooc.pojo.*;
import com.imooc.pojo.vo.CommentLevelVO;
import com.imooc.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemsMapper itemsMapper;

    @Autowired
    private ItemsImgMapper itemsImgMapper;

    @Autowired
    private ItemsSpecMapper itemsSpecMapper;

    @Autowired
    private ItemsParamMapper itemsParamMapper;
    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;

    @Override
    public Items queryItemById(String id) {
        return itemsMapper.selectById(id);
    }

    @Override
    public List<ItemsImg> queryItemImgList(String itemId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(ItemsImg.ITEM_ID, itemId);
        return itemsImgMapper.selectList(queryWrapper);
    }

    @Override
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(ItemsImg.ITEM_ID, itemId);
        return itemsSpecMapper.selectList(queryWrapper);
    }

    @Override
    public ItemsParam queryItemParam(String itemId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(ItemsImg.ITEM_ID, itemId);
        return itemsParamMapper.selectOne(queryWrapper);
    }

    @Override
    public CommentLevelVO getItemCommentLevel(String itemId) {
        CommentLevelVO commentLevelVO = new CommentLevelVO();
        List<ItemsComments> commentLevel = itemsCommentsMapper.getCommentLevel(itemId);
        if (CollectionUtils.isEmpty(commentLevel)) {
            return new CommentLevelVO();
        }
        Integer totalCount = 0;
        Map<Integer, List<ItemsComments>> commentLevelMap = commentLevel.stream().collect(Collectors.groupingBy(ItemsComments::getCommentLevel));
        for (Map.Entry<Integer, List<ItemsComments>> integerListEntry : commentLevelMap.entrySet()) {
            Integer key = integerListEntry.getKey();
            List<ItemsComments> value = integerListEntry.getValue();
            Integer commentCount = value.size();
            if (key.equals(CommentLevel.GOOD.getCode())) {
                commentLevelVO.setGoodCounts(commentCount);
                totalCount += commentCount;
            } else if (key.equals(CommentLevel.NORMAL.getCode())) {
                commentLevelVO.setNormalCounts(commentCount);
                totalCount += commentCount;
            } else if (key.equals(CommentLevel.BAD.getCode())) {
                commentLevelVO.setBadCounts(commentCount);
                totalCount += commentCount;
            }
        }
        commentLevelVO.setTotalCounts(totalCount);
        return commentLevelVO;
    }


}
