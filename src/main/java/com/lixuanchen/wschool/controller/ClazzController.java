package com.lixuanchen.wschool.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lixuanchen.wschool.pojo.Clazz;
import com.lixuanchen.wschool.service.ClazzService;
import com.lixuanchen.wschool.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "班级控制器")
@RestController
@RequestMapping("sms/clazzController")
public class ClazzController {
    @Resource
    private ClazzService clazzService;

    @ApiOperation("查询班级信息,分页带条件")
    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzsByOpr(@PathVariable Integer pageNo, @PathVariable Integer pageSize,
                                 String name, String gradeName){
        Page<Clazz> page = new Page<>(pageNo, pageSize);
        QueryWrapper<Clazz> wrapper = new QueryWrapper<>();
        if(!("".equals(name) || name == null)) wrapper.like("name", name);
        if(!("".equals(gradeName) || gradeName == null)) wrapper.eq("grade_name", gradeName);
        wrapper.groupBy("id");
        return Result.ok(clazzService.page(page, wrapper));
    }

    @ApiOperation("保存或者修改班级信息")
    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClazz(@RequestBody Clazz clazz){
        return clazzService.saveOrUpdate(clazz)? Result.ok(): Result.fail();
    }

    @ApiOperation("删除一个或者多个班级信息")
    @DeleteMapping("/deleteClazz")
    public Result deleteClazzByIds(@RequestBody List<Integer> ids){
        return clazzService.removeByIds(ids)? Result.ok(): Result.fail();
    }

    @ApiOperation("获取所有班级的JSON")
    @GetMapping("/getClazzs")
    public Result getClazzs(){
        return Result.ok(clazzService.list());
    }
}
