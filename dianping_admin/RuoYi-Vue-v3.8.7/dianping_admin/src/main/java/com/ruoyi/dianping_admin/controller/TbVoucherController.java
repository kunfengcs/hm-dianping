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
import com.ruoyi.dianping_admin.domain.TbVoucher;
import com.ruoyi.dianping_admin.service.ITbVoucherService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 优惠券Controller
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
@RestController
@RequestMapping("/dianping_admin/voucher")
public class TbVoucherController extends BaseController
{
    @Autowired
    private ITbVoucherService tbVoucherService;

    /**
     * 查询优惠券列表
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:voucher:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbVoucher tbVoucher)
    {
        startPage();
        List<TbVoucher> list = tbVoucherService.selectTbVoucherList(tbVoucher);
        return getDataTable(list);
    }

    /**
     * 导出优惠券列表
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:voucher:export')")
    @Log(title = "优惠券", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TbVoucher tbVoucher)
    {
        List<TbVoucher> list = tbVoucherService.selectTbVoucherList(tbVoucher);
        ExcelUtil<TbVoucher> util = new ExcelUtil<TbVoucher>(TbVoucher.class);
        util.exportExcel(response, list, "优惠券数据");
    }

    /**
     * 获取优惠券详细信息
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:voucher:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tbVoucherService.selectTbVoucherById(id));
    }

    /**
     * 新增优惠券
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:voucher:add')")
    @Log(title = "优惠券", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbVoucher tbVoucher)
    {
        return toAjax(tbVoucherService.insertTbVoucher(tbVoucher));
    }

    /**
     * 修改优惠券
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:voucher:edit')")
    @Log(title = "优惠券", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbVoucher tbVoucher)
    {
        return toAjax(tbVoucherService.updateTbVoucher(tbVoucher));
    }

    /**
     * 删除优惠券
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:voucher:remove')")
    @Log(title = "优惠券", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbVoucherService.deleteTbVoucherByIds(ids));
    }
}
