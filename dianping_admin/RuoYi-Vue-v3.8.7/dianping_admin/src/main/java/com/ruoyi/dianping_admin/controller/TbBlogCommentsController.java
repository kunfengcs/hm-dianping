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
import com.ruoyi.dianping_admin.domain.TbBlogComments;
import com.ruoyi.dianping_admin.service.ITbBlogCommentsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 博客评论Controller
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
@RestController
@RequestMapping("/dianping_admin/comments")
public class TbBlogCommentsController extends BaseController
{
    @Autowired
    private ITbBlogCommentsService tbBlogCommentsService;

    /**
     * 查询博客评论列表
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:comments:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbBlogComments tbBlogComments)
    {
        startPage();
        List<TbBlogComments> list = tbBlogCommentsService.selectTbBlogCommentsList(tbBlogComments);
        return getDataTable(list);
    }

    /**
     * 导出博客评论列表
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:comments:export')")
    @Log(title = "博客评论", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TbBlogComments tbBlogComments)
    {
        List<TbBlogComments> list = tbBlogCommentsService.selectTbBlogCommentsList(tbBlogComments);
        ExcelUtil<TbBlogComments> util = new ExcelUtil<TbBlogComments>(TbBlogComments.class);
        util.exportExcel(response, list, "博客评论数据");
    }

    /**
     * 获取博客评论详细信息
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:comments:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tbBlogCommentsService.selectTbBlogCommentsById(id));
    }

    /**
     * 新增博客评论     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:comments:add')")
    @Log(title = "博客评论", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbBlogComments tbBlogComments)
    {
        return toAjax(tbBlogCommentsService.insertTbBlogComments(tbBlogComments));
    }

    /**
     * 修改博客评论     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:comments:edit')")
    @Log(title = "博客评论", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbBlogComments tbBlogComments)
    {
        return toAjax(tbBlogCommentsService.updateTbBlogComments(tbBlogComments));
    }

    /**
     * 删除博客评论     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:comments:remove')")
    @Log(title = "博客评论", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbBlogCommentsService.deleteTbBlogCommentsByIds(ids));
    }
}
