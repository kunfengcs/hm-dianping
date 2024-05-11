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
import com.ruoyi.dianping_admin.domain.TbSign;
import com.ruoyi.dianping_admin.service.ITbSignService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 签到Controller
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
@RestController
@RequestMapping("/dianping_admin/sign")
public class TbSignController extends BaseController
{
    @Autowired
    private ITbSignService tbSignService;

    /**
     * 查询签到列表
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:sign:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbSign tbSign)
    {
        startPage();
        List<TbSign> list = tbSignService.selectTbSignList(tbSign);
        return getDataTable(list);
    }

    /**
     * 导出签到列表
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:sign:export')")
    @Log(title = "签到", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TbSign tbSign)
    {
        List<TbSign> list = tbSignService.selectTbSignList(tbSign);
        ExcelUtil<TbSign> util = new ExcelUtil<TbSign>(TbSign.class);
        util.exportExcel(response, list, "签到数据");
    }

    /**
     * 获取签到详细信息
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:sign:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tbSignService.selectTbSignById(id));
    }

    /**
     * 新增签到
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:sign:add')")
    @Log(title = "签到", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbSign tbSign)
    {
        return toAjax(tbSignService.insertTbSign(tbSign));
    }

    /**
     * 修改签到
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:sign:edit')")
    @Log(title = "签到", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbSign tbSign)
    {
        return toAjax(tbSignService.updateTbSign(tbSign));
    }

    /**
     * 删除签到
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:sign:remove')")
    @Log(title = "签到", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbSignService.deleteTbSignByIds(ids));
    }
}
