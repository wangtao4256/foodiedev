package com.imooc.controller;

import com.imooc.enums.CarouselShowEnum;
import com.imooc.enums.CategoryTypeEnum;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.NewItemsVO;
import com.imooc.service.CarouseService;
import com.imooc.service.CategoryCustomService;
import com.imooc.service.CategoryService;
import com.imooc.utils.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/index")
public class IndexController {
    @Autowired
    private CarouseService carouseService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryCustomService categoryCustomService;

    @GetMapping("carousel")
    public Results getCarousel() {
        List<Carousel> carousels = carouseService.queryAllCarouselByIsShow(CarouselShowEnum.SHOW.getCode());
        return Results.success(carousels);
    }


    @GetMapping("cats")
    public Results getFirstLevelCategory() {
        List<Category> firstLevelCategory = categoryService.getFirstLevelCategory(CategoryTypeEnum.FirstLevel.getCode());
        return Results.success(firstLevelCategory);
    }

    @GetMapping("subCat/{id}")
    public Results getFirstLevelCategory(@PathVariable("id") Integer rootCatId) {
        List<CategoryVO> categories = categoryCustomService.getSubCategoryByFatherId(rootCatId);
        return Results.success(categories);
    }

    @GetMapping("sixNewItems/{rootCatId}")
    public Results sixNewItems(@PathVariable("rootCatId") Integer rootCatId) {
        if (null == rootCatId) {
            return Results.error("分类不存在");
        }
        List<NewItemsVO> sixNewItemsLazy = categoryService.getSixNewItemsLazy(rootCatId);
        return Results.success(sixNewItemsLazy);
    }


}
