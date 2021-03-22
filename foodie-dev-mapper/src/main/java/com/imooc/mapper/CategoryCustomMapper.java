package com.imooc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.pojo.vo.CategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryCustomMapper extends BaseMapper {
    List<CategoryVO> getSubCategoryByFatherId(@Param(value = "rootCatId") Integer rootCatId);
}
