package com.imooc.service;

import com.imooc.pojo.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getFirstLevelCategory(Integer type);

    List<Category> getCategoryByCatId(Integer catId);

    List<Category> getSixNewItemsLazy(Integer rootCatId);
}
