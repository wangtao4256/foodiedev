package com.imooc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.pojo.ItemsComments;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ItemsCommentsMapper extends BaseMapper<ItemsComments> {

    @Select("select item_id,comment_level from items_comments where item_id=\"${itemId}\"")
    List<ItemsComments> getCommentLevel(@Param("itemId") String itemId);

}
