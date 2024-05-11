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
import com.ruoyi.dianping_admin.domain.TbFollow;
import com.ruoyi.dianping_admin.service.ITbFollowService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户关注Controller
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
@RestController
@RequestMapping("/dianping_admin/follow")
public class TbFollowController extends BaseController
{
    @Autowired
    private ITbFollowService tbFollowService;

    /**
     * 查询用户关注列表
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:follow:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbFollow tbFollow)
    {
        startPage();
        List<TbFollow> list = tbFollowService.selectTbFollowList(tbFollow);
        return getDataTable(list);
    }

    /**
     * 导出用户关注列表
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:follow:export')")
    @Log(title = "用户关注", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TbFollow tbFollow)
    {
        List<TbFollow> list = tbFollowService.selectTbFollowList(tbFollow);
        ExcelUtil<TbFollow> util = new ExcelUtil<TbFollow>(TbFollow.class);
        util.exportExcel(response, list, "用户关注数据");
    }

    /**
     * 获取用户关注详细信息
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:follow:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tbFollowService.selectTbFollowById(id));
    }

    /**
     * 新增用户关注
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:follow:add')")
    @Log(title = "用户关注", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbFollow tbFollow)
    {
        return toAjax(tbFollowService.insertTbFollow(tbFollow));
    }

    /**
     * 修改用户关注
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:follow:edit')")
    @Log(title = "用户关注", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbFollow tbFollow)
    {
        return toAjax(tbFollowService.updateTbFollow(tbFollow));
    }

    /**
     * 删除用户关注
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:follow:remove')")
    @Log(title = "用户关注", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbFollowService.deleteTbFollowByIds(ids));
    }
}
