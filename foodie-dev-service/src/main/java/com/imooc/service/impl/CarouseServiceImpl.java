package com.imooc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.mapper.CarouselMapper;
import com.imooc.pojo.Carousel;
import com.imooc.service.CarouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.List;

@Service
public class CarouseServiceImpl implements CarouseService {
    @Autowired
    private CarouselMapper carouselMapper;

    @Override
    public List<Carousel> queryAllCarouselByIsShow(Integer isShow) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(Carousel.SHOW, isShow);
        queryWrapper.orderByAsc(Carousel.SORT);
        return carouselMapper.selectList(queryWrapper);

    }
}
