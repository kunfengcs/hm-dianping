package com.ruoyi.dianping_admin.service;

import java.util.List;
import com.ruoyi.dianping_admin.domain.TbShopType;

/**
 * 商户类型Service接口
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
public interface ITbShopTypeService 
{
    /**
     * 查询商户类型
     * 
     * @param id 商户类型主键
     * @return 商户类型
     */
    public TbShopType selectTbShopTypeById(Long id);

    /**
     * 查询商户类型列表
     * 
     * @param tbShopType 商户类型
     * @return 商户类型集合
     */
    public List<TbShopType> selectTbShopTypeList(TbShopType tbShopType);

    /**
     * 新增商户类型
     * 
     * @param tbShopType 商户类型
     * @return 结果
     */
    public int insertTbShopType(TbShopType tbShopType);

    /**
     * 修改商户类型
     * 
     * @param tbShopType 商户类型
     * @return 结果
     */
    public int updateTbShopType(TbShopType tbShopType);

    /**
     * 批量删除商户类型
     * 
     * @param ids 需要删除的商户类型主键集合
     * @return 结果
     */
    public int deleteTbShopTypeByIds(Long[] ids);

    /**
     * 删除商户类型信息
     * 
     * @param id 商户类型主键
     * @return 结果
     */
    public int deleteTbShopTypeById(Long id);
}
