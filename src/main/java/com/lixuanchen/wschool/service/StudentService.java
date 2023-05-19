package com.lixuanchen.wschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lixuanchen.wschool.pojo.LoginForm;
import com.lixuanchen.wschool.pojo.Student;

public interface StudentService extends IService<Student> {
    Student login(LoginForm loginForm);
}
