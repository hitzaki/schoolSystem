package com.lixuanchen.wschool.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lixuanchen.wschool.pojo.Admin;
import com.lixuanchen.wschool.service.AdminService;
import com.lixuanchen.wschool.util.MD5;
import com.lixuanchen.wschool.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "管理员控制器")
@RestController
@RequestMapping("sms/adminController")
public class AdminController {
    @Resource
    public AdminService adminService;

    @ApiOperation("分页获取所有Admin信息【带条件】")
    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(@PathVariable Integer pageNo, @PathVariable Integer pageSize,
                              String adminName){
        Page<Admin> page = new Page<>(pageNo, pageSize);
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        if(!(adminName==null || "".equals(adminName))) wrapper.like("name", adminName);
        wrapper.groupBy("id");
        return Result.ok(adminService.page(page, wrapper));
    }

    @ApiOperation("添加或修改Admin信息")
    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(@RequestBody Admin admin){
        // 如果是修改信息, 封装的admin对象的密码为null或""
        String password = admin.getPassword();
        if(!(password==null || "".equals(password))){
            admin.setPassword(MD5.encrypt(password));
        }
        return adminService.saveOrUpdate(admin)? Result.ok(): Result.fail();
    }

    @ApiOperation("删除Admin信息")
    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(@RequestBody List<Integer> ids){
        return adminService.removeByIds(ids)? Result.ok(): Result.fail();
    }
}
