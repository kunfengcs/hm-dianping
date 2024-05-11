package com.ruoyi.dianping_admin.mapper;

import java.util.List;
import com.ruoyi.dianping_admin.domain.TbShop;

/**
 * 商户信息Mapper接口
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
public interface TbShopMapper 
{
    /**
     * 查询商户信息
     * 
     * @param id 商户信息主键
     * @return 商户信息
     */
    public TbShop selectTbShopById(Long id);

    /**
     * 查询商户信息列表
     * 
     * @param tbShop 商户信息
     * @return 商户信息集合
     */
    public List<TbShop> selectTbShopList(TbShop tbShop);

    /**
     * 新增商户信息
     * 
     * @param tbShop 商户信息
     * @return 结果
     */
    public int insertTbShop(TbShop tbShop);

    /**
     * 修改商户信息
     * 
     * @param tbShop 商户信息
     * @return 结果
     */
    public int updateTbShop(TbShop tbShop);

    /**
     * 删除商户信息
     * 
     * @param id 商户信息主键
     * @return 结果
     */
    public int deleteTbShopById(Long id);

    /**
     * 批量删除商户信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbShopByIds(Long[] ids);
}
