package com.imooc.service;

import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.CommentLevelVO;
import com.imooc.pojo.vo.ItemCommentsVO;
import com.imooc.pojo.vo.PagingGridVO;
import com.imooc.pojo.vo.ShopCartVO;

import java.util.List;

public interface ItemService {
    Items queryItemById(String id);

    List<ItemsImg> queryItemImgList(String itemId);

    List<ItemsSpec> queryItemSpecList(String itemId);

    ItemsParam queryItemParam(String itemId);

    CommentLevelVO getItemCommentLevel(String itemId);

    ItemCommentsVO queryPagedComments(String itemId, Integer level, Integer pageNo, Integer pageSize);

    /**
     * 搜索商品列表
     *
     * @param keyword  关键字
     * @param sort     排序字段
     * @param page     当前页
     * @param pageSize 每页显示的记录数
     */
    PagingGridVO queryItem(String keyword, String sort, Integer page, Integer pageSize);

    /**
     * 搜索商品列表
     */
    PagingGridVO queryItem(Integer categoryId, String sort, Integer page, Integer pageSize);

    /**
     * 根据规格ids查询最新的购物车中的商品数据（用于刷新渲染购物车中的商品数据）
     */
    List<ShopCartVO> queryItemsBySpecIds(String specIds);

    /**
     * 减少库存
     *
     * @param specId   商品规格Id
     * @param buyCount 购买数量
     */
    void decreaseItemSpecStock(String specId, Integer buyCount);

    /**
     * 根据商品规格Id，获取规格对象的具体信息
     *
     * @param specId 商品规格Id
     * @return 规格对象
     */
    ItemsSpec queryItemSpecById(String specId);

    /**
     * 根据商品Id，获得商品图片主图url
     *
     * @param itemId 商品Id
     * @return 商品图片主图url
     */
    ItemsImg queryItemMainImageById(String itemId);
}
