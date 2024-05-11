package com.ruoyi.dianping_admin.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.dianping_admin.mapper.TbShopTypeMapper;
import com.ruoyi.dianping_admin.domain.TbShopType;
import com.ruoyi.dianping_admin.service.ITbShopTypeService;

/**
 * 商户类型Service业务层处理
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
@Service
public class TbShopTypeServiceImpl implements ITbShopTypeService 
{
    @Autowired
    private TbShopTypeMapper tbShopTypeMapper;

    /**
     * 查询商户类型
     * 
     * @param id 商户类型主键
     * @return 商户类型
     */
    @Override
    public TbShopType selectTbShopTypeById(Long id)
    {
        return tbShopTypeMapper.selectTbShopTypeById(id);
    }

    /**
     * 查询商户类型列表
     * 
     * @param tbShopType 商户类型
     * @return 商户类型
     */
    @Override
    public List<TbShopType> selectTbShopTypeList(TbShopType tbShopType)
    {
        return tbShopTypeMapper.selectTbShopTypeList(tbShopType);
    }

    /**
     * 新增商户类型
     * 
     * @param tbShopType 商户类型
     * @return 结果
     */
    @Override
    public int insertTbShopType(TbShopType tbShopType)
    {
        tbShopType.setCreateTime(DateUtils.getNowDate());
        return tbShopTypeMapper.insertTbShopType(tbShopType);
    }

    /**
     * 修改商户类型
     * 
     * @param tbShopType 商户类型
     * @return 结果
     */
    @Override
    public int updateTbShopType(TbShopType tbShopType)
    {
        tbShopType.setUpdateTime(DateUtils.getNowDate());
        return tbShopTypeMapper.updateTbShopType(tbShopType);
    }

    /**
     * 批量删除商户类型
     * 
     * @param ids 需要删除的商户类型主键
     * @return 结果
     */
    @Override
    public int deleteTbShopTypeByIds(Long[] ids)
    {
        return tbShopTypeMapper.deleteTbShopTypeByIds(ids);
    }

    /**
     * 删除商户类型信息
     * 
     * @param id 商户类型主键
     * @return 结果
     */
    @Override
    public int deleteTbShopTypeById(Long id)
    {
        return tbShopTypeMapper.deleteTbShopTypeById(id);
    }
}
