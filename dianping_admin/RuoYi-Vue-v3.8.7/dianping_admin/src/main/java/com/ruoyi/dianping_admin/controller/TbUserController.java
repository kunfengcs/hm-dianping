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
import com.ruoyi.dianping_admin.domain.TbUser;
import com.ruoyi.dianping_admin.service.ITbUserService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户Controller
 * 
 * @author 王坤峰
 * @date 2024-05-11
 */
@RestController
@RequestMapping("/dianping_admin/user")
public class TbUserController extends BaseController
{
    @Autowired
    private ITbUserService tbUserService;

    /**
     * 查询用户列表
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbUser tbUser)
    {
        startPage();
        List<TbUser> list = tbUserService.selectTbUserList(tbUser);
        return getDataTable(list);
    }

    /**
     * 导出用户列表
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:user:export')")
    @Log(title = "用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TbUser tbUser)
    {
        List<TbUser> list = tbUserService.selectTbUserList(tbUser);
        ExcelUtil<TbUser> util = new ExcelUtil<TbUser>(TbUser.class);
        util.exportExcel(response, list, "用户数据");
    }

    /**
     * 获取用户详细信息
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:user:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tbUserService.selectTbUserById(id));
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:user:add')")
    @Log(title = "用户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbUser tbUser)
    {
        return toAjax(tbUserService.insertTbUser(tbUser));
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:user:edit')")
    @Log(title = "用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbUser tbUser)
    {
        return toAjax(tbUserService.updateTbUser(tbUser));
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:user:remove')")
    @Log(title = "用户", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbUserService.deleteTbUserByIds(ids));
    }
    /**
     * 用户总数
     */
    @PreAuthorize("@ss.hasPermi('dianping_admin:user:list')")
    @Log(title = "用户总数", businessType = BusinessType.OTHER)
    @GetMapping("/count")
    public AjaxResult count()
    {
        return AjaxResult.success(tbUserService.count());
    }
}
