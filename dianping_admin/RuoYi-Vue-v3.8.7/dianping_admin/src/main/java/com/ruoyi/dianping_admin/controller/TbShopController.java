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
import com.ruoyi.dianping_admin.domain.TbShop;
import com.ruoyi.dianping_admin.service.ITbShopService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商户信息Controller
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
@RestController
@RequestMapping("/dianping_admin/shop")
public class TbShopController extends BaseController
{
    @Autowired
    private ITbShopService tbShopService;

    /**
     * 查询商户信息列表
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:shop:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbShop tbShop)
    {
        startPage();
        List<TbShop> list = tbShopService.selectTbShopList(tbShop);
        return getDataTable(list);
    }

    /**
     * 导出商户信息列表
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:shop:export')")
    @Log(title = "商户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TbShop tbShop)
    {
        List<TbShop> list = tbShopService.selectTbShopList(tbShop);
        ExcelUtil<TbShop> util = new ExcelUtil<TbShop>(TbShop.class);
        util.exportExcel(response, list, "商户信息数据");
    }

    /**
     * 获取商户信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:shop:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tbShopService.selectTbShopById(id));
    }

    /**
     * 新增商户信息
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:shop:add')")
    @Log(title = "商户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbShop tbShop)
    {
        return toAjax(tbShopService.insertTbShop(tbShop));
    }

    /**
     * 修改商户信息
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:shop:edit')")
    @Log(title = "商户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbShop tbShop)
    {
        return toAjax(tbShopService.updateTbShop(tbShop));
    }

    /**
     * 删除商户信息
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:shop:remove')")
    @Log(title = "商户信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbShopService.deleteTbShopByIds(ids));
    }
}
