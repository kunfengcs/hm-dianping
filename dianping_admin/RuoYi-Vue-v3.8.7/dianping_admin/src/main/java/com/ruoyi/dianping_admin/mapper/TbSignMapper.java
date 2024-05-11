package com.ruoyi.dianping_admin.mapper;

import java.util.List;
import com.ruoyi.dianping_admin.domain.TbSign;

/**
 * 签到Mapper接口
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
public interface TbSignMapper 
{
    /**
     * 查询签到
     * 
     * @param id 签到主键
     * @return 签到
     */
    public TbSign selectTbSignById(Long id);

    /**
     * 查询签到列表
     * 
     * @param tbSign 签到
     * @return 签到集合
     */
    public List<TbSign> selectTbSignList(TbSign tbSign);

    /**
     * 新增签到
     * 
     * @param tbSign 签到
     * @return 结果
     */
    public int insertTbSign(TbSign tbSign);

    /**
     * 修改签到
     * 
     * @param tbSign 签到
     * @return 结果
     */
    public int updateTbSign(TbSign tbSign);

    /**
     * 删除签到
     * 
     * @param id 签到主键
     * @return 结果
     */
    public int deleteTbSignById(Long id);

    /**
     * 批量删除签到
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbSignByIds(Long[] ids);
}
