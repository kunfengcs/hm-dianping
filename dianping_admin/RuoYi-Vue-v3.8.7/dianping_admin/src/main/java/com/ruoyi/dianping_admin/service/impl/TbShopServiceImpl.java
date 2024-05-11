package com.ruoyi.dianping_admin.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.dianping_admin.mapper.TbShopMapper;
import com.ruoyi.dianping_admin.domain.TbShop;
import com.ruoyi.dianping_admin.service.ITbShopService;

/**
 * 商户信息Service业务层处理
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
@Service
public class TbShopServiceImpl implements ITbShopService 
{
    @Autowired
    private TbShopMapper tbShopMapper;

    /**
     * 查询商户信息
     * 
     * @param id 商户信息主键
     * @return 商户信息
     */
    @Override
    public TbShop selectTbShopById(Long id)
    {
        return tbShopMapper.selectTbShopById(id);
    }

    /**
     * 查询商户信息列表
     * 
     * @param tbShop 商户信息
     * @return 商户信息
     */
    @Override
    public List<TbShop> selectTbShopList(TbShop tbShop)
    {
        return tbShopMapper.selectTbShopList(tbShop);
    }

    /**
     * 新增商户信息
     * 
     * @param tbShop 商户信息
     * @return 结果
     */
    @Override
    public int insertTbShop(TbShop tbShop)
    {
        tbShop.setCreateTime(DateUtils.getNowDate());
        return tbShopMapper.insertTbShop(tbShop);
    }

    /**
     * 修改商户信息
     * 
     * @param tbShop 商户信息
     * @return 结果
     */
    @Override
    public int updateTbShop(TbShop tbShop)
    {
        tbShop.setUpdateTime(DateUtils.getNowDate());
        return tbShopMapper.updateTbShop(tbShop);
    }

    /**
     * 批量删除商户信息
     * 
     * @param ids 需要删除的商户信息主键
     * @return 结果
     */
    @Override
    public int deleteTbShopByIds(Long[] ids)
    {
        return tbShopMapper.deleteTbShopByIds(ids);
    }

    /**
     * 删除商户信息信息
     * 
     * @param id 商户信息主键
     * @return 结果
     */
    @Override
    public int deleteTbShopById(Long id)
    {
        return tbShopMapper.deleteTbShopById(id);
    }
}
