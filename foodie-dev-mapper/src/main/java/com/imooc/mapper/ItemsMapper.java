package com.imooc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.pojo.Items;
import com.imooc.pojo.vo.SearchItemsVO;
import com.imooc.pojo.vo.ShopCartVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ItemsMapper extends BaseMapper<Items> {
    /**
     * 查询商品列表
     */
    List<SearchItemsVO> selectItem(@Param("paramsMap") Map<String, Object> map);

    /**
     * 通过三级分类，查询商品列表
     */
    List<SearchItemsVO> selectItemByThirdCategory(@Param("paramsMap") Map<String, Object> map);

    /**
     * 根据规格ids查询最新的购物车中的商品数据（用于刷新渲染购物车中的商品数据）
     */
    List<ShopCartVO> selectItemBySpecIdList(@Param("specIdList") List<String> specIdList);

    /**
     * 减库存
     */
    int decreaseItemSpecStock(String specId, Integer pendingCount);
}