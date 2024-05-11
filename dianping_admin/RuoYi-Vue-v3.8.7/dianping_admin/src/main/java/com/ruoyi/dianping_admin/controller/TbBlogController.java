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
import com.ruoyi.dianping_admin.domain.TbBlog;
import com.ruoyi.dianping_admin.service.ITbBlogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户博客
Controller
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
@RestController
@RequestMapping("/dianping_admin/blog")
public class TbBlogController extends BaseController
{
    @Autowired
    private ITbBlogService tbBlogService;

    /**
     * 查询用户博客列表
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:blog:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbBlog tbBlog)
    {
        startPage();
        List<TbBlog> list = tbBlogService.selectTbBlogList(tbBlog);
        return getDataTable(list);
    }

    /**
     * 导出用户博客列表
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:blog:export')")
    @Log(title = "用户博客 ", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TbBlog tbBlog)
    {
        List<TbBlog> list = tbBlogService.selectTbBlogList(tbBlog);
        ExcelUtil<TbBlog> util = new ExcelUtil<TbBlog>(TbBlog.class);
        util.exportExcel(response, list, "用户博客 数据");
    }

    /**
     * 获取用户博客详细信息
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:blog:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tbBlogService.selectTbBlogById(id));
    }

    /**
     * 新增用户博客
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:blog:add')")
    @Log(title = "用户博客 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbBlog tbBlog)
    {
        return toAjax(tbBlogService.insertTbBlog(tbBlog));
    }

    /**
     * 修改用户博客
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:blog:edit')")
    @Log(title = "用户博客 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbBlog tbBlog)
    {
        return toAjax(tbBlogService.updateTbBlog(tbBlog));
    }

    /**
     * 删除用户博客
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:blog:remove')")
    @Log(title = "用户博客 ", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbBlogService.deleteTbBlogByIds(ids));
    }
}
