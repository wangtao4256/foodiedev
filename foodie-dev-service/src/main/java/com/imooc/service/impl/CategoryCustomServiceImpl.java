package com.imooc.service.impl;

import com.imooc.mapper.CategoryCustomMapper;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.service.CategoryCustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryCustomServiceImpl implements CategoryCustomService {
    @Autowired
    private CategoryCustomMapper categoryCustomMapper;

    @Override
    public List<CategoryVO> getSubCategoryByFatherId(Integer rootCatId) {
        return categoryCustomMapper.getSubCategoryByFatherId(rootCatId);
    }
}
