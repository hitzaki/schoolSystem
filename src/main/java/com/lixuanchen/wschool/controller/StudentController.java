package com.lixuanchen.wschool.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lixuanchen.wschool.pojo.Student;
import com.lixuanchen.wschool.service.StudentService;
import com.lixuanchen.wschool.util.MD5;
import com.lixuanchen.wschool.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "学生控制器")
@RestController
@RequestMapping("sms/studentController")
public class StudentController {
    @Resource
    private StudentService studentService;

    @ApiOperation("查询学生信息,分页带条件")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentsByOpr(@PathVariable Integer pageSize, @PathVariable Integer pageNo,
                                   String name, String clazzName){
        Page<Student> page = new Page<>(pageNo, pageSize);
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        if(!("".equals(name) || name == null)) wrapper.like("name", name);
        if(!("".equals(clazzName) || clazzName == null)) wrapper.eq("clazz_name", clazzName);
        wrapper.groupBy("id");
        return Result.ok(studentService.page(page, wrapper));
    }

    @ApiOperation("增加学生信息")
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(@RequestBody Student student){
        // 如果是修改信息, 封装的student对象的密码为null或""
        String password = student.getPassword();
        if (!(password == null || "".equals(password))) {
            student.setPassword(MD5.encrypt(password));
        }
        return studentService.saveOrUpdate(student)? Result.ok(): Result.fail();
    }

    @ApiOperation("删除一个或者多个学生信息")
    @DeleteMapping("/delStudentById")
    public Result delStudentById(@RequestBody List<Integer> ids){
        return studentService.removeByIds(ids)? Result.ok(): Result.fail();
    }

}
