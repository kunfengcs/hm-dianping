package com.ruoyi.dianping_admin.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.dianping_admin.mapper.TbSignMapper;
import com.ruoyi.dianping_admin.domain.TbSign;
import com.ruoyi.dianping_admin.service.ITbSignService;

/**
 * 签到Service业务层处理
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
@Service
public class TbSignServiceImpl implements ITbSignService 
{
    @Autowired
    private TbSignMapper tbSignMapper;

    /**
     * 查询签到
     * 
     * @param id 签到主键
     * @return 签到
     */
    @Override
    public TbSign selectTbSignById(Long id)
    {
        return tbSignMapper.selectTbSignById(id);
    }

    /**
     * 查询签到列表
     * 
     * @param tbSign 签到
     * @return 签到
     */
    @Override
    public List<TbSign> selectTbSignList(TbSign tbSign)
    {
        return tbSignMapper.selectTbSignList(tbSign);
    }

    /**
     * 新增签到
     * 
     * @param tbSign 签到
     * @return 结果
     */
    @Override
    public int insertTbSign(TbSign tbSign)
    {
        return tbSignMapper.insertTbSign(tbSign);
    }

    /**
     * 修改签到
     * 
     * @param tbSign 签到
     * @return 结果
     */
    @Override
    public int updateTbSign(TbSign tbSign)
    {
        return tbSignMapper.updateTbSign(tbSign);
    }

    /**
     * 批量删除签到
     * 
     * @param ids 需要删除的签到主键
     * @return 结果
     */
    @Override
    public int deleteTbSignByIds(Long[] ids)
    {
        return tbSignMapper.deleteTbSignByIds(ids);
    }

    /**
     * 删除签到信息
     * 
     * @param id 签到主键
     * @return 结果
     */
    @Override
    public int deleteTbSignById(Long id)
    {
        return tbSignMapper.deleteTbSignById(id);
    }
}
