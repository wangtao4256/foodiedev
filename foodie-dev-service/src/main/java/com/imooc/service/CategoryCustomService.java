package com.imooc.service;

import com.imooc.pojo.vo.CategoryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryCustomService {

    List<CategoryVO> getSubCategoryByFatherId(Integer rootCatId);

}
