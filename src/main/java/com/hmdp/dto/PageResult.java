package com.hmdp.dto;

import com.hmdp.entity.Shop;
import lombok.Data;

import java.util.List;

@Data
public class PageResult {
    private Long total;
    private List<Shop> shops;

    public PageResult() {
    }

    public PageResult(Long total, List<Shop> shops) {
        this.total = total;
        this.shops = shops;
    }
}
