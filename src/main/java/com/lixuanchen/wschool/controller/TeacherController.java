package com.lixuanchen.wschool.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lixuanchen.wschool.pojo.Teacher;
import com.lixuanchen.wschool.service.TeacherService;
import com.lixuanchen.wschool.util.MD5;
import com.lixuanchen.wschool.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "教师控制器")
@RestController
@RequestMapping("sms/teacherController")
public class TeacherController {
    @Resource
    public TeacherService teacherService;

    @ApiOperation("获取教师信息,分页带条件")
    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result getTeachers(@PathVariable("pageNo") Integer pageNo, @PathVariable("pageSize") Integer pageSize,
                              String name, String clazzName){
        Page<Teacher> page = new Page<>(pageNo, pageSize);
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        if(!("".equals(name) || name == null)) wrapper.like("name", name);
        if(!("".equals(clazzName) || clazzName == null)) wrapper.eq("clazz_name", clazzName);
        wrapper.groupBy("id");
        return Result.ok(teacherService.page(page, wrapper));
    }

    @ApiOperation("添加和修改教师信息")
    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(@RequestBody Teacher teacher){
        // 如果是修改信息, 封装的teacher对象的密码为null或""
        String password = teacher.getPassword();
        if(!(password==null || "".equals(password))){
            teacher.setPassword(MD5.encrypt(password));
        }
        return teacherService.saveOrUpdate(teacher)? Result.ok(): Result.fail();
    }

    @ApiOperation("删除一个或者多个教师信息")
    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(@RequestBody List<Integer> ids){
        return teacherService.removeByIds(ids)? Result.ok(): Result.fail();
    }
}
