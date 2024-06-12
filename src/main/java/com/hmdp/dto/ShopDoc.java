package com.hmdp.dto;

import com.hmdp.entity.Shop;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data

public class ShopDoc {


    private Long id;

    /**
     * 商铺名称
     */
    private String name;

    /**
     * 商铺类型的id
     */
    private Long typeId;

    /**
     * 商铺图片，多个图片以','隔开
     */
    private String images;

    /**
     * 商圈，例如陆家嘴
     */
    private String area;

    /**
     * 地址
     */
    private String address;

    private String location;

    private List<String> suggestion;//分词


    /**
     * 均价，取整数
     */
    private Long avgPrice;

    /**
     * 销量
     */
    private Integer sold;

    /**
     * 评论数量
     */
    private Integer comments;

    /**
     * 评分，1~5分，乘10保存，避免小数
     */
    private Integer score;

    /**
     * 营业时间，例如 10:00-22:00
     */
    private String openHours;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


    public ShopDoc() {
    }

    public ShopDoc(Shop shop) {
        this.id = shop.getId();
        this.name = shop.getName();
        this.typeId = shop.getTypeId();
        this.images = shop.getImages();
        this.area = shop.getArea();
        this.address = shop.getAddress();
        this.location = shop.getY() + "," + shop.getX();
        this.avgPrice = shop.getAvgPrice();
        this.sold = shop.getSold();
        this.comments = shop.getComments();
        this.score = shop.getScore();
        this.openHours = shop.getOpenHours();
        this.createTime = shop.getCreateTime();
        this.updateTime = shop.getUpdateTime();

        if (this.area.contains("/")) {
            // business有多个值，需要切割
            String[] arr = this.area.split("/");
            // 添加元素
            this.suggestion = new ArrayList<>();
            this.suggestion.add(this.name);
            this.suggestion.addAll(Arrays.asList(arr));
        } else {
            this.suggestion = Arrays.asList(this.name, this.area);
        }

    }
}
