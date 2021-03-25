package com.imooc.service;

import com.imooc.pojo.Category;
import com.imooc.pojo.vo.NewItemsVO;

import java.util.List;

public interface CategoryService {

    List<Category> getFirstLevelCategory(Integer type);

    List<Category> getCategoryByCatId(Integer catId);

    List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId);
}
