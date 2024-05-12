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
import com.ruoyi.dianping_admin.domain.TbVoucherOrder;
import com.ruoyi.dianping_admin.service.ITbVoucherOrderService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 优惠券的订单Controller
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
@RestController
@RequestMapping("/dianping_admin/order")
public class TbVoucherOrderController extends BaseController
{
    @Autowired
    private ITbVoucherOrderService tbVoucherOrderService;

    /**
     * 查询优惠券的订单列表
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbVoucherOrder tbVoucherOrder)
    {
        startPage();
        List<TbVoucherOrder> list = tbVoucherOrderService.selectTbVoucherOrderList(tbVoucherOrder);
        return getDataTable(list);
    }

    /**
     * 导出优惠券的订单列表
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:order:export')")
    @Log(title = "优惠券的订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TbVoucherOrder tbVoucherOrder)
    {
        List<TbVoucherOrder> list = tbVoucherOrderService.selectTbVoucherOrderList(tbVoucherOrder);
        ExcelUtil<TbVoucherOrder> util = new ExcelUtil<TbVoucherOrder>(TbVoucherOrder.class);
        util.exportExcel(response, list, "优惠券的订单数据");
    }

    /**
     * 获取优惠券的订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:order:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tbVoucherOrderService.selectTbVoucherOrderById(id));
    }

    /**
     * 新增优惠券的订单
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:order:add')")
    @Log(title = "优惠券的订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbVoucherOrder tbVoucherOrder)
    {
        return toAjax(tbVoucherOrderService.insertTbVoucherOrder(tbVoucherOrder));
    }

    /**
     * 修改优惠券的订单
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:order:edit')")
    @Log(title = "优惠券的订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbVoucherOrder tbVoucherOrder)
    {
        return toAjax(tbVoucherOrderService.updateTbVoucherOrder(tbVoucherOrder));
    }

    /**
     * 删除优惠券的订单
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:order:remove')")
    @Log(title = "优惠券的订单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbVoucherOrderService.deleteTbVoucherOrderByIds(ids));
    }

    /**
     * 优惠券的订单总数量
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:order:list')")
    @Log(title = "优惠券的订单", businessType = BusinessType.OTHER)
    @GetMapping("/count")
    public AjaxResult count()
    {
        return AjaxResult.success(tbVoucherOrderService.count());
    }
}
