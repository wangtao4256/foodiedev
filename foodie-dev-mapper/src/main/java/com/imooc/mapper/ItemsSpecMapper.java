package com.imooc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.pojo.ItemsSpec;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ItemsSpecMapper extends BaseMapper<ItemsSpec> {
    int deleteByPrimaryKey(String id);

    int insert(ItemsSpec record);

    int insertSelective(ItemsSpec record);

    ItemsSpec selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ItemsSpec record);

    int updateByPrimaryKey(ItemsSpec record);

    /**
     * 根据商品Id查询商品规格
     *
     * @param itemId 商品Id
     * @return 商品规格
     */
    List<ItemsSpec> selectByItemId(String itemId);
}