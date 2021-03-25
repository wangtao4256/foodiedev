package com.imooc.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class NewItemsVO {
    /**
     * 一级分类Id
     */
    private Integer rootCatId;

    /**
     * 一级分类名称
     */
    private String rootCatName;

    /**
     * 标语
     */
    private String slogan;

    /**
     * 分类图片
     */
    private String catImage;

    /**
     * 背景颜色
     */
    private String bgColor;

    /**
     * 6个商品
     */
    private List<SimpleItemVO> simpleItemList;

}
