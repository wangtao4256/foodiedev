package com.imooc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.imooc.enums.CommentLevel;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.FoodieException;
import com.imooc.mapper.*;
import com.imooc.pojo.*;
import com.imooc.pojo.vo.CommentLevelVO;
import com.imooc.pojo.vo.ItemCommentsVO;
import com.imooc.pojo.vo.PagingGridVO;
import com.imooc.pojo.vo.ShopCartVO;
import com.imooc.service.ItemService;
import com.imooc.utils.DesensitizationUtil;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
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

    @Autowired
    private UsersMapper usersMapper;

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

    @Override
    public ItemCommentsVO queryPagedComments(String itemId, Integer level, Integer pageNo, Integer pageSize) {
        Page page = new Page();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        QueryWrapper queryWrapper = new QueryWrapper<ItemsComments>();
        queryWrapper.eq(ItemsComments.ITEM_ID, itemId);
        queryWrapper.eq(null != level, ItemsComments.COMMENT_LEVEL, level);
        IPage pageResult = itemsCommentsMapper.selectPage(page, queryWrapper);
        List<ItemsComments> records = pageResult.getRecords();
        Set<String> userIds = records.stream().map(ItemsComments::getUserId).collect(Collectors.toSet());
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.in(!CollectionUtils.isEmpty(userIds), "id", userIds);
        List<Users> users = usersMapper.selectList(queryWrapper1);
        if (CollectionUtils.isEmpty(users)) {
            return new ItemCommentsVO();
        }
        ItemCommentsVO itemCommentsVO = new ItemCommentsVO();
        itemCommentsVO.setTotal((int) pageResult.getPages());
        itemCommentsVO.setRecords((int) pageResult.getTotal());
        List<ItemCommentsVO.ItemCommentsDTO> commentsList = Lists.newArrayList();
        Map<String, Users> userMap = users.stream().collect(Collectors.toMap(Users::getId, Function.identity(), (user1, user2) -> user1));
        for (ItemsComments record : records) {
            ItemCommentsVO.ItemCommentsDTO itemCommentsDTO = new ItemCommentsVO.ItemCommentsDTO();
            itemCommentsDTO.setCommentLevel(record.getCommentLevel());
            itemCommentsDTO.setContent(record.getContent());
            itemCommentsDTO.setCreatedTime(record.getCreatedTime());
            itemCommentsDTO.setSpecName(record.getSepcName());
            String userId = record.getUserId();
            Users users1 = userMap.get(userId);
            if (users1 == null) {
                continue;
            }
            itemCommentsDTO.setUserFace(users1.getFace());
            itemCommentsDTO.setNickname(DesensitizationUtil.commonDisplay(users1.getNickname()));
            commentsList.add(itemCommentsDTO);
        }
        itemCommentsVO.setCommentsList(commentsList);
        return itemCommentsVO;
    }

    @Override
    public PagingGridVO queryItem(String keyword, String sort, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("keyword", keyword);
        map.put("sort", sort);

        PageHelper.startPage(page, pageSize);
        return PageInfo2PagingGridResultConverter.convert(itemsMapper.selectItem(map), page);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagingGridVO queryItem(Integer categoryId, String sort, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryId", categoryId);
        map.put("sort", sort);

        PageHelper.startPage(page, pageSize);
        return PageInfo2PagingGridResultConverter.convert(itemsMapper.selectItemByThirdCategory(map), page);
    }

    @Override
    public List<ShopCartVO> queryItemsBySpecIds(String specIds) {
        String[] idArr = specIds.split(",");
        List<String> specIdList = com.google.common.collect.Lists.newArrayList(idArr);

        return itemsMapper.selectItemBySpecIdList(specIdList);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void decreaseItemSpecStock(String specId, Integer buyCount) {
        // 库存作为一种共享资源，需要控制
        // synchronized 不推荐使用，集群下无用，性能低下
        // 锁数据库：不推荐，导致数据库性能低下
        // 分布式锁zookeeper redis

        // lockUtil.getLock(); -- 加锁
        // lockUtil.unLock(); -- 解锁

        int result = itemsMapper.decreaseItemSpecStock(specId, buyCount);
        if (result != 1) {
            throw new FoodieException(ResultEnum.DECREASE_STOCK_FAIL);
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ItemsSpec queryItemSpecById(String specId) {
        return itemsSpecMapper.selectByPrimaryKey(specId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ItemsImg queryItemMainImageById(String itemId) {
        return itemsImgMapper.selectByItemIdIsMain(itemId);
    }
}
