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
import com.ruoyi.dianping_admin.domain.TbSeckillVoucher;
import com.ruoyi.dianping_admin.service.ITbSeckillVoucherService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 秒杀优惠券，与优惠券是一对一关系Controller
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
@RestController
@RequestMapping("/dianping_admin/seckill_voucher")
public class TbSeckillVoucherController extends BaseController
{
    @Autowired
    private ITbSeckillVoucherService tbSeckillVoucherService;

    /**
     * 查询秒杀优惠券，与优惠券是一对一关系列表
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:voucher:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbSeckillVoucher tbSeckillVoucher)
    {
        startPage();
        List<TbSeckillVoucher> list = tbSeckillVoucherService.selectTbSeckillVoucherList(tbSeckillVoucher);
        return getDataTable(list);
    }

    /**
     * 导出秒杀优惠券，与优惠券是一对一关系列表
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:voucher:export')")
    @Log(title = "秒杀优惠券，与优惠券是一对一关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TbSeckillVoucher tbSeckillVoucher)
    {
        List<TbSeckillVoucher> list = tbSeckillVoucherService.selectTbSeckillVoucherList(tbSeckillVoucher);
        ExcelUtil<TbSeckillVoucher> util = new ExcelUtil<TbSeckillVoucher>(TbSeckillVoucher.class);
        util.exportExcel(response, list, "秒杀优惠券，与优惠券是一对一关系数据");
    }

    /**
     * 获取秒杀优惠券，与优惠券是一对一关系详细信息
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:voucher:query')")
    @GetMapping(value = "/{voucherId}")
    public AjaxResult getInfo(@PathVariable("voucherId") Long voucherId)
    {
        return success(tbSeckillVoucherService.selectTbSeckillVoucherByVoucherId(voucherId));
    }

    /**
     * 新增秒杀优惠券，与优惠券是一对一关系
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:voucher:add')")
    @Log(title = "秒杀优惠券，与优惠券是一对一关系", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbSeckillVoucher tbSeckillVoucher)
    {
        return toAjax(tbSeckillVoucherService.insertTbSeckillVoucher(tbSeckillVoucher));
    }

    /**
     * 修改秒杀优惠券，与优惠券是一对一关系
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:voucher:edit')")
    @Log(title = "秒杀优惠券，与优惠券是一对一关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbSeckillVoucher tbSeckillVoucher)
    {
        return toAjax(tbSeckillVoucherService.updateTbSeckillVoucher(tbSeckillVoucher));
    }

    /**
     * 删除秒杀优惠券，与优惠券是一对一关系
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:voucher:remove')")
    @Log(title = "秒杀优惠券，与优惠券是一对一关系", businessType = BusinessType.DELETE)
	@DeleteMapping("/{voucherIds}")
    public AjaxResult remove(@PathVariable Long[] voucherIds)
    {
        return toAjax(tbSeckillVoucherService.deleteTbSeckillVoucherByVoucherIds(voucherIds));
    }
}
