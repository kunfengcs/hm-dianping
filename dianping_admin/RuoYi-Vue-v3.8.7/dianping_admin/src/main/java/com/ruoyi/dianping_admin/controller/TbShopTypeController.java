package com.ruoyi.dianping_admin.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.dianping_admin.domain.TbShopType;
import com.ruoyi.dianping_admin.service.ITbShopTypeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商户类型Controller
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
@RestController
@RequestMapping("/dianping_admin/type")
public class TbShopTypeController extends BaseController
{
    @Autowired
    private ITbShopTypeService tbShopTypeService;

    /**
     * 查询商户类型列表
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:type:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbShopType tbShopType)
    {
        startPage();
        List<TbShopType> list = tbShopTypeService.selectTbShopTypeList(tbShopType);
        return getDataTable(list);
    }

    /**
     * 导出商户类型列表
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:type:export')")
    @Log(title = "商户类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TbShopType tbShopType)
    {
        List<TbShopType> list = tbShopTypeService.selectTbShopTypeList(tbShopType);
        ExcelUtil<TbShopType> util = new ExcelUtil<TbShopType>(TbShopType.class);
        util.exportExcel(response, list, "商户类型数据");
    }

    /**
     * 获取商户类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:type:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tbShopTypeService.selectTbShopTypeById(id));
    }

    /**
     * 新增商户类型
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:type:add')")
    @Log(title = "商户类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbShopType tbShopType)
    {
        return toAjax(tbShopTypeService.insertTbShopType(tbShopType));
    }

    /**
     * 修改商户类型
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:type:edit')")
    @Log(title = "商户类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbShopType tbShopType)
    {
        return toAjax(tbShopTypeService.updateTbShopType(tbShopType));
    }

    /**
     * 删除商户类型
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:type:remove')")
    @Log(title = "商户类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbShopTypeService.deleteTbShopTypeByIds(ids));
    }
}
