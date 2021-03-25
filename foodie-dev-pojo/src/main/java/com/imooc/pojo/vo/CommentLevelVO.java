package com.imooc.pojo.vo;

import lombok.Data;

@Data
public class CommentLevelVO {
    private Integer totalCounts;
    private Integer goodCounts;
    private Integer normalCounts;
    private Integer badCounts;
}
