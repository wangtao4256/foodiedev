package com.imooc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.imooc.mapper.CategoryCustomMapper;
import com.imooc.mapper.CategoryMapper;
import com.imooc.pojo.Category;
import com.imooc.pojo.vo.NewItemsVO;
import com.imooc.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryCustomMapper categoryCustomMapper;
    @Override
    public List<Category> getFirstLevelCategory(Integer type) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(Category.TYPE, type);
        return categoryMapper.selectList(queryWrapper);
    }

    @Override
    public List<Category> getCategoryByCatId(Integer catId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(Category.FATHERID, catId);
        return categoryMapper.selectList(queryWrapper);
    }

    @Override
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId) {
        Map<String,Object> paramMap= Maps.newHashMap();
        paramMap.put("rootCatId",rootCatId);
        return categoryCustomMapper.getSixNewItemsLazy(paramMap);
    }
}
