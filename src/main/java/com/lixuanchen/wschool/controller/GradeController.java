package com.lixuanchen.wschool.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lixuanchen.wschool.pojo.Grade;
import com.lixuanchen.wschool.service.GradeService;
import com.lixuanchen.wschool.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "年级控制器")
@RestController
@RequestMapping("sms/gradeController")
public class GradeController {
    @Resource
    public GradeService gradeService;

    @ApiOperation("查询年级信息,分页带条件")
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGradePage(@PathVariable Integer pageNo, @PathVariable Integer pageSize, String gradeName){
        Page<Grade> page = new Page<>((long)pageNo, (long)pageSize);
        QueryWrapper<Grade> wrapper = new QueryWrapper<>();
        if(!("".equals(gradeName) || gradeName == null)) wrapper.like("name", gradeName);
        wrapper.groupBy("id");
        return Result.ok(gradeService.page(page, wrapper));
    }

    @ApiOperation("添加或者修改年级信息")
    @PostMapping("/saveOrUpdateGrade")
    public  Result saveOrUpdateGrade(@RequestBody Grade grade){
        // 调用服务层方法,实现添加或者修改年级信息
        return gradeService.saveOrUpdate(grade)? Result.ok(): Result.fail();
    }

    @ApiOperation("删除一个或者多个grade信息")
    @DeleteMapping("/deleteGrade")
    public Result deleteGradeById(@RequestBody List<Integer> ids){
        return gradeService.removeByIds(ids)? Result.ok(): Result.fail();
    }

    @ApiOperation("获取所有Grade信息")
    @GetMapping("/getGrades")
    public Result getGrades(){
        return Result.ok(gradeService.list());
    }
}
