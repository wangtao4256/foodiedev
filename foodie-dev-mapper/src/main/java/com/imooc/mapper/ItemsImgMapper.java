package com.imooc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.pojo.ItemsImg;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ItemsImgMapper extends BaseMapper<ItemsImg> {
    int deleteByPrimaryKey(String id);

    int insert(ItemsImg record);

    int insertSelective(ItemsImg record);

    ItemsImg selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ItemsImg record);

    int updateByPrimaryKey(ItemsImg record);

    /**
     * 根据商品Id查询商品图片列表
     *
     * @param itemId 商品Id
     * @return 商品图片
     */
    List<ItemsImg> selectByItemId(String itemId);

    /**
     * 根据商品Id，获得商品图片主图url
     *
     * @param itemId 商品Id
     * @return 商品图片主图url
     */
    ItemsImg selectByItemIdIsMain(String itemId);

}