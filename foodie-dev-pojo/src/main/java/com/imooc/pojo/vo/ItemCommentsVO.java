package com.imooc.pojo.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ItemCommentsVO {
    //总页数
    private Integer total;
    //总记录数
    private Integer records;
    List<ItemCommentsDTO> commentsList;

    @Data
    public static class ItemCommentsDTO {

        private Integer commentLevel;
        private String content;
        private String specName;
        private Date createdTime;
        private String userFace;
        private String nickname;
    }
}
